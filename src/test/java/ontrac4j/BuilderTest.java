package ontrac4j;

import org.junit.Test;

public class BuilderTest {
    @Test
    public void buildTestClient() {
        OnTrac ontrac = OnTracBuilder.create()
            .test()
            .account("37")
            .password("testpass")
            .build();
    }
    
    @Test
    public void buildProductionClient() {
        OnTrac ontrac = OnTracBuilder.create()
            .account("37")
            .password("testpass")
            .build();
    }
}