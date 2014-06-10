package ontrac4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.datatype.DatatypeFactory;

import org.junit.Test;

import ontrac4j.xml.CodType;
import ontrac4j.xml.Consignee;
import ontrac4j.xml.Dim;
import ontrac4j.xml.ShipmentRequest;
import ontrac4j.xml.ShipmentResponse;
import ontrac4j.xml.Shipper;

public class CreateShipmentTest extends BaseTest {
    @Test
    public void createShipment() throws Exception {
        String uid = UUID.randomUUID().toString();

        Shipper shipper = new Shipper();
        shipper.setName("ontrac4j");
        shipper.setAddr1("1454 The Embarcadero");
        shipper.setCity("San Francisco");
        shipper.setState("CA");
        shipper.setZip("94123");
        shipper.setContact("Java Developer");
        shipper.setPhone("415-555-1212");

        String reference = UUID.randomUUID().toString();

        Consignee consignee = new Consignee();
        consignee.setName("Staples Center");
        consignee.setAddr1("1111 S Figueroa St.");
        consignee.setAddr2("");
        consignee.setAddr3("");
        consignee.setCity("Los Angeles");
        consignee.setState("CA");
        consignee.setZip("90015");
        consignee.setContact("Robert Paulson");
        consignee.setPhone("213-555-1212");

        Dim dim = new Dim();
        dim.setLength(22.0);
        dim.setWidth(18.0);
        dim.setHeight(12.0);

        GregorianCalendar shipDate = new GregorianCalendar();
        shipDate.setTime(new Date());
        
        ShipmentRequest shipmentRequest = new ShipmentRequest();
        shipmentRequest.setUID(uid);
        shipmentRequest.setShipper(shipper);
        shipmentRequest.setConsignee(consignee);
        shipmentRequest.setService("S");
        shipmentRequest.setSignatureRequired(true);
        shipmentRequest.setResidential(false);
        shipmentRequest.setSaturdayDel(false);
        shipmentRequest.setDeclared(123.45);
        shipmentRequest.setCOD(0);
        shipmentRequest.setCODType(CodType.NONE);
        shipmentRequest.setWeight(15.0);
        shipmentRequest.setLetter(0);
        shipmentRequest.setBillTo(0);
        shipmentRequest.setInstructions("Leave at front desk");
        shipmentRequest.setReference(reference);
        shipmentRequest.setReference2("");
        shipmentRequest.setReference3("");
        shipmentRequest.setTracking("");
        shipmentRequest.setDIM(dim);
        shipmentRequest.setLabelType(0);
        shipmentRequest.setShipEmail("dcheckoway@gmail.com;dan@sunbasket.com");
        shipmentRequest.setDelEmail("dcheckoway@gmail.com;dan@sunbasket.com");
        shipmentRequest.setShipDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(shipDate));

        ShipmentResponse shipmentResponse = ontrac().createShipment(shipmentRequest);
        assertNotNull(shipmentResponse);
        assertNotNull(shipmentResponse.getTracking());
        assertEquals(uid, shipmentResponse.getUID());
        System.out.println("Created tracking #: " + shipmentResponse.getTracking());
    }
}