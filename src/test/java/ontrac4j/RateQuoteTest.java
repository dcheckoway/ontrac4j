package ontrac4j;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import ontrac4j.schema.Dim;
import ontrac4j.schema.RateQuote;
import ontrac4j.schema.RateShipment;

public class RateQuoteTest extends BaseTest {
    @Test
    public void rateQuote_allServices() throws IOException {
        RateShipment rateShipment = new RateShipment();
        rateShipment.setUID(UUID.randomUUID().toString());
        rateShipment.setDelzip("91709");
        rateShipment.setPUZip("94123");
        rateShipment.setDeclared(123.45);
        rateShipment.setResidential(true);
        rateShipment.setCOD(0);
        rateShipment.setSaturdayDel(false);
        rateShipment.setWeight(4.5);
        rateShipment.setDIM(new Dim());
        rateShipment.getDIM().setLength(18);
        rateShipment.getDIM().setWidth(14);
        rateShipment.getDIM().setHeight(6);
        Map<String,RateQuote> rateQuoteMap = ontrac().getRateQuote(rateShipment);
        assertNotNull(rateQuoteMap);
        assertFalse(rateQuoteMap.isEmpty());
        for (Map.Entry<String,RateQuote> entry : rateQuoteMap.entrySet()) {
            RateQuote rateQuote = entry.getValue();
            System.out.println("For Service \"" + entry.getKey() + "\", serviceCharge=" + rateQuote.getServiceCharge() + ", fuelCharge=" + rateQuote.getFuelCharge() + ", totalCharge=" + rateQuote.getTotalCharge() + ", transitDays=" + rateQuote.getTransitDays() + ", globalRate=" + rateQuote.getGlobalRate());
        }
    }
    
    @Test
    public void rateQuote_singleService() throws IOException {
    }
}
