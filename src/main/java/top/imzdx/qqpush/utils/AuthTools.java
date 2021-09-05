package top.imzdx.qqpush.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author Renxing
 */
@Component
public class AuthTools {
    public static String generateCipher() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
