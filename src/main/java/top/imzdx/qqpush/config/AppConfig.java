package top.imzdx.qqpush.config;

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
    private System system;
    private System.User user;
    private System.Baidu baidu;
    private System.Qq qq;
    private System.Aliyun aliyun;
    private System.Geetest geetest;

    @Data
    public static class System {
        private boolean debug;
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
    }
}
