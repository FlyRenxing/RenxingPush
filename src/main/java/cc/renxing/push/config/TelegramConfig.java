package cc.renxing.push.config;

import cc.renxing.push.repository.UserDao;
import cc.renxing.push.service.UserService;
import cc.renxing.push.service.impl.TelegramMsgCallbackServiceImpl;
import cc.renxing.push.utils.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class TelegramConfig {
    AppConfig appConfig;

    @Autowired
    public TelegramConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Bean
    public TelegramBot telegramBot(DefaultBotOptions options, AppConfig appConfig, UserDao userDao, UserService userService, TelegramMsgCallbackServiceImpl telegramMsgCallbackService) {
        return new TelegramBot(options, appConfig, userDao, userService, telegramMsgCallbackService);
    }
    @Bean
    public DefaultBotOptions getDefaultBotOptions() {
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
        //本地调试设置代理
        System.out.println(appConfig.toString());
        if (StringUtils.hasText(appConfig.getTelegram().getProxyHost())) {
            defaultBotOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            defaultBotOptions.setProxyHost(appConfig.getTelegram().getProxyHost());
            defaultBotOptions.setProxyPort(appConfig.getTelegram().getProxyPort());
        }
        return defaultBotOptions;
    }
}
