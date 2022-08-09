package top.imzdx.renxingpush.service.impl;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.renxingpush.model.po.MessageCallback;
import top.imzdx.renxingpush.repository.MessageCallbackDao;
import top.imzdx.renxingpush.repository.MessageCallbackLogDao;
import top.imzdx.renxingpush.repository.UserDao;
import top.imzdx.renxingpush.service.MsgCallbackService;

import static top.imzdx.renxingpush.model.po.MessageCallbackLog.TYPE_QQ;

@Service
public class QQMsgCallbackServiceImpl extends MsgCallbackService {
    UserDao userDao;

    @Autowired
    public QQMsgCallbackServiceImpl(MessageCallbackDao messageCallbackDao, MessageCallbackLogDao messageCallbackLogDao, UserDao userDao) {
        super(messageCallbackDao, messageCallbackLogDao);
        this.userDao = userDao;
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
                Long qqBotNumber = user.getConfig().getQqBot();
                Bot bot = Bot.findInstance(qqBotNumber);
                if (messageCallback.getGroup() == null) {
                    if (messageCallback.getReply()) {
                        bot.getFriend(Long.parseLong(messageCallback.getSender())).sendMessage(messageCallback.getResponse());
                    } else {
                        bot.getFriend(Long.parseLong(messageCallback.getSender())).sendMessage(messageCallback.getFeedback());
                    }
                } else {
                    if (messageCallback.getReply()) {
                        bot.getGroup(Long.parseLong(messageCallback.getGroup())).sendMessage(messageCallback.getResponse());
                    } else {
                        bot.getGroup(Long.parseLong(messageCallback.getGroup())).sendMessage(messageCallback.getFeedback());
                    }
                }
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
                Long qqBotNumber = user.getConfig().getQqBot();
                Bot bot = Bot.findInstance(qqBotNumber);
                bot.getFriend(Long.parseLong(messageCallback.getSender())).sendMessage("回调失败");
            });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
