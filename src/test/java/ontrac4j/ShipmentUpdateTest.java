package ontrac4j;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import ontrac4j.schema.ShipmentUpdate;

public class ShipmentUpdateTest extends BaseTest {
    @Test(expected=IOException.class)
    public void invalidTrackingCode() throws IOException {
        ontrac().getShipmentUpdate("INVALIDTRACKINGNUMBER");
    }
    
    @Test
    public void validTrackingCode() throws IOException {
        ShipmentUpdate shipmentUpdate = ontrac().getShipmentUpdate("D10010683089895");
        assertNotNull(shipmentUpdate);
        assertTrue(shipmentUpdate.isDelivered());
        System.out.println("Total Charge: " + shipmentUpdate.getTotalChrg());
    }
}
