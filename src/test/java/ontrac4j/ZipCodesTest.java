package ontrac4j;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

public class ZipCodesTest extends BaseTest {
    @Test
    public void getZipCodes() throws IOException {
        List<ZipCode> zipCodes = ontrac().getZipCodes();
        System.out.println("# of ZipCodes: " + zipCodes.size());
        assertFalse(zipCodes.isEmpty());
    }
    
    @Test
    public void getZipCodes_withLastUpdate() throws IOException {
        Date lastUpdate = DateUtils.addDays(new Date(), -730);
        List<ZipCode> zipCodes = ontrac().getZipCodes(lastUpdate);
        System.out.println("# of ZipCodes updated: " + zipCodes.size());
        assertFalse(zipCodes.isEmpty());
    }
}