package top.imzdx.qqpush.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.qqpush.model.dto.Msg;
import top.imzdx.qqpush.model.po.MessageLog;
import top.imzdx.qqpush.model.po.QQGroupWhitelist;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.MessageLogDao;
import top.imzdx.qqpush.repository.QQGroupWhitelistDao;
import top.imzdx.qqpush.repository.UserDao;
import top.imzdx.qqpush.utils.QqMsgContentTools;


/**
 * @author Renxing
 */
@Service("qq_group")
public class QQGroupMsgServiceImpl extends QQMsgServiceImpl {
    QQGroupWhitelistDao qqGroupWhitelistDao;

    @Autowired
    public QQGroupMsgServiceImpl(QqMsgContentTools qqMsgContentTools, MessageLogDao messageLogDao, UserDao userDao, QQGroupWhitelistDao qqGroupWhitelistDao) {
        super(qqMsgContentTools, messageLogDao, userDao);
        this.qqGroupWhitelistDao = qqGroupWhitelistDao;
    }

    @Override
    public void sendMsg(User user, Msg msg) {
        MessageLog messageLog = saveMsgToDB(msg, user.getUid());
        riskControl(user, messageLog);
        testAuthority(user, Long.valueOf(msg.getMeta().getData()), messageLog);
        try {
            long qqGroup = Long.parseLong(msg.getMeta().getData());
            ObjectNode node = new ObjectMapper().readValue(user.getConfig(), ObjectNode.class);
            Group group = Bot.findInstance(node.get("qq_bot").asLong()).getGroup(qqGroup);
            if (group != null) {
                group.sendMessage(qqMsgContentTools.buildMessage(msg.getContent(), messageLog));
                return;
            }
        } catch (NumberFormatException e) {
            throw messageLog.fail("收信群号码不正确");
        } catch (NullPointerException e) {
            throw messageLog.fail("绑定的机器人已失效，请前往官网重新绑定机器人");
        } catch (JsonProcessingException e) {
            throw messageLog.fail("用户机器人账户配置异常，请前往官网重新选择可用机器人");
        }
        throw messageLog.fail("该机器人离线或您没有将该机器人拉入目标qq群（先加好友然后拉群）。您当前的配置：" + user.getConfig());
    }

    private void testAuthority(User user, Long qqGroup, MessageLog messageLog) {
        QQGroupWhitelist qqGroupWhitelist = qqGroupWhitelistDao.findByNumber(qqGroup).orElseThrow(() -> messageLog.fail("该群不在白名单中"));
        if (!user.getUid().equals(qqGroupWhitelist.getUserId())) {
            throw messageLog.fail("您没有权限发送群消息");
        }
    }
}
