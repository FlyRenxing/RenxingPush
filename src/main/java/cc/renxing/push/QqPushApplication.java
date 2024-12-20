package cc.renxing.push;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableSpringUtil
public class QqPushApplication {

    public static void main(String[] args) throws ClassNotFoundException {

        SpringApplication.run(QqPushApplication.class, args);
    }


}
