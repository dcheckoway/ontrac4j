package ontrac4j.impl;

import java.io.InputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
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

import ontrac4j.OnTrac;
import ontrac4j.xml.ArrayOfShipmentRequest;
import ontrac4j.xml.ObjectFactory;
import ontrac4j.xml.ShipmentRequest;
import ontrac4j.xml.ShipmentRequestList;
import ontrac4j.xml.ShipmentResponse;
import ontrac4j.xml.ShipmentResponseList;
import ontrac4j.xml.TrackingShipment;
import ontrac4j.xml.TrackingShipmentList;
import ontrac4j.xml.ZipCode;
import ontrac4j.xml.ZipCodeList;

public class OnTracImpl implements OnTrac {
    private static final Logger LOG = Logger.getLogger(OnTracImpl.class.getName());
    
    private static final String TEST_ROOT_URL = "https://www.shipontrac.net/OnTracTestWebServices/OnTracServices.svc";
    private static final String PRODUCTION_ROOT_URL = "https://www.shipontrac.net/OnTracWebServices/OnTracServices.svc";

    private static final FastDateFormat LAST_UPDATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private static final int DEFAULT_MAX_RETRIES = 5;
    private static final long DEFAULT_SLEEP_BETWEEN_RETRIES = 3000;

    private static final ConcurrentMap<String,Unmarshaller> UNMARSHALLERS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String,Marshaller> MARSHALLERS = new ConcurrentHashMap<>();

    private final HttpClient httpClient = HttpClientBuilder.create().build();
    private final ObjectFactory objectFactory = new ObjectFactory();
    private final Config config;
    
    private OnTracImpl(Config config) {
        this.config = config;
    }

    /** {@inheritDoc} */
    public Map<String,ZipCode> getZipCodes() throws IOException {
        return getZipCodes(null);
    }
    
    /** {@inheritDoc} */
    public Map<String,ZipCode> getZipCodes(Date lastUpdate) throws IOException {
        String url = "/V1/" + config.getAccount() + "/Zips?pw=" + config.getPassword();
        if (lastUpdate != null) {
            url += "&lastUpdate=" + LAST_UPDATE_FORMAT.format(lastUpdate);
        }
        ZipCodeList zipCodeList = get(url, ZipCodeList.class);
        if (StringUtils.isNotBlank(zipCodeList.getError())) {
            throw new GeneralException(zipCodeList.getError());
        }
        Map<String,ZipCode> map = new LinkedHashMap<>();
        for (ZipCode zipCode : zipCodeList.getZips().getZips()) {
            map.put(zipCode.getZipCode(), zipCode);
        }
        return map;
    }

    /** {@inheritDoc} */
    public ShipmentResponse createShipment(ShipmentRequest shipmentRequest) throws IOException {
        String url = "/V1/" + config.getAccount() + "/shipments?pw=" + config.getPassword();
        ShipmentRequestList shipmentRequestList = new ShipmentRequestList();
        shipmentRequestList.setShipments(new ArrayOfShipmentRequest());
        shipmentRequestList.getShipments().getShipments().add(shipmentRequest);
        ShipmentResponseList shipmentResponseList = post(url, objectFactory.createOnTracShipmentRequest(shipmentRequestList), ShipmentResponseList.class);
        if (StringUtils.isNotBlank(shipmentResponseList.getError())) {
            throw new GeneralException(shipmentResponseList.getError());
        } else if (CollectionUtils.isEmpty(shipmentResponseList.getShipments().getShipments())) {
            throw new GeneralException("Shipment not created, no ShipmentResponse found in response");
        } else {
            ShipmentResponse shipmentResponse = shipmentResponseList.getShipments().getShipments().get(0);
            if (StringUtils.isNotBlank(shipmentResponse.getError())) {
                throw new GeneralException(shipmentResponse.getError());
            } else {
                return shipmentResponse;
            }
        }
    }

