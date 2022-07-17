package top.imzdx.qqpush.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class TelegramConfig {
    AppConfig appConfig;

    @Autowired
    public TelegramConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Bean
    public DefaultBotOptions getDefaultBotOptions() {
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
        //本地调试设置代理
        if (appConfig.getSystem().isDebug()) {
            defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            defaultBotOptions.setProxyHost("localhost");
            defaultBotOptions.setProxyPort(7890);
        }
        return defaultBotOptions;
    }
}
