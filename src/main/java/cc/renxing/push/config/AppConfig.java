package cc.renxing.push.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "app")
@Component
public class AppConfig {
    private AppConfig.System system;
    private AppConfig.User user;
    private AppConfig.Baidu baidu;
    private AppConfig.Qq qq;
    private AppConfig.Aliyun aliyun;
    private AppConfig.Geetest geetest;
    private AppConfig.Telegram telegram;

    @Data
    public static class System {
        private String name;
        private String domain;
        private boolean openQqgroupWhitelistApply;
        private boolean debug;
        private boolean openQqMsg;
        private boolean openTelegramMsg;
        private Check check;

        @Data
        public static class Check {
            private Text text;
            private Image image;

            @Data
            public static class Text {
                private boolean enabled;
                private String useType;
            }

            @Data
            public static class Image {
                private boolean enabled;
                private String useType;
                private String privateCloudUrl;
            }
        }
    }

    @Data
    public static class User {
        private Default defaultSetting;

        @Data
        public static class Default {
            private int cipherDigit;
            private int dayMaxSendCount;
        }
    }

    @Data
    public static class Baidu {
        private String appID;
        private String aPIKey;
        private String secretKey;
    }

    @Data
    public static class Qq {
        private String appID;
        private String appKey;
        private String backUrl;
        private String redirectUri;
    }

    @Data
    public static class Aliyun {
        private String accessKeyId;
        private String accessKeySecret;
    }

    @Data
    public static class Geetest {
        private String id;
        private String key;
        private boolean enabled;
    }

    @Data
    public static class Telegram {
        private String botToken;
        private String botName;
        private long creatorId;

        private String proxyHost;
        private int proxyPort;

    }
}
