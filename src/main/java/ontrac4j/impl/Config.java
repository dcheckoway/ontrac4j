package ontrac4j.impl;

public final class Config {
    public static final String TEST_ROOT_URL = "https://www.shipontrac.net/OnTracTestWebServices/OnTracServices.svc";
    public static final String PRODUCTION_ROOT_URL = "https://www.shipontrac.net/OnTracWebServices/OnTracServices.svc";

    private static final int DEFAULT_MAX_RETRIES = 5;
    private static final long DEFAULT_SLEEP_BETWEEN_RETRIES = 3000;
    
    private static final int NO_TIMEOUT = 0;

    private String account;
    private String password;
    private String rootUrl = PRODUCTION_ROOT_URL;
    private int connectTimeout = NO_TIMEOUT;
    private int socketTimeout = NO_TIMEOUT;
    private int maxRetries = DEFAULT_MAX_RETRIES;
    private long sleepBetweenRetries = DEFAULT_SLEEP_BETWEEN_RETRIES;

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRootUrl() {
        return rootUrl;
    }
    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getMaxRetries() {
        return maxRetries;
    }
    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public long getSleepBetweenRetries() {
        return sleepBetweenRetries;
    }
    public void setSleepBetweenRetries(long sleepBetweenRetries) {
        this.sleepBetweenRetries = sleepBetweenRetries;
    }
}
