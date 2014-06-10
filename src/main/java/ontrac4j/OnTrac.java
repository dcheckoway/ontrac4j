package ontrac4j;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import ontrac4j.schema.ShipmentRequest;
import ontrac4j.schema.ShipmentResponse;
import ontrac4j.schema.ShipmentUpdate;
import ontrac4j.schema.TrackingShipment;
import ontrac4j.schema.ZipCode;

/**
 * Interface to the OnTrac shipping API
 */
public interface OnTrac {
    /**
     * Get all zip codes
     * @return all zip codes in the system
     */
    Map<String,ZipCode> getZipCodes() throws IOException;

    /**
     * Get all zip codes updated since a specific date
     * @param lastUpdate the date of the last zip request made to the OnTrac system
     * @return only zips that have been added or changed since lastUpdate
     */
    Map<String,ZipCode> getZipCodes(Date lastUpdate) throws IOException;

    /**
     * Create a new shipment
     * @param shipmentRequest the shipment request details to create
     * @return the shipment response
     */
    ShipmentResponse createShipment(ShipmentRequest shipmentRequest) throws IOException;

    /**
     * Get updated details for a shipment
     * @param trackingNumber the tracking number of the shipment whose details are desired
     * @return the updated shipment details
     */
    ShipmentUpdate getShipmentUpdate(String trackingNumber) throws IOException;

    /**
     * Track a shipment
     * @param trackingNumber the tracking number of the shipment to track
     * @return the tracking info for the shipment
     */
    TrackingShipment trackShipment(String trackingNumber) throws IOException;

    /**
     * Builder convenience method to eliminate the need to import OnTracBuilder directly
     * @return a new instance of a builder that can be used to build an OnTrac instance
     */
    static OnTracBuilder builder() {
        return OnTracBuilder.create();
    }
}
