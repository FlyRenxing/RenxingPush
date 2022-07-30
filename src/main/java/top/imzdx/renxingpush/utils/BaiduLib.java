package top.imzdx.renxingpush.utils;

import com.baidu.aip.contentcensor.AipContentCensor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.imzdx.renxingpush.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

@Component

public class BaiduLib {
    private final AipContentCensor client;

    @Autowired
    public BaiduLib(AppConfig appConfig) {
        client = new AipContentCensor(appConfig.getBaidu().getAppID(), appConfig.getBaidu().getAPIKey(), appConfig.getBaidu().getSecretKey());
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }

    public void textCensorUserDefined(String text) throws DefinitionException {
        JSONObject jsonObject = client.textCensorUserDefined(text);
        try {
            if (jsonObject.getInt("conclusionType") != 1) {
                List<String> msgs = new ArrayList<>();
                JSONArray datas = jsonObject.getJSONArray("data");
                for (int i = 0; i < datas.length(); i++) {
                    msgs.add(datas.getJSONObject(i).getString("msg"));
                }
                throw new DefinitionException("消息有敏感词，请检查后再试。提示：" + msgs);
            }
        } catch (JSONException e) {
            throw new DefinitionException("百度文本审核服务异常");
        }
    }
}
