package ontrac4j.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import ontrac4j.OnTrac;
import ontrac4j.ZipCode;
import ontrac4j.xml.ZipCodeList;

public class OnTracImpl implements OnTrac {
    static final String TEST_ROOT_URL = "https://www.shipontrac.net/OnTracTestWebServices/OnTracServices.svc";
    static final String PRODUCTION_ROOT_URL = "https://www.shipontrac.net/OnTracWebServices/OnTracServices.svc";

    static final FastDateFormat LAST_UPDATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private static final Unmarshaller UNMARSHALLER;
    static {
        try {
            UNMARSHALLER = JAXBContext.newInstance("ontrac4j.xml").createUnmarshaller();
        } catch (JAXBException e) {
            throw new IllegalStateException("Failed to initialize unmarshaller", e);
        }
    }
    
    private final String rootUrl;
    private final String account;
    private final String password;
    
    public OnTracImpl(String account, String password, String rootUrl) {
        this.account = account;
        this.password = password;
        this.rootUrl = rootUrl;
    }
    
    public List<ZipCode> getZipCodes() throws IOException {
        return getZipCodes(null);
    }
    
    public List<ZipCode> getZipCodes(Date lastUpdate) throws IOException {
        String url = rootUrl + "/V1/" + account + "/Zips?pw=" + password;
        if (lastUpdate != null) {
            url += "&lastUpdate=" + LAST_UPDATE_FORMAT.format(lastUpdate);
        }
        ZipCodeList zipCodeList = unmarshal(url, ZipCodeList.class);
        if (StringUtils.isNotBlank(zipCodeList.getError())) {
            throw new GeneralException(zipCodeList.getError());
        }
        List<ZipCode> zipCodes = new ArrayList<>(zipCodeList.getZips().getZip().size());
        for (ontrac4j.xml.ZipCode zipCode : zipCodeList.getZips().getZip()) {
            zipCodes.add(new ZipCode(zipCode));
        }
        return zipCodes;
    }

    @SuppressWarnings("unchecked")
    static <T> T unmarshal(String url, Class<T> clazz) throws IOException {
        try {
            Object obj = UNMARSHALLER.unmarshal(new URL(url).openStream());
            if (obj instanceof JAXBElement) {
                return ((JAXBElement<T>)obj).getValue();
            } else {
                return (T)obj;
            }
        } catch (JAXBException e) {
            throw new IOException("Failed to unmarshal object from URL", e);
        }
    }

    public static class GeneralException extends IOException {
        private static final long serialVersionUID = 1L;
        
        private GeneralException(String message) {
            super(message);
        }
    }
    
    public static class Builder implements OnTrac.Builder {
        private String account;
        private String password;
        private String rootUrl = PRODUCTION_ROOT_URL;
        
        public Builder account(String account) {
            this.account = account;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder rootUrl(String rootUrl) {
            this.rootUrl = rootUrl;
            return this;
        }

        public Builder test() {
            return rootUrl(TEST_ROOT_URL);
        }

        public Builder production() {
            return rootUrl(PRODUCTION_ROOT_URL);
        }

        public OnTrac build() {
            return new OnTracImpl(account, password, rootUrl);
        }
    }
}