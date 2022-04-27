//package top.imzdx.qqpush.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//
///**
// * @author Renxing
// */
//@Configuration
//public class SwaggerConfig {
//    @Bean
//    public Docket desertsApi() {
//        return new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("top.imzdx.qqpush.controller"))
//                .paths(PathSelectors.any())
//                .build()
//                .groupName("任性推")
//                .enable(true);
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("任性推服务端文档")
//                .description("这里是任性推服务的API文档")
//                .contact(new Contact("会飞的任性", "https://imzdx.top", "flyrenxing@qq.com"))
//                .version("1.0")
//                .build();
//    }
//}
//
