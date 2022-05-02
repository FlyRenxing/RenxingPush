package top.imzdx.qqpush.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        MessageLog messageLog = saveMsgToDB(msg, user.getUid());
        riskControl(user, messageLog);
        try {
            long qq = Long.parseLong(msg.getMeta().getData());
            Friend friend = Bot.findInstance(user.getConfig().getQqBot()).getFriend(qq);
            if (friend != null) {
                friend.sendMessage(qqMsgContentTools.buildMessage(msg.getContent(), messageLog));
                return;
            }
        } catch (NumberFormatException e) {
            throw messageLog.fail("收件号码不正确");
        } catch (NullPointerException e) {
            throw messageLog.fail("绑定的机器人已失效，请前往官网重新绑定机器人");
        }
        throw messageLog.fail("该机器人离线或您没有添加指定机器人为好友，请先添加您目前绑定的机器人为好友");

    }

    @Override
    public MessageLog saveMsgToDB(Msg msg, long uid) {
        ObjectMapper mapper = new ObjectMapper();
        MessageLog messageLog = null;
        try {
            messageLog = new MessageLog()
                    .setContent(msg.getContent())
                    .setMeta(mapper.writeValueAsString(msg.getMeta()))
                    .setUid(uid)
                    .setStatus(MessageLog.STATUS_SUCCESS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return messageLogDao.save(messageLog);
    }

    void riskControl(User user, MessageLog messageLog) {
        long dayMaxSendCount = user.getDayMaxSendCount();
        if (userDao.getTodayUseCount(user.getUid()) >= dayMaxSendCount) {
            throw messageLog.fail("您当日消息已达到" + dayMaxSendCount + "条，请明日再试。");
        }
        if (userDao.getLastThreeSecondUseCount(user.getUid()) >= 3) {
            throw messageLog.fail("发送消息过于频繁，请三秒后再试。");
        }
    }
}
