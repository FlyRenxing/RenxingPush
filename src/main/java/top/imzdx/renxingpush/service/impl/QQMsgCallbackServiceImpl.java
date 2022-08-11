package top.imzdx.renxingpush.service.impl;

import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.renxingpush.model.dto.Msg;
import top.imzdx.renxingpush.model.dto.MsgMeta;
import top.imzdx.renxingpush.model.po.MessageCallback;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.repository.MessageCallbackDao;
import top.imzdx.renxingpush.repository.MessageCallbackLogDao;
import top.imzdx.renxingpush.repository.UserDao;
import top.imzdx.renxingpush.service.MsgCallbackService;
import top.imzdx.renxingpush.utils.QQBotTools;

import static top.imzdx.renxingpush.model.po.MessageCallbackLog.TYPE_QQ;

@Service
public class QQMsgCallbackServiceImpl extends MsgCallbackService {
    UserDao userDao;
    QQBotTools qqBotTools;

    QQGroupMsgServiceImpl qqGroupMsgServiceImpl;

    @Autowired
    public QQMsgCallbackServiceImpl(MessageCallbackDao messageCallbackDao, MessageCallbackLogDao messageCallbackLogDao, UserDao userDao, QQBotTools qqBotTools, QQGroupMsgServiceImpl qqGroupMsgServiceImpl) {
        super(messageCallbackDao, messageCallbackLogDao);
        this.userDao = userDao;
        this.qqBotTools = qqBotTools;
        this.qqGroupMsgServiceImpl = qqGroupMsgServiceImpl;
        GlobalEventChannel.INSTANCE.subscribeAlways(MessageEvent.class, event -> {
            MessageChain chain = event.getMessage();
            MessageCallback messageCallback = new MessageCallback()
                    .setAppType(TYPE_QQ)
                    .setSender(String.valueOf(event.getSender().getId()))
                    .setMessage(chain.contentToString());
            if (event instanceof GroupMessageEvent groupMessageEvent) {
                messageCallback.setGroup(String.valueOf(groupMessageEvent.getGroup().getId()));
            }
            haveNewMessage(messageCallback);
        });
    }

    @Override
    public boolean successCallback(MessageCallback messageCallback) {
        try {
            userDao.findById(messageCallback.getUid()).ifPresent(user -> {
                Msg msg = getMsg(user, messageCallback);
                if (messageCallback.getReply()) {
                    msg.setContent(messageCallback.getResponse());
                } else {
                    msg.setContent(messageCallback.getFeedback());
                }
                qqGroupMsgServiceImpl.sendMsg(user, msg);
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean failCallback(MessageCallback messageCallback) {
        try {
            userDao.findById(messageCallback.getUid()).ifPresent(user -> {
                Msg msg = getMsg(user, messageCallback);
                msg.setContent("回调失败");
                qqBotTools.sendMsg(user, msg);
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private Msg getMsg(User user, MessageCallback messageCallback) {
        Long qqBotNumber = user.getConfig().getQqBot();
        return new Msg() {{
            MsgMeta meta = new MsgMeta();
            meta.setType(messageCallback.getGroup() == null ? MsgMeta.MSG_TYPE_QQ : MsgMeta.MSG_TYPE_QQ_GROUP);
            meta.setData(messageCallback.getGroup() == null ? messageCallback.getSender() : messageCallback.getGroup());
            meta.setQqBot(qqBotNumber);
            setMeta(meta);
        }};
    }
}
