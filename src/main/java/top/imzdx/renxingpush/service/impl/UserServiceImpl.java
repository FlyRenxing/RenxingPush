package top.imzdx.renxingpush.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import top.imzdx.renxingpush.config.AppConfig;
import top.imzdx.renxingpush.model.dto.TelegramAuthenticationRequest;
import top.imzdx.renxingpush.model.po.QQInfo;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.repository.QQInfoDao;
import top.imzdx.renxingpush.repository.UserDao;
import top.imzdx.renxingpush.service.UserService;
import top.imzdx.renxingpush.utils.AuthTools;
import top.imzdx.renxingpush.utils.DefinitionException;
import top.imzdx.renxingpush.utils.QQConnection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;


/**
 * @author Renxing
 */
@Service
public class UserServiceImpl implements UserService {
    public static final String TELEGRAM_LOGIN_PERFIXT = "RenxingPush-Login:";
    QQInfoDao qqInfoDao;
    UserDao userDao;
    QQConnection qqConnection;
    AuthTools authTools;
    AppConfig appConfig;

    HashMap<String, User> telegramLoginCodeMap = new HashMap<>();

    @Autowired
    public UserServiceImpl(QQInfoDao qqInfoDao, UserDao userDao, AuthTools authTools, QQConnection qqConnection,
                           AppConfig appConfig) {
        this.qqInfoDao = qqInfoDao;
        this.userDao = userDao;
        this.authTools = authTools;
        this.qqConnection = qqConnection;
        this.appConfig = appConfig;
    }

    @Override
    public User login(String username, String password) {
        User user = userDao.findByName(username).orElseThrow(() -> new DefinitionException("用户不存在"));
        if (!user.getPassword().equals(password)) {
            throw new DefinitionException("密码错误");
        }
        AuthTools.login(user.getUid());
        return user;

    }

    @Override
    public User register(User user) {
        user.setCipher(authTools.generateCipher())
                .setDayMaxSendCount((long) appConfig.getUser().getDefaultSetting().getDayMaxSendCount())
                .setConfig(new User.UserConfig(
                        qqInfoDao.findFirstBy().orElse(new QQInfo().setNumber(0L)).getNumber())
                );
        return userDao.save(user);
    }

    @Override
    public String refreshCipher(String userName) {
        String newCipher = authTools.generateCipher();
        User user = userDao.findByName(userName).orElseThrow(() -> new DefinitionException("用户不存在"));
        user.setCipher(newCipher);
        userDao.save(user);
        return newCipher;
    }

    @Override
    public User findUserByName(String name) {
        return userDao.findByName(name).orElseThrow(() -> new DefinitionException("用户不存在"));
    }

    @Override
    public User findUserById(Long id) {
        return userDao.findById(id).orElseThrow(() -> new DefinitionException("用户不存在"));
    }

    @Override
    public User findUserByTelegramId(Long id) {
        return userDao.findByTelegramId(id).orElseThrow(() -> new DefinitionException("用户不存在"));
    }

    @Override
    public boolean setQQBot(User user, long number) {
        QQInfo qqInfo = qqInfoDao.findByNumber(number).orElseThrow(() -> new DefinitionException("所选机器人不在服务列表内，请更换机器人"));
        user.setConfig(new User.UserConfig(number));
        userDao.save(user);
        return true;
    }

    @Override
    public int selectToDayUserUseCount(long uid) {
        return userDao.getTodayUseCount(uid);
    }

    @Override
    public User qqLogin(HttpServletRequest request, HttpServletResponse response, String code) {
        //        第三步 获取access token
        String accessToken = qqConnection.getAccessToken(code);
//        第四步 获取登陆后返回的 openid、appid 以JSON对象形式返回
        ObjectNode userInfo = qqConnection.getUserOpenID(accessToken);
//        第五步获取用户有效(昵称、头像等）信息  以JSON对象形式返回
        String oauth_consumer_key = userInfo.get("client_id").asText();
        String openid = userInfo.get("openid").asText();
        ObjectNode userRealInfo = qqConnection.getUserInfo(accessToken, oauth_consumer_key, openid);

        Optional<User> userOptional = userDao.findByOpenid(openid);
        User user = null;
        try {
            user = userOptional.orElseThrow(() -> new DefinitionException("用户不存在"));
        } catch (DefinitionException e) {
            String userName = userRealInfo.get("nickname").asText();
            String randomString = AuthTools.generateRandomString(6);
            do {
                userName = userName + "_" + randomString;
            } while (userDao.findByName(userName).isPresent());
            user = new User()
                    .setName(userName)
                    .setPassword(randomString)
                    .setOpenid(openid);
            try {
                user = register(user);
            } catch (DefinitionException e1) {
                throw new DefinitionException("注册失败");
            }
        }
        assert user != null;
        AuthTools.login(user.getUid());
        try {
            response.sendRedirect(appConfig.getQq().getBackUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User bindTelegramUser(BaseAbilityBot bot, Update update, String cipher) {
        Long chatId = update.getMessage().getChatId();
        if (userDao.findByTelegramId(chatId).isPresent()) {
            bot.silent().send("此telegram账户已与其他账户绑定，请前往官网进行解绑", chatId);
            throw new DefinitionException("此telegram账户已与其他账户绑定，请前往官网进行解绑");
        }
        Optional<User> optionalUser = userDao.findByCipher(cipher);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setTelegramId(chatId);
            userDao.save(user);
            bot.silent().send("绑定成功", chatId);
            return user;
        } else {
            bot.silent().send("Cipher不正确", chatId);
            throw new DefinitionException("Cipher不正确");
        }
    }

    @Override
    public User putTelegramLoginCode(String code, Long chatId) throws DefinitionException {
        User user = userDao.findByTelegramId(chatId).orElseThrow(() -> new DefinitionException("用户不存在,请先在控制台绑定tg账户。若还未注册，请先注册"));
        telegramLoginCodeMap.put(code, user);
        return user;
    }

    @Override
    public String getTelegramLoginCode() {
        String code;
        do {
            code = AuthTools.generateRandomString(6);
        } while (telegramLoginCodeMap.get(code) != null);
        return code;
    }

    @Override
    public boolean hasTelegramLogin(String code) {
        return telegramLoginCodeMap.get(code) != null;
    }

    @Override
    public User telegramQRCodeLogin(String code) {
        User user = telegramLoginCodeMap.get(code);
        if (user == null) {
            throw new DefinitionException("二维码/代码已过期，请重新扫描");
        }
        AuthTools.login(user.getUid());
        return user;
    }

    @Override
    public User telegramLogin(TelegramAuthenticationRequest telegramUser) {
        User user;
        try {
            user = userDao.findByTelegramId(telegramUser.getId()).orElseThrow(() -> new DefinitionException("用户不存在"));
        } catch (DefinitionException e) {
            String userName = telegramUser.getFirst_name();
            String randomString = AuthTools.generateRandomString(6);
            do {
                userName = userName + "_" + randomString;
            } while (userDao.findByName(userName).isPresent());
            user = new User()
                    .setName(userName)
                    .setPassword(randomString)
                    .setTelegramId(telegramUser.getId());
            try {
                user = register(user);
            } catch (DefinitionException e1) {
                throw new DefinitionException("注册失败");
            }
            return user;
        }
        return user;
    }


}
