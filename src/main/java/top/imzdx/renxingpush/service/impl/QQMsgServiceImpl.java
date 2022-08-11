package top.imzdx.renxingpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.renxingpush.model.dto.Msg;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.repository.MessageLogDao;
import top.imzdx.renxingpush.repository.UserDao;
import top.imzdx.renxingpush.service.MsgService;
import top.imzdx.renxingpush.utils.MsgContentTools;
import top.imzdx.renxingpush.utils.QQBotTools;


/**
 * @author Renxing
 */
@Service("qq")
public class QQMsgServiceImpl extends MsgService {
    MsgContentTools msgContentTools;
    MessageLogDao messageLogDao;
    UserDao userDao;
    QQBotTools qqBotTools;

    @Autowired
    public QQMsgServiceImpl(MsgContentTools msgContentTools, MessageLogDao messageLogDao, UserDao userDao, QQBotTools qqBotTools) {
        this.msgContentTools = msgContentTools;
        this.messageLogDao = messageLogDao;
        this.userDao = userDao;
        this.qqBotTools = qqBotTools;
    }

    @Override
    public void sendMsg(User user, Msg msg) {
        qqBotTools.sendMsg(user, msg);
    }

//    public void sendMsg(User user, Msg msg) {
//        MessageLog messageLog = saveMsgToDB(msg, user.getUid());
//        riskControl(user, messageLog);
//        long qqBot=msg.getMeta().getQqBot()!=null?msg.getMeta().getQqBot():user.getConfig().getQqBot();
//        try {
//            long qq = Long.parseLong(msg.getMeta().getData());
//            Friend friend = Bot.findInstance(qqBot).getFriend(qq);
//            if (friend != null) {
//                friend.sendMessage(msgContentTools.buildQQMessage(msg.getContent(), messageLog, friend));
//                return;
//            }
//        } catch (NumberFormatException e) {
//            throw messageLog.fail("收件号码不正确");
//        } catch (NullPointerException e) {
//            throw messageLog.fail("所选QQ机器人已失效，请前往官网重新设定默认机器人或附带qqBot参数指定机器人。");
//        }
//        throw messageLog.fail("该机器人离线或您没有添加指定机器人为好友，请先添加您目前绑定的机器人为好友,当前所选机器人号码："+qqBot);
//
//    }


}
