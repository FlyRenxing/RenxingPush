package cc.renxing.push.service;


import cc.renxing.push.model.dto.TelegramAuthenticationRequest;
import cc.renxing.push.model.po.User;
import cc.renxing.push.utils.TelegramBot;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

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

    User bindTelegramUser(TelegramBot bot, Update update, String cipher);

    User putTelegramLoginCode(String result, Long chatId);

    String getTelegramLoginCode();

    boolean hasTelegramLogin(String code);

    User telegramQRCodeLogin(String code);

    User telegramLogin(TelegramAuthenticationRequest telegramUser);

    String regWebAuthNReq(String username, HttpSession session) throws JsonProcessingException;

    boolean regWebAuthNResp(String publicKeyCredentialJson, HttpSession session) throws IOException;

    String loginWebAuthNReq(HttpSession session) throws JsonProcessingException;

    User loginWebAuthNResp(String publicKeyCredentialJson, HttpSession session) throws IOException;
}
