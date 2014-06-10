package ontrac4j;

import java.io.IOException;

/**
 * Exception thrown when the OnTrac service returns a specific error message
 */
public class OnTracException extends IOException {
    public OnTracException(String msg) {
        super(msg);
    }
}
