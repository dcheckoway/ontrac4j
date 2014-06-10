package ontrac4j;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import ontrac4j.schema.PickupRequest;

public class PickupTest extends BaseTest {
    @Test
    public void schedulePickup() throws Exception {
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        
        GregorianCalendar date = new GregorianCalendar();
        date.setTime(DateUtils.addDays(new Date(), 1));
        
        GregorianCalendar readyAt = new GregorianCalendar();
        readyAt.setTime(new Date());
        
        GregorianCalendar closeAt = new GregorianCalendar();
        closeAt.setTime(DateUtils.addHours(date.getTime(), 4));
        
        PickupRequest pickupRequest = new PickupRequest();
        pickupRequest.setDate(datatypeFactory.newXMLGregorianCalendar(date));
        pickupRequest.setReadyAt(datatypeFactory.newXMLGregorianCalendar(readyAt));
        pickupRequest.setCloseAt(datatypeFactory.newXMLGregorianCalendar(closeAt));
        pickupRequest.setName("My Company");
        pickupRequest.setAddress("123 Main St.");
        pickupRequest.setCity("Los Angeles");
        pickupRequest.setState("CA");
        pickupRequest.setZip("90069");
        pickupRequest.setDelZip("94111");
        pickupRequest.setPhone("310-555-1212");
        pickupRequest.setContact("Joe Schmoe");
        String trackingNumber = ontrac().schedulePickup(pickupRequest);
        assertNotNull(trackingNumber);
        System.out.println("Scheduled pickup tracking #: " + trackingNumber);
    }
}
