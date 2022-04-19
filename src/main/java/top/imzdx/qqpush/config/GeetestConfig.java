package top.imzdx.qqpush.config;

import org.springframework.beans.factory.annotation.Value;
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

    public GeetestConfig(@Value("${geetest.id}") String geetest_id, @Value("${geetest.key}") String geetest_key) {
        this.geetest_id = geetest_id;
        this.geetest_key = geetest_key;
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
