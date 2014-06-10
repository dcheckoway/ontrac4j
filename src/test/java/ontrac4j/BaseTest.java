package ontrac4j;

public abstract class BaseTest {
    private static final String ACCOUNT = System.getProperty("ontrac4j.account", "37");
    private static final String PASSWORD = System.getProperty("ontrac4j.password", "testpass");
    
    protected static OnTrac ontrac() {
        return OnTracBuilder.create()
            .test()
            .account(ACCOUNT)
            .password(PASSWORD)
            .build();
    }
}