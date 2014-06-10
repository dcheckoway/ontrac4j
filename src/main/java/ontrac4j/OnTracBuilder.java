package ontrac4j;

import ontrac4j.impl.Config;
import ontrac4j.impl.OnTracImpl;

public final class OnTracBuilder {
    private final Config config = new Config();

    public static OnTracBuilder create() {
        return new OnTracBuilder();
    }
        
    public OnTracBuilder account(String account) {
        config.setAccount(account);
        return this;
    }

    public OnTracBuilder password(String password) {
        config.setPassword(password);
        return this;
    }

    public OnTracBuilder rootUrl(String rootUrl) {
        config.setRootUrl(rootUrl);
        return this;
    }

    public OnTracBuilder test() {
        return rootUrl(Config.TEST_ROOT_URL);
    }

    public OnTracBuilder production() {
        return rootUrl(Config.PRODUCTION_ROOT_URL);
    }

    public OnTracBuilder connectTimeout(int connectTimeout) {
        config.setConnectTimeout(connectTimeout);
        return this;
    }
        
    public OnTracBuilder socketTimeout(int socketTimeout) {
        config.setSocketTimeout(socketTimeout);
        return this;
    }

    public OnTracBuilder maxRetries(int maxRetries) {
        config.setMaxRetries(maxRetries);
        return this;
    }
        
    public OnTracBuilder sleepBetweenRetries(long sleepBetweenRetries) {
        config.setSleepBetweenRetries(sleepBetweenRetries);
        return this;
    }
        
    public OnTrac build() {
        return new OnTracImpl(config);
    }
}
