package top.imzdx.qqpush.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.qqpush.config.AppConfig;
import top.imzdx.qqpush.model.po.QQInfo;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.QQInfoDao;
import top.imzdx.qqpush.repository.UserDao;
import top.imzdx.qqpush.service.UserService;
import top.imzdx.qqpush.utils.AuthTools;
import top.imzdx.qqpush.utils.DefinitionException;
import top.imzdx.qqpush.utils.QQConnection;

import java.io.IOException;
import java.util.Optional;


/**
 * @author Renxing
 */
@Service
public class UserServiceImpl implements UserService {
    QQInfoDao qqInfoDao;
    UserDao userDao;
    QQConnection qqConnection;
    AuthTools authTools;
    AppConfig appConfig;
    int digit;

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
    public User register(String name, String password) {
        if (userDao.findByName(name).isPresent()) {
            throw new DefinitionException("该用户名已被注册，请更换后重试");
        }
        User user = new User()
                .setName(name)
                .setPassword(password)
                .setCipher(authTools.generateCipher())
                .setDayMaxSendCount((long) appConfig.getUser().getDefaultSetting().getDayMaxSendCount())
                .setConfig(new User.UserConfig(
                        qqInfoDao.findFirstBy().orElse(new QQInfo().setNumber(0L)).getNumber())
                );
        return userDao.save(user);
    }

    @Override
    public boolean register(String name, String password, String openid) {
        User user = new User()
                .setName(name)
                .setPassword(password)
                .setCipher(authTools.generateCipher())
                .setDayMaxSendCount((long) appConfig.getUser().getDefaultSetting().getDayMaxSendCount())
                .setOpenid(openid)
                .setConfig(new User.UserConfig(
                        qqInfoDao.findFirstBy().orElse(new QQInfo().setNumber(0L)).getNumber())
                );
        userDao.save(user);
        return true;
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

            if (register(userName, randomString, openid)) {
                user = userDao.findByName(userName).orElseThrow(() -> new DefinitionException("注册异常"));
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

    public User bindTelegramUser(Long chatId, String cipher) {
        if (userDao.findByTelegramId(chatId) != null) {
            throw new DefinitionException("此telegram账户已与其他账户绑定，请前往官网进行解绑");
        }
        User user = userDao.findByCipher(cipher).orElseThrow(() -> new DefinitionException("Cipher不正确"));
        user.setTelegramId(chatId);
        return userDao.save(user);
    }

}
