package top.imzdx.qqpush.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import top.imzdx.qqpush.model.dto.TelegramAuthenticationRequest;
import top.imzdx.qqpush.model.po.User;

/**
 * @author Renxing
 */
public interface UserService {
    User login(String username, String password);

    User register(User user);

    String refreshCipher(String userName);

    User findUserByName(String name);

    User findUserById(Long id);

    User findUserByTelegramId(Long id);

    boolean setQQBot(User user, long number);

    int selectToDayUserUseCount(long uid);

    User qqLogin(HttpServletRequest request, HttpServletResponse response, String code);

    User bindTelegramUser(BaseAbilityBot bot, Update update, String cipher);

    User putTelegramLoginCode(String result, Long chatId);

    String getTelegramLoginCode();

    boolean hasTelegramLogin(String code);

    User telegramQRCodeLogin(String code);

    User telegramLogin(TelegramAuthenticationRequest telegramUser);
}
