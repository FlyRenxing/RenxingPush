package cc.renxing.push;

import cc.renxing.push.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QqPushApplicationTests {
    @Autowired
    private AppConfig appConfig;

    @Test
    void contextLoads() {
        System.out.println(appConfig);
    }

}
