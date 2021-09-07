package top.imzdx.qqpush.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.imzdx.qqpush.dao.UserDao;

import java.util.Random;

/**
 * @author Renxing
 */
@Component
public class AuthTools {
    @Autowired
    UserDao userDao;

    public String generateCipher() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        if (userDao.findUserByCipher(sb.toString()) != null) {
            return generateCipher();
        }
        return sb.toString();
    }
}
