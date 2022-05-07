package top.imzdx.qqpush.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.imzdx.qqpush.config.AppConfig;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.UserDao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Random;

/**
 * @author Renxing
 */
@Component
public class AuthTools {
    static UserDao userDao;
    AppConfig appConfig;


    @Autowired
    public AuthTools(UserDao userDao, AppConfig appConfig) {
        AuthTools.userDao = userDao;
        this.appConfig = appConfig;
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
}
