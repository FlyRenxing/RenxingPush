package top.imzdx.qqpush.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * GeetestWeb配置文件
 */
@Component
public class GeetestConfig {

	private final boolean newfailback = true;
	// 填入自己的captcha_id和private_key
	@Value("${geetest.id}")
	private String geetest_id;
	@Value("${geetest.key}")
	private String geetest_key;

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
