package top.imzdx.qqpush.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.qqpush.dao.MessageLogDao;
import top.imzdx.qqpush.dao.UserDao;
import top.imzdx.qqpush.model.dto.Msg;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.service.MsgService;
import top.imzdx.qqpush.utils.DefinitionException;
import top.imzdx.qqpush.utils.QqMsgContentTools;


/**
 * @author Renxing
 */
@Service("qq")
public class QQMsgServiceImpl implements MsgService {
    @Autowired
    QqMsgContentTools qqMsgContentTools;
    @Autowired
    MessageLogDao messageLogDao;
    @Autowired
    UserDao userDao;


    @Override
    public void sendMsg(User user, Msg msg) {
        riskControl(user);
        try {
            long qq = Long.parseLong(msg.getMeta().getData());
            Friend friend = Bot.findInstance(JSONObject.parseObject(user.getConfig()).getLong("qq_bot"))
                    .getFriend(qq);
            if (friend != null) {
                saveMsgToDB(msg, user.getUid());
                friend.sendMessage(qqMsgContentTools.buildMessage(msg.getContent()));
                return;
            }
        } catch (NumberFormatException e) {
            throw new DefinitionException("收件号码不正确");
        } catch (NullPointerException e) {
            throw new DefinitionException("绑定的机器人已失效，请前往官网重新绑定机器人");
        }
        throw new DefinitionException("该机器人离线或您没有添加指定机器人为好友，请先添加您目前绑定的机器人为好友。QQ:" + JSONObject.parseObject(user.getConfig()).getLong("qq_bot"));
    }

    @Override
    public void saveMsgToDB(Msg msg, long uid) {
        messageLogDao.InsertMessageLog(msg.getContent(), JSONObject.toJSONString(msg.getMeta()), uid);
    }

    private void riskControl(User user) {
        long dayMaxSendCount = user.getDayMaxSendCount();
        if (userDao.selectToDayUserUseCount(user.getUid()) >= dayMaxSendCount) {
            throw new DefinitionException("您当日消息已达到" + dayMaxSendCount + "条，请明日再试。");
        }
        if (userDao.selectThreeSecondUserUseCount(user.getUid()) >= 3) {
            throw new DefinitionException("发送消息过于频繁，请三秒后再试。");
        }
    }
}
