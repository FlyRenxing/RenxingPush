package top.imzdx.renxingpush.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.imzdx.renxingpush.interceptor.AdminInterceptor;
import top.imzdx.renxingpush.interceptor.LoginInterceptor;


/**
 * @author Renxing
 * @description
 * @date 2021/4/11 13:59
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(adminInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public AdminInterceptor adminInterceptor() {
        return new AdminInterceptor();
    }
}
