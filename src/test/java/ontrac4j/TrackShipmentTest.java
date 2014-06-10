package ontrac4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import ontrac4j.xml.Event;
import ontrac4j.xml.TrackingShipment;

public class TrackShipmentTest extends BaseTest {
    @Test(expected=IOException.class)
    public void invalidTrackingCode() throws IOException {
        ontrac().trackShipment("INVALIDTRACKINGNUMBER");
    }
    
    @Test
    public void validTrackingCode() throws IOException {
        TrackingShipment trackingShipment = ontrac().trackShipment("D10010683089895");
        assertNotNull(trackingShipment);
        assertTrue(trackingShipment.isDelivered());
        assertEquals("ADAM ZBAR", trackingShipment.getName());

        for (Event event : trackingShipment.getEvents().getEvents()) {
            System.out.println("EVENT: status=" + event.getStatus() + ", description=" + event.getDescription() + ", eventTime=" + event.getEventTime() + ", facility=" + event.getFacility() + ", city=" + event.getCity() + ", state=" + event.getState());
        }
    }
}