    /** {@inheritDoc} */
    public TrackingShipment trackShipment(String trackingNumber) throws IOException {
        String url = "/V1/" + config.getAccount() + "/shipments?pw=" + config.getPassword() + "&requestType=track&tn=" + trackingNumber;
        TrackingShipmentList trackingShipmentList = get(url, TrackingShipmentList.class);
        if (StringUtils.isNotBlank(trackingShipmentList.getError())) {
            throw new GeneralException(trackingShipmentList.getError());
        } else if (CollectionUtils.isEmpty(trackingShipmentList.getShipments().getShipments())) {
            throw new GeneralException("Shipment not found: " + trackingNumber);
        } else {
            TrackingShipment trackingShipment = trackingShipmentList.getShipments().getShipments().get(0);
            if (StringUtils.isNotBlank(trackingShipment.getError())) {
                throw new GeneralException(trackingShipment.getError());
            } else {
                return trackingShipment;
            }
        }
    }

    private <T> T get(String url, Class<T> clazz) throws IOException {
        return executeAndUnmarshal(new HttpGet(config.getRootUrl() + url), clazz);
    }

    private <T> T post(String url, Object request, Class<T> clazz) throws IOException {
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
                HttpResponse httpResponse = httpClient.execute(httpRequestBase);
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

    private static Unmarshaller getUnmarshaller(Class clazz) throws JAXBException {
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
        if (object instanceof JAXBElement) {
            object = ((JAXBElement)object).getValue();
        }
        String packageName = object.getClass().getPackage().getName();
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
    
    public static class GeneralException extends IOException {
        private static final long serialVersionUID = 1L;
        
        private GeneralException(String message) {
            super(message);
        }
    }

    private static final class Config {
        private String account;
        private String password;
        private String rootUrl = PRODUCTION_ROOT_URL;
        private int connectTimeout = -1;
        private int socketTimeout = -1;
        private int maxRetries = DEFAULT_MAX_RETRIES;
        private long sleepBetweenRetries = DEFAULT_SLEEP_BETWEEN_RETRIES;

        public String getAccount() {
            return account;
        }
        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

        public String getRootUrl() {
            return rootUrl;
        }
        public void setRootUrl(String rootUrl) {
            this.rootUrl = rootUrl;
        }

        public int getConnectTimeout() {
            return connectTimeout;
        }
        public void setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public int getSocketTimeout() {
            return socketTimeout;
        }
        public void setSocketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
        }

        public int getMaxRetries() {
            return maxRetries;
        }
        public void setMaxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
        }

        public long getSleepBetweenRetries() {
            return sleepBetweenRetries;
        }
        public void setSleepBetweenRetries(long sleepBetweenRetries) {
            this.sleepBetweenRetries = sleepBetweenRetries;
        }
    }
    
    public static final class Builder implements OnTrac.Builder {
        private final Config config = new Config();
        
        public Builder account(String account) {
            config.setAccount(account);
            return this;
        }

        public Builder password(String password) {
            config.setPassword(password);
            return this;
        }

        public Builder rootUrl(String rootUrl) {
            config.setRootUrl(rootUrl);
            return this;
        }

        public Builder test() {
            return rootUrl(TEST_ROOT_URL);
        }

        public Builder production() {
            return rootUrl(PRODUCTION_ROOT_URL);
        }

        public Builder connectTimeout(int connectTimeout) {
            config.setConnectTimeout(connectTimeout);
            return this;
        }
        
        public Builder socketTimeout(int socketTimeout) {
            config.setSocketTimeout(socketTimeout);
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            config.setMaxRetries(maxRetries);
            return this;
        }
        
        public Builder sleepBetweenRetries(long sleepBetweenRetries) {
            config.setSleepBetweenRetries(sleepBetweenRetries);
            return this;
        }
        
        public OnTrac build() {
            return new OnTracImpl(config);
        }
    }
}