package ontrac4j;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import ontrac4j.impl.OnTracImpl;
import ontrac4j.xml.ZipCode;

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
     * Builder interface used to build OnTrac instances
     */
    static interface Builder {
        Builder account(String account);
        Builder password(String password);
        Builder rootUrl(String rootUrl);
        Builder test();
        Builder production();
        OnTrac build();
    }

    /**
     * @return a builder instance that can be used to build an OnTrac instance
     */
    static Builder builder() {
        return new OnTracImpl.Builder();
    }
}