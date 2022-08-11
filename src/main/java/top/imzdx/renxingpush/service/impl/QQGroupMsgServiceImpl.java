package top.imzdx.renxingpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.renxingpush.model.dto.Msg;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.service.MsgService;
import top.imzdx.renxingpush.utils.QQBotTools;


/**
 * @author Renxing
 */
@Service("qq_group")
public class QQGroupMsgServiceImpl extends MsgService {
    QQBotTools qqBotTools;

    @Autowired
    public QQGroupMsgServiceImpl(QQBotTools qqBotTools) {
        this.qqBotTools = qqBotTools;
    }

    @Override
    public void sendMsg(User user, Msg msg) {
        qqBotTools.sendMsg(user, msg);
    }

//    @Override
//    public void sendMsg(User user, Msg msg) {
//        MessageLog messageLog = saveMsgToDB(msg, user.getUid());
//        riskControl(user, messageLog);
//        testAuthority(user, Long.valueOf(msg.getMeta().getData()), messageLog);
//        long qqBot = msg.getMeta().getQqBot() != null ? msg.getMeta().getQqBot() : user.getConfig().getQqBot();
//        try {
//            long qqGroup = Long.parseLong(msg.getMeta().getData());
//            Group group = qqBotTools.findGroup(qqBot, qqGroup);
//            if (group != null) {
//                group.sendMessage(msgContentTools.buildQQMessage(msg.getContent(), messageLog, group));
//                return;
//            }
//        } catch (NumberFormatException e) {
//            throw messageLog.fail("收信群号码不正确");
//        } catch (QQBotTools.NullQQBotException e) {
//            throw messageLog.fail("所选QQ机器人已失效，请前往官网重新设定默认机器人或附带qqBot参数指定机器人。当前所选机器人号码：" + qqBot);
//        } catch (QQBotTools.NullQQGroupException e) {
//            throw messageLog.fail("收信群号码不正确或所选机器人未进群聊");
//        }
//        throw messageLog.fail("该机器人离线或您没有将该机器人拉入目标qq群（先加好友然后拉群）。当前所选机器人号码：" + qqBot);
//    }
}
