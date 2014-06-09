package ontrac4j;

import org.junit.Test;

public class BuilderTest {
    @Test
    public void buildTestClient() {
        OnTrac ontrac = OnTrac.builder()
            .test()
            .account("37")
            .password("testpass")
            .build();
    }
    
    @Test
    public void buildProductionClient() {
        OnTrac ontrac = OnTrac.builder()
            .account("37")
            .password("testpass")
            .build();
    }
}