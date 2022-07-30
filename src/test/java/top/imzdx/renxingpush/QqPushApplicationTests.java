package top.imzdx.renxingpush;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.imzdx.renxingpush.config.AppConfig;

@SpringBootTest
class QqPushApplicationTests {
    @Autowired
    private AppConfig appConfig;
    @Test
    void contextLoads() {
        System.out.println(appConfig);
    }

}
