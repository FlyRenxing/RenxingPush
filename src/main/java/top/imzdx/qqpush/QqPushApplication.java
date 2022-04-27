package top.imzdx.qqpush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
/**
 * 目前springfox不支持springboot3，故暂时禁用swagger
 */
//@EnableOpenApi
public class QqPushApplication {

    public static void main(String[] args) {
        SpringApplication.run(QqPushApplication.class, args);
    }


}
