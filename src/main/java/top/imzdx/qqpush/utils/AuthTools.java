package top.imzdx.qqpush.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.HexUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.imzdx.qqpush.config.AppConfig;
import top.imzdx.qqpush.model.dto.TelegramAuthenticationRequest;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.UserDao;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Renxing
 */
@Component
public class AuthTools {
    static UserDao userDao;
    static AppConfig appConfig;


    @Autowired
    public AuthTools(UserDao userDao, AppConfig appConfig) {
        AuthTools.userDao = userDao;
        AuthTools.appConfig = appConfig;
    }

    public static String generateRandomString(int digit) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digit; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getIpAddr(HttpServletRequest request) {
        final String UNKNOWN = "unknown";
        String ipAddress;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if ("127.0.0.1".equals(ipAddress)) {
                    // 根据网卡取本机配置的IP
                    try {
                        ipAddress = InetAddress.getLocalHost().getHostAddress();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
            // 通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null) {
                if (ipAddress.contains(",")) {
                    return ipAddress.split(",")[0];
                } else {
                    return ipAddress;
                }
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void login(long uid) {
        getHttpServletRequest().getSession().setAttribute("uid", uid);
    }

    public static User getUser() {
        Long uid = (Long) getHttpServletRequest().getSession().getAttribute("uid");
        if (uid == null) {
            throw new DefinitionException("当前未登录");
        }
        return userDao.findById(uid).orElseThrow((() -> new DefinitionException("账户不存在")));
    }

    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public String generateCipher() {
        int digit = appConfig.getUser().getDefaultSetting().getCipherDigit();
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digit; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        if (userDao.findByCipher(sb.toString()).isPresent()) {
            return generateCipher();
        }
        return sb.toString();
    }

    public static boolean verifyAuth(TelegramAuthenticationRequest user) {
        Map<String, Object> request = BeanUtil.beanToMap(user);
        String hash = (String) request.get("hash");
        request.remove("hash");

        // Prepare the string
        String str = request.entrySet().stream()
                .sorted((a, b) -> a.getKey().compareToIgnoreCase(b.getKey()))
                .filter(entry -> entry.getValue() != null && !entry.getValue().equals(""))
                .map(kvp -> kvp.getKey() + "=" + kvp.getValue())
                .collect(Collectors.joining("\n"));

        try {
            SecretKeySpec sk = new SecretKeySpec(
                    // Get SHA 256 from telegram token
                    MessageDigest.getInstance("SHA-256").digest(appConfig.getTelegram().getBotToken().getBytes(StandardCharsets.UTF_8)
                    ), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(sk);

            byte[] result = mac.doFinal(str.getBytes(StandardCharsets.UTF_8));

            // Convert the result to hex string
            // Like https://stackoverflow.com/questions/9655181
            String resultStr = HexUtil.encodeHexStr(result);

            // Compare the result with the hash from body
            if (hash.compareToIgnoreCase(resultStr) == 0) {
                // Do other things like create a user and JWT token
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }
}
