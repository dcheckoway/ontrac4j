package ontrac4j.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import ontrac4j.OnTrac;
import ontrac4j.schema.ArrayOfShipmentRequest;
import ontrac4j.schema.Dim;
import ontrac4j.schema.ObjectFactory;
import ontrac4j.schema.PickupRequest;
import ontrac4j.schema.PickupResponse;
import ontrac4j.schema.RateQuote;
import ontrac4j.schema.RateShipment;
import ontrac4j.schema.RateShipmentList;
import ontrac4j.schema.ShipmentRequest;
import ontrac4j.schema.ShipmentRequestList;
import ontrac4j.schema.ShipmentResponse;
import ontrac4j.schema.ShipmentResponseList;
import ontrac4j.schema.ShipmentUpdate;
import ontrac4j.schema.ShipmentUpdateList;
import ontrac4j.schema.TrackingShipment;
import ontrac4j.schema.TrackingShipmentList;
import ontrac4j.schema.ZipCode;
import ontrac4j.schema.ZipCodeList;

public class OnTracImpl implements OnTrac {
    private static final FastDateFormat LAST_UPDATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    private static final String VERSION_PATH = "/V1/";
    
    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

    private static final DecimalFormat AMOUNT_FORMAT = new DecimalFormat("0.00");

    private final Config config;
    private final Client client;
    
    public OnTracImpl(Config config) {
        this.config = config;
        this.client = new Client(config);
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
    public Map<String,RateQuote> getRateQuote(RateShipment rateShipment) throws IOException {
        return getRateQuote(rateShipment, null);
    }
    
    /** {@inheritDoc} */
    public Map<String,RateQuote> getRateQuote(RateShipment rateShipment, String service) throws IOException {
        String url = VERSION_PATH + config.getAccount() + "/rates?pw=" + config.getPassword() + "&packages=" + urlEncode(makePackageParam(rateShipment, service));
        RateShipmentList rateShipmentList = client.get(url, RateShipmentList.class);
        if (StringUtils.isNotBlank(rateShipmentList.getError())) {
            throw new GeneralException(rateShipmentList.getError());
        } else if (CollectionUtils.isEmpty(rateShipmentList.getShipments().getShipments())) {
            throw new GeneralException("No rates available");
        } else {
            RateShipment rateShipmentResponse = rateShipmentList.getShipments().getShipments().get(0);
            if (StringUtils.isNotBlank(rateShipmentResponse.getError())) {
                throw new GeneralException(rateShipmentResponse.getError());
            } else if (CollectionUtils.isEmpty(rateShipmentResponse.getRates().getRates())) {
                throw new GeneralException("No rates available");
            } else {
                Map<String,RateQuote> rateQuoteMap = new LinkedHashMap<>();
                for (RateQuote rateQuote : rateShipmentResponse.getRates().getRates()) {
                    rateQuoteMap.put(rateQuote.getService(), rateQuote);
                }
                return rateQuoteMap;
            }
        }
    }

    /** {@inheritDoc} */
    public String schedulePickup(PickupRequest pickupRequest) throws IOException {
        String url = VERSION_PATH + config.getAccount() + "/pickups?pw=" + config.getPassword();
        PickupResponse pickupResponse = client.post(url, OBJECT_FACTORY.createOnTracPickupRequest(pickupRequest), PickupResponse.class);
        if (StringUtils.isNotBlank(pickupResponse.getError())) {
            throw new GeneralException(pickupResponse.getError());
        } else {
            return pickupResponse.getTracking();
        }
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

    static String makePackageParam(RateShipment rateShipment, String service) {
        List<String> list = new ArrayList<>();
        list.add(rateShipment.getUID());
        list.add(rateShipment.getPUZip());
        list.add(rateShipment.getDelzip());
        list.add(String.valueOf(rateShipment.isResidential()));
        list.add(AMOUNT_FORMAT.format(rateShipment.getCOD()));
        list.add(String.valueOf(rateShipment.isSaturdayDel()));
        list.add(AMOUNT_FORMAT.format(rateShipment.getDeclared()));
        list.add(String.valueOf(rateShipment.getWeight()));
        list.add(dimToString(rateShipment.getDIM()));
        list.add(StringUtils.trimToEmpty(service));
        return StringUtils.join(list, ';');
    }

    static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException("Failed to URL encode value", e);
        }
    }

    static String dimToString(Dim dim) {
        return dim.getLength() + "X" + dim.getWidth() + "X" + dim.getHeight();
    }
    
    public static class GeneralException extends IOException {
        private static final long serialVersionUID = 1L;
        
        private GeneralException(String message) {
            super(message);
        }
    }
}
