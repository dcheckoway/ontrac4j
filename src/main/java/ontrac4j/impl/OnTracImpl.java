package ontrac4j.impl;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import ontrac4j.OnTrac;
import ontrac4j.xml.ArrayOfShipmentRequest;
import ontrac4j.xml.ObjectFactory;
import ontrac4j.xml.ShipmentRequest;
import ontrac4j.xml.ShipmentRequestList;
import ontrac4j.xml.ShipmentResponse;
import ontrac4j.xml.ShipmentResponseList;
import ontrac4j.xml.ShipmentUpdate;
import ontrac4j.xml.ShipmentUpdateList;
import ontrac4j.xml.TrackingShipment;
import ontrac4j.xml.TrackingShipmentList;
import ontrac4j.xml.ZipCode;
import ontrac4j.xml.ZipCodeList;

public class OnTracImpl implements OnTrac {
    private static final FastDateFormat LAST_UPDATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private static final String VERSION_PATH = "/V1/";
    
    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    private final Config config;
    private final Client client;
    
    public OnTracImpl(Config config) {
        this.config = config;
        this.client = new Client(config);
    }

    /** {@inheritDoc} */
    public Map<String,ZipCode> getZipCodes() throws IOException {
        return getZipCodes(null);
    }
    
    /** {@inheritDoc} */
    public Map<String,ZipCode> getZipCodes(Date lastUpdate) throws IOException {
        String url = VERSION_PATH + config.getAccount() + "/Zips?pw=" + config.getPassword();
        if (lastUpdate != null) {
            url += "&lastUpdate=" + LAST_UPDATE_FORMAT.format(lastUpdate);
        }
        ZipCodeList zipCodeList = client.get(url, ZipCodeList.class);
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
        String url = VERSION_PATH + config.getAccount() + "/shipments?pw=" + config.getPassword();
        ShipmentRequestList shipmentRequestList = new ShipmentRequestList();
        shipmentRequestList.setShipments(new ArrayOfShipmentRequest());
        shipmentRequestList.getShipments().getShipments().add(shipmentRequest);
        ShipmentResponseList shipmentResponseList = client.post(url, OBJECT_FACTORY.createOnTracShipmentRequest(shipmentRequestList), ShipmentResponseList.class);
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
    public ShipmentUpdate getShipmentUpdate(String trackingNumber) throws IOException {
        String url = VERSION_PATH + config.getAccount() + "/shipments?pw=" + config.getPassword() + "&requestType=details&tn=" + trackingNumber;
        ShipmentUpdateList shipmentUpdateList = client.get(url, ShipmentUpdateList.class);
        if (StringUtils.isNotBlank(shipmentUpdateList.getError())) {
            throw new GeneralException(shipmentUpdateList.getError());
        } else if (CollectionUtils.isEmpty(shipmentUpdateList.getShipments().getShipments())) {
            throw new GeneralException("Shipment not found: " + trackingNumber);
        } else {
            ShipmentUpdate shipmentUpdate = shipmentUpdateList.getShipments().getShipments().get(0);
            if (StringUtils.isNotBlank(shipmentUpdate.getError())) {
                throw new GeneralException(shipmentUpdate.getError());
            } else {
                return shipmentUpdate;
            }
        }
    }

    /** {@inheritDoc} */
    public TrackingShipment trackShipment(String trackingNumber) throws IOException {
        String url = VERSION_PATH + config.getAccount() + "/shipments?pw=" + config.getPassword() + "&requestType=track&tn=" + trackingNumber;
        TrackingShipmentList trackingShipmentList = client.get(url, TrackingShipmentList.class);
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
    
    public static class GeneralException extends IOException {
        private static final long serialVersionUID = 1L;
        
        private GeneralException(String message) {
            super(message);
        }
    }
}