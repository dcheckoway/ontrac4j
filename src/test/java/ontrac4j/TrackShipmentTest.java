package ontrac4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import ontrac4j.schema.Event;
import ontrac4j.schema.TrackingShipment;

public class TrackShipmentTest extends BaseTest {
    @Test(expected=OnTracException.class)
    public void invalidTrackingCode() throws IOException {
        ontrac().trackShipment("INVALIDTRACKINGNUMBER");
    }
    
    @Test
    public void validTrackingCode() throws IOException {
        TrackingShipment trackingShipment = ontrac().trackShipment("D10010683089895");
        assertNotNull(trackingShipment);
        assertTrue(trackingShipment.isDelivered());
        assertEquals("ADAM ZBAR", trackingShipment.getName());
        if (trackingShipment.isDelivered()) {
            System.out.println("Shipment " + trackingShipment.getTracking() + " was shipped on " + trackingShipment.getShipDate() + " and delivered to " + trackingShipment.getTracking() + " on " + trackingShipment.getExpDelDate());
        } else {
            System.out.println("Shipment " + trackingShipment.getTracking() + " was shipped on " + trackingShipment.getShipDate() + " and is expected to be delivered to " + trackingShipment.getName() + " on " + trackingShipment.getExpDelDate());
        }

        for (Event event : trackingShipment.getEvents().getEvents()) {
            System.out.println("EVENT: status=" + event.getStatus() + ", description=" + event.getDescription() + ", eventTime=" + event.getEventTime() + ", facility=" + event.getFacility() + ", city=" + event.getCity() + ", state=" + event.getState());
        }
    }
}
