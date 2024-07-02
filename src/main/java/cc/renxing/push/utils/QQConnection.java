package cc.renxing.push.utils;

import cc.renxing.push.config.AppConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Renxing
 */

@Component
public class QQConnection {
    String QQAppID;
    String QQAppKEY;
    String redirect_uri;

    public QQConnection(AppConfig appConfig) {
        this.QQAppID = appConfig.getQq().getAppID();
        this.QQAppKEY = appConfig.getQq().getAppKey();
        this.redirect_uri = appConfig.getQq().getRedirectUri();
    }
//    第一步：获取 QQ登录按钮url 几乎等于手动拼接 无太大意义

    public String getUrl() {
        return "https://graph.qq.com/oauth2.0/authorize?display=pc&response_type=code&client_id=" + QQAppID + "&redirect_uri=" + redirect_uri;
    }

    /*
    第三步：根据前一步得到的 Authorization Code 获取access token
     */
    public String getAccessToken(String code) {
        String accessTaken = "";
        String url = "https://graph.qq.com/oauth2.0/token?display=pc&grant_type=authorization_code&client_id=" + QQAppID + "&client_secret=" + QQAppKEY + "&redirect_uri=" + redirect_uri + "&code=" + code;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String responseString = response.body().string();
            accessTaken = responseString.split("=")[1].split("&")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessTaken;
    }

    /*
    第四步：根据得到的access token 获取用户的 openID 和 oauth_consumer_key：申请QQ登录成功后，分配给应用的openid(对当前网站应用唯一，可用于检测是否为同一用户的凭证）
     */
    public ObjectNode getUserOpenID(String accessToken) {
        String urlProvideByQQConnection = "https://graph.qq.com/oauth2.0/me";
        String requestUrl = urlProvideByQQConnection + "?access_token=" + accessToken;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();
        ObjectNode userInfo = null;
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String UserInfoString = response.body().string().split(" ")[1];
            userInfo = new ObjectMapper().readValue(UserInfoString, ObjectNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    /*
    第五步：根据 access_token、oauth_consumer_key(上一步返回的client_id）、openid 获取用户有效信息 (昵称、头像等） 返回json对象
     */
    public ObjectNode getUserInfo(String access_token, String oauth_consumer_key, String openid) {
        ObjectNode userRealInfo = null;
        String urlProvideByQQConnection = "https://graph.qq.com/user/get_user_info?";
        String requestUrl = urlProvideByQQConnection + "access_token=" + access_token + "&oauth_consumer_key=" + oauth_consumer_key + "&openid=" + openid;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(requestUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            String UserRealInfoString = response.body().string();
            userRealInfo = new ObjectMapper().readValue(UserRealInfoString, ObjectNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userRealInfo;
    }
}
