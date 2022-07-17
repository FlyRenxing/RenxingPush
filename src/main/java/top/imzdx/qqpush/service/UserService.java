package top.imzdx.qqpush.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import top.imzdx.qqpush.model.po.User;

/**
 * @author Renxing
 */
public interface UserService {
    User login(String username, String password);

    User register(String username, String password);

    boolean register(String username, String password, String openid);

    String refreshCipher(String userName);

    User findUserByName(String name);

    User findUserById(Long id);

    boolean setQQBot(User user, long number);

    int selectToDayUserUseCount(long uid);

    User qqLogin(HttpServletRequest request, HttpServletResponse response, String code);

    User bindTelegramUser(Long chatId, String cipher);
}
