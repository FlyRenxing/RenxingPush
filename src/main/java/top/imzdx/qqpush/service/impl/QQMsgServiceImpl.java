package top.imzdx.qqpush.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.qqpush.model.dto.Msg;
import top.imzdx.qqpush.model.po.MessageLog;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.MessageLogDao;
import top.imzdx.qqpush.repository.UserDao;
import top.imzdx.qqpush.service.MsgService;
import top.imzdx.qqpush.utils.DefinitionException;
import top.imzdx.qqpush.utils.QqMsgContentTools;


/**
 * @author Renxing
 */
@Service("qq")
public class QQMsgServiceImpl implements MsgService {
    QqMsgContentTools qqMsgContentTools;
    MessageLogDao messageLogDao;
    UserDao userDao;

    @Autowired
    public QQMsgServiceImpl(QqMsgContentTools qqMsgContentTools, MessageLogDao messageLogDao, UserDao userDao) {
        this.qqMsgContentTools = qqMsgContentTools;
        this.messageLogDao = messageLogDao;
        this.userDao = userDao;
    }

    @Override
    public void sendMsg(User user, Msg msg) {
        riskControl(user);
        try {
            long qq = Long.parseLong(msg.getMeta().getData());
            ObjectNode node = new ObjectMapper().readValue(user.getConfig(), ObjectNode.class);
            Friend friend = Bot.findInstance(node.get("qq_bot").asLong()).getFriend(qq);
            if (friend != null) {
                saveMsgToDB(msg, user.getUid());
                friend.sendMessage(qqMsgContentTools.buildMessage(msg.getContent()));
                return;
            }
        } catch (NumberFormatException e) {
            throw new DefinitionException("收件号码不正确");
        } catch (NullPointerException e) {
            throw new DefinitionException("绑定的机器人已失效，请前往官网重新绑定机器人");
        } catch (JsonProcessingException e) {
            throw new DefinitionException("用户机器人账户配置异常，请前往官网重新选择可用机器人");
        }
        throw new DefinitionException("该机器人离线或您没有添加指定机器人为好友，请先添加您目前绑定的机器人为好友。QQ:" + JSONObject.parseObject(user.getConfig()).getLong("qq_bot"));
    }

    @Override
    public void saveMsgToDB(Msg msg, long uid) {
        MessageLog messageLog = new MessageLog()
                .setContent(msg.getContent())
                .setMeta(JSONObject.toJSONString(msg.getMeta()))
                .setUid(uid);
        messageLogDao.save(messageLog);
    }

    void riskControl(User user) {
        long dayMaxSendCount = user.getDayMaxSendCount();
        if (userDao.getTodayUseCount(user.getUid()) >= dayMaxSendCount) {
            throw new DefinitionException("您当日消息已达到" + dayMaxSendCount + "条，请明日再试。");
        }
        if (userDao.getLastThreeSecondUseCount(user.getUid()) >= 3) {
            throw new DefinitionException("发送消息过于频繁，请三秒后再试。");
        }
    }
}
