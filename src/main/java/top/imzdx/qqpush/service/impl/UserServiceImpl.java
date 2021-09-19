package top.imzdx.qqpush.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.imzdx.qqpush.dao.QqInfoDao;
import top.imzdx.qqpush.dao.UserDao;
import top.imzdx.qqpush.model.po.QqInfo;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.service.UserService;
import top.imzdx.qqpush.utils.AuthTools;
import top.imzdx.qqpush.utils.DefinitionException;


/**
 * @author Renxing
 */
@Service
public class UserServiceImpl implements UserService {
    QqInfoDao qqInfoDao;
    UserDao userDao;
    AuthTools authTools;
    long dayMaxSendCount;
    int digit;

    @Autowired
    public UserServiceImpl(QqInfoDao qqInfoDao, UserDao userDao, AuthTools authTools,
                           @Value("${app.user.default.day-max-send-count}") long dayMaxSendCount,
                           @Value("${app.user.default.cipher-digit}") int digit) {
        this.qqInfoDao = qqInfoDao;
        this.userDao = userDao;
        this.authTools = authTools;
        this.dayMaxSendCount = dayMaxSendCount;
        this.digit = digit;
    }

    @Override
    public boolean register(String name, String password) {
        User user = new User()
                .setName(name)
                .setPassword(password)
                .setCipher(authTools.generateCipher(digit))
                .setDayMaxSendCount(dayMaxSendCount)
                .setConfig(new JSONObject() {{
                    put("qq_bot", qqInfoDao.getFirst().getNumber());
                }}.toJSONString());
        int i = userDao.insertUser(user);
        if (i == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean register(String name, String password, String openid) {
        User user = new User()
                .setName(name)
                .setPassword(password)
                .setCipher(authTools.generateCipher(digit))
                .setDayMaxSendCount(dayMaxSendCount)
                .setOpenid(openid)
                .setConfig(new JSONObject() {{
                    put("qq_bot", qqInfoDao.getFirst().getNumber());
                }}.toJSONString());
        int i = userDao.insertUser(user);
        if (i == 1) {
            return true;
        }
        return false;
    }

    @Override
    public String refreshCipher(String userName) {
        String newCipher = authTools.generateCipher(digit);
        User user = userDao.findUserByName(userName);
        user.setCipher(newCipher);
        if (userDao.updateUser(user) == 1) {
            return newCipher;
        }
        throw new DefinitionException("没有成功更新，请退出登录后重试");
    }

    @Override
    public User findUserByName(String name) {
        return userDao.findUserByName(name);
    }

    @Override
    public User findUserByOpenid(String openid) {
        return userDao.findUserByOpenid(openid);
    }

    @Override
    public boolean setQQBot(long uid, long number) {
        QqInfo qqInfo = qqInfoDao.findInfoByNumber(number);
        if (qqInfo == null) {
            throw new DefinitionException("所选机器人不在服务列表内，请更换机器人");
        }
        User user = userDao.findUserByUid(uid);
        JSONObject config = JSONObject.parseObject(user.getConfig());
        config.put("qq_bot", number);
        user.setConfig(config.toJSONString());
        userDao.updateUser(user);
        return true;
    }

    @Override
    public int selectToDayUserUseCount(long uid) {
        return userDao.selectToDayUserUseCount(uid);
    }


}
