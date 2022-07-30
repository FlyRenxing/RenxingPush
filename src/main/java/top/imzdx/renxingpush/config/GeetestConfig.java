package top.imzdx.renxingpush.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GeetestWeb配置文件
 */
@Component
public class GeetestConfig {

    @SuppressWarnings("FieldCanBeLocal")
    private final boolean newfailback = true;
    // 填入自己的captcha_id和private_key
    private final String geetest_id;
    private final String geetest_key;

    @Autowired
    public GeetestConfig(AppConfig appConfig) {
        this.geetest_id = appConfig.getGeetest().getId();
        this.geetest_key = appConfig.getGeetest().getKey();
    }

    public final String getGeetest_id() {
        return geetest_id;
    }

    public final String getGeetest_key() {
        return geetest_key;
    }

    public final boolean isnewfailback() {
        return newfailback;
    }

}
