package ontrac4j;

public abstract class BaseTest {
    protected static OnTrac ontrac() {
        return OnTrac.builder()
            .test()
            .account("37")
            .password("testpass")
            .build();
    }
}