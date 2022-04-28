package top.imzdx.qqpush.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("qq_bot", qqInfoDao.findFirstBy().orElse(new QQInfo().setNumber(0L)).getNumber());
        User user = new User()
                .setName(name)
                .setPassword(password)
                .setCipher(authTools.generateCipher(digit))
                .setDayMaxSendCount(dayMaxSendCount)
                .setConfig(node.asText());
        userDao.save(user);
        return true;
    }

    @Override
    public boolean register(String name, String password, String openid) {
        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("qq_bot", qqInfoDao.findFirstBy().orElse(new QQInfo().setNumber(0L)).getNumber());
        User user = new User()
                .setName(name)
                .setPassword(password)
                .setCipher(authTools.generateCipher(digit))
                .setDayMaxSendCount(dayMaxSendCount)
                .setOpenid(openid)
                .setConfig(node.asText());
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
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode;
        try {
            objectNode = (ObjectNode) mapper.readTree(user.getConfig());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        objectNode.put("qq_bot", number);
        user.setConfig(objectNode.asText());
        userDao.save(user);
        return true;
    }

    @Override
    public int selectToDayUserUseCount(long uid) {
        return userDao.getTodayUseCount(uid);
    }


}
