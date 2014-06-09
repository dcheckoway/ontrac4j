package ontrac4j;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

public class ZipCodesTest extends BaseTest {
    @Test
    public void getZipCodes() throws IOException {
        Map<String,ZipCode> zipCodes = ontrac().getZipCodes();
        System.out.println("# of ZipCodes: " + zipCodes.size());
        ZipCode sfzip = zipCodes.get("94123");
        assertNotNull(sfzip);
        assertTrue(sfzip.isPickupServiced());
    }
    
    @Test
    public void getZipCodes_withLastUpdate() throws IOException {
        Date lastUpdate = DateUtils.addDays(new Date(), -730);
        Map<String,ZipCode> zipCodes = ontrac().getZipCodes(lastUpdate);
        System.out.println("# of ZipCodes updated: " + zipCodes.size());
        assertFalse(zipCodes.isEmpty());
    }
}