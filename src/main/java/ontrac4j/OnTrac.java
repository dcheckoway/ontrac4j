package ontrac4j;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import ontrac4j.impl.OnTracImpl;

public interface OnTrac {
    List<ZipCode> getZipCodes() throws IOException;
    List<ZipCode> getZipCodes(Date lastUpdate) throws IOException;

    static interface Builder {
        Builder account(String account);
        Builder password(String password);
        Builder rootUrl(String rootUrl);
        Builder test();
        Builder production();
        OnTrac build();
    }

    static Builder builder() {
        return new OnTracImpl.Builder();
    }
}