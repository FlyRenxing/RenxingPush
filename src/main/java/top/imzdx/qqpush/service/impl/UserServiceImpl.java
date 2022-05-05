package top.imzdx.qqpush.service.impl;

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


/**
 * @author Renxing
 */
@Service
public class UserServiceImpl implements UserService {
    QQInfoDao qqInfoDao;
    UserDao userDao;
    AuthTools authTools;
    long dayMaxSendCount;
    int digit;

    @Autowired
    public UserServiceImpl(QQInfoDao qqInfoDao, UserDao userDao, AuthTools authTools,
                           AppConfig appConfig) {
        this.qqInfoDao = qqInfoDao;
        this.userDao = userDao;
        this.authTools = authTools;
        this.dayMaxSendCount = appConfig.getUser().getDefaultSetting().getDayMaxSendCount();
        this.digit = appConfig.getUser().getDefaultSetting().getCipherDigit();
    }

    @Override
    public boolean register(String name, String password) {
        User user = new User()
                .setName(name)
                .setPassword(password)
                .setCipher(authTools.generateCipher(digit))
                .setDayMaxSendCount(dayMaxSendCount)
                .setConfig(new User.UserConfig(
                        qqInfoDao.findFirstBy().orElse(new QQInfo().setNumber(0L)).getNumber())
                );
        userDao.save(user);
        return true;
    }

    @Override
    public boolean register(String name, String password, String openid) {
        User user = new User()
                .setName(name)
                .setPassword(password)
                .setCipher(authTools.generateCipher(digit))
                .setDayMaxSendCount(dayMaxSendCount)
                .setOpenid(openid)
                .setConfig(new User.UserConfig(
                        qqInfoDao.findFirstBy().orElse(new QQInfo().setNumber(0L)).getNumber())
                );
        userDao.save(user);
        return true;
    }

    @Override
    public String refreshCipher(String userName) {
        String newCipher = authTools.generateCipher(digit);
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
    public User findUserByOpenid(String openid) {
        return userDao.findByOpenid(openid).orElseThrow(() -> new DefinitionException("用户不存在"));
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


}
