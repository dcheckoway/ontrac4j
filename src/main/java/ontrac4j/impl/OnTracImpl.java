package ontrac4j.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import ontrac4j.OnTrac;
import ontrac4j.xml.ShipmentRequest;
import ontrac4j.xml.ShipmentResponse;
import ontrac4j.xml.TrackingShipment;
import ontrac4j.xml.TrackingShipmentList;
import ontrac4j.xml.ZipCode;
import ontrac4j.xml.ZipCodeList;

public class OnTracImpl implements OnTrac {
    private static final String TEST_ROOT_URL = "https://www.shipontrac.net/OnTracTestWebServices/OnTracServices.svc";
    private static final String PRODUCTION_ROOT_URL = "https://www.shipontrac.net/OnTracWebServices/OnTracServices.svc";

    private static final FastDateFormat LAST_UPDATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private final String rootUrl;
    private final String account;
    private final String password;
    
    private OnTracImpl(String account, String password, String rootUrl) {
        this.account = account;
        this.password = password;
        this.rootUrl = rootUrl;
    }

    /** {@inheritDoc} */
    public Map<String,ZipCode> getZipCodes() throws IOException {
        return getZipCodes(null);
    }
    
    /** {@inheritDoc} */
    public Map<String,ZipCode> getZipCodes(Date lastUpdate) throws IOException {
        String url = rootUrl + "/V1/" + account + "/Zips?pw=" + password;
        if (lastUpdate != null) {
            url += "&lastUpdate=" + LAST_UPDATE_FORMAT.format(lastUpdate);
        }
        ZipCodeList zipCodeList = unmarshal(url, ZipCodeList.class);
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
        String url = rootUrl + "/V1/" + account + "/shipments?pw=" + password;
        // TODO
        throw new IOException("unimplemented");
    }

    /** {@inheritDoc} */
    public TrackingShipment trackShipment(String trackingNumber) throws IOException {
        String url = rootUrl + "/V1/" + account + "/shipments?pw=" + password + "&requestType=track&tn=" + trackingNumber;
        System.out.println(url);
        TrackingShipmentList trackingShipmentList = unmarshal(url, TrackingShipmentList.class);
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

    @SuppressWarnings("unchecked")
    private static <T> T unmarshal(String url, Class<T> clazz) throws IOException {
        Unmarshaller unmarshaller;
        try {
            unmarshaller = JAXBContext.newInstance(clazz.getPackage().getName()).createUnmarshaller();
            Object obj = unmarshaller.unmarshal(new URL(url).openStream());
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