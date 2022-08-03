package top.imzdx.renxingpush.service.impl;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.renxingpush.model.dto.Msg;
import top.imzdx.renxingpush.model.po.MessageLog;
import top.imzdx.renxingpush.model.po.QQGroupWhitelist;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.repository.MessageLogDao;
import top.imzdx.renxingpush.repository.QQGroupWhitelistDao;
import top.imzdx.renxingpush.repository.UserDao;
import top.imzdx.renxingpush.utils.MsgContentTools;

import java.util.List;


/**
 * @author Renxing
 */
@Service("qq_group")
public class QQGroupMsgServiceImpl extends QQMsgServiceImpl {
    QQGroupWhitelistDao qqGroupWhitelistDao;

    @Autowired
    public QQGroupMsgServiceImpl(MsgContentTools msgContentTools, MessageLogDao messageLogDao, UserDao userDao, QQGroupWhitelistDao qqGroupWhitelistDao) {
        super(msgContentTools, messageLogDao, userDao);
        this.qqGroupWhitelistDao = qqGroupWhitelistDao;
    }

    @Override
    public void sendMsg(User user, Msg msg) {
        MessageLog messageLog = saveMsgToDB(msg, user.getUid());
        riskControl(user, messageLog);
        testAuthority(user, Long.valueOf(msg.getMeta().getData()), messageLog);
        long qqBot=msg.getMeta().getQqBot()!=null?msg.getMeta().getQqBot():user.getConfig().getQqBot();
        try {
            long qqGroup = Long.parseLong(msg.getMeta().getData());
            Group group = Bot.findInstance(qqBot).getGroup(qqGroup);
            if (group != null) {
                group.sendMessage(msgContentTools.buildQQMessage(msg.getContent(), messageLog, group));
                return;
            }
        } catch (NumberFormatException e) {
            throw messageLog.fail("收信群号码不正确");
        } catch (NullPointerException e) {
            throw messageLog.fail("所选QQ机器人已失效，请前往官网重新设定默认机器人或附带qqBot参数指定机器人。当前所选机器人号码："+qqBot);
        }
        throw messageLog.fail("该机器人离线或您没有将该机器人拉入目标qq群（先加好友然后拉群）。当前所选机器人号码："+qqBot);
    }

    private void testAuthority(User user, Long qqGroup, MessageLog messageLog) {
        List<QQGroupWhitelist> qqGroupWhitelist = qqGroupWhitelistDao.findByNumber(qqGroup);
        if (qqGroupWhitelist.isEmpty()) {
            throw messageLog.fail("该群不在白名单中");
        }
        qqGroupWhitelist.forEach(qqGroupWhitelist1 -> {
            if (!qqGroupWhitelist1.getUserId().equals(user.getUid())) {
                throw messageLog.fail("您没有权限发送该群消息");
            }
        });
    }
}
