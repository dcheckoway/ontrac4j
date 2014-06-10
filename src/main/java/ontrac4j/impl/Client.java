package ontrac4j.impl;

import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class.getName());
    
    private static final ConcurrentMap<String,Unmarshaller> UNMARSHALLERS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String,Marshaller> MARSHALLERS = new ConcurrentHashMap<>();

    private static final HttpClient HTTP_CLIENT = HttpClientBuilder.create().build();
    
    private final Config config;
    
    public Client(Config config) {
        this.config = config;
    }

    public <T> T get(String url, Class<T> clazz) throws IOException {
        return executeAndUnmarshal(new HttpGet(config.getRootUrl() + url), clazz);
    }

    public <T> T post(String url, Object request, Class<T> clazz) throws IOException {
        HttpPost httpPost = new HttpPost(config.getRootUrl() + url);
        httpPost.setEntity(new StringEntity(marshal(request)));
        return executeAndUnmarshal(httpPost, clazz);
    }

    private <T> T executeAndUnmarshal(HttpRequestBase httpRequestBase, Class<T> clazz) throws IOException {
        httpRequestBase.setConfig(RequestConfig.custom().setConnectTimeout(config.getConnectTimeout()).setSocketTimeout(config.getSocketTimeout()).build());
        httpRequestBase.setHeader(HttpHeaders.ACCEPT, "*/*");
        
        int attempt = 0;
        int maxAttempts = 1 + Math.max(config.getMaxRetries(), 0);
        IOException lastException = null;
        while (attempt < maxAttempts) {
            if (attempt++ > 0) {
                LOG.fine("Sleeping for " + config.getSleepBetweenRetries() + "ms before retrying");
                try {
                    TimeUnit.MILLISECONDS.sleep(config.getSleepBetweenRetries());
                } catch (InterruptedException e) {
                    LOG.warning("Sleep interrupted");
                }
            }
            
            HttpEntity httpEntity = null;
            try {
                HttpResponse httpResponse = HTTP_CLIENT.execute(httpRequestBase);
                httpEntity = httpResponse.getEntity();
                StatusLine statusLine = httpResponse.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (LOG.isLoggable(Level.FINER)) {
                    LOG.finer("HTTP response was " + statusCode + " " + statusLine.getReasonPhrase());
                }
                if (statusCode == HttpStatus.SC_OK) {
                    return unmarshal(httpEntity.getContent(), clazz);
                } else {
                    throw new HttpResponseException(statusCode, "Got " + statusCode + " " + statusLine.getReasonPhrase());
                }
            } catch (IOException e) {
                lastException = e;
            } finally {
                EntityUtils.consumeQuietly(httpEntity);
            }
        }

        LOG.info("HTTP failed after " + attempt + " attempt(s), throwing last exception caught");
        throw lastException;
    }

    private <T> String marshal(Object object) throws IOException {
        populateDefaultValues(object);
        
        StringWriter stringWriter = new StringWriter();
        try {
            getMarshaller(object).marshal(object, stringWriter);
        } catch (JAXBException e) {
            throw new IOException("Failed to marshal " + object.getClass().getName(), e);
        }
        return stringWriter.toString();
    }
    
    @SuppressWarnings("unchecked")
    private static <T> T unmarshal(InputStream inputStream, Class<T> clazz) throws IOException {
        try {
            Object obj = getUnmarshaller(clazz).unmarshal(inputStream);
            if (obj instanceof JAXBElement) {
                return ((JAXBElement<T>)obj).getValue();
            } else {
                return (T)obj;
            }
        } catch (JAXBException e) {
            throw new IOException("Failed to unmarshal object from URL", e);
        }
    }

    private static Unmarshaller getUnmarshaller(Class<?> clazz) throws JAXBException {
        String packageName = clazz.getPackage().getName();
        Unmarshaller unmarshaller = UNMARSHALLERS.get(packageName);
        if (unmarshaller == null) {
            unmarshaller = JAXBContext.newInstance(packageName).createUnmarshaller();
            Unmarshaller previous = UNMARSHALLERS.putIfAbsent(packageName, unmarshaller);
            if (previous != null) {
                unmarshaller = previous;
            }
        }
        return unmarshaller;
    }

    private static Marshaller getMarshaller(Object object) throws JAXBException {
        Object value = object instanceof JAXBElement ? ((JAXBElement)object).getValue() : object;
        String packageName = value.getClass().getPackage().getName();
        Marshaller marshaller = MARSHALLERS.get(packageName);
        if (marshaller == null) {
            marshaller = JAXBContext.newInstance(packageName).createMarshaller();
            Marshaller previous = MARSHALLERS.putIfAbsent(packageName, marshaller);
            if (previous != null) {
                marshaller = previous;
            }
        }
        return marshaller;
    }

    private static void populateDefaultValues(Object object) {
        Object target = object instanceof JAXBElement ? ((JAXBElement)object).getValue() : object;
        if (target == null) {
            return;
        }
        Class<?> clazz = target.getClass();
        XmlType xmlType = clazz.getDeclaredAnnotation(XmlType.class);
        if (xmlType != null) {
            for (Field field : clazz.getDeclaredFields()) {
                XmlElement xmlElement = field.getAnnotation(XmlElement.class);
                if (xmlElement != null) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    Object fieldValue;
                    try {
                        fieldValue = field.get(target);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Failed to get value of " + field.getName() + " on  " + clazz.getName(), e);
                    }

                    if (fieldValue == null && xmlElement.required()) {
                        LOG.fine("Setting required field \"" + field.getName() + "\" on " + clazz.getName() + " to default instance of " + field.getType().getName());
                        try {
                            fieldValue = field.getType().newInstance();
                            field.set(target, fieldValue);
                        } catch (IllegalAccessException | InstantiationException e) {
                            throw new IllegalStateException("Failed to set default instance of " + field.getType().getName() + " as \"" + field.getName() + "\" on  " + clazz.getName(), e);
                        }
                    }
                    
                    // Walk down the hierarchy
                    populateDefaultValues(fieldValue);
                }
            }
        } else if (target instanceof Collection) {
            for (Object element : ((Collection<?>)target)) {
                populateDefaultValues(element);
            }
        }
    }
}