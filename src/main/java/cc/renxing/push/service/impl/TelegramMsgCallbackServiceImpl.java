package cc.renxing.push.service.impl;

import cc.renxing.push.model.po.MessageCallback;
import cc.renxing.push.repository.MessageCallbackDao;
import cc.renxing.push.repository.MessageCallbackLogDao;
import cc.renxing.push.service.MsgCallbackService;
import cc.renxing.push.utils.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


@Service
public class TelegramMsgCallbackServiceImpl extends MsgCallbackService {

    private final TelegramBot telegramBot;

    @Autowired
    public TelegramMsgCallbackServiceImpl(@Lazy TelegramBot telegramBot, MessageCallbackDao messageCallbackDao, MessageCallbackLogDao messageCallbackLogDao) {
        super(messageCallbackDao, messageCallbackLogDao);
        this.telegramBot = telegramBot;
    }


    @Override
    public boolean successCallback(MessageCallback messageCallback) {
        try {
            if (messageCallback.getReply()) {
                if (messageCallback.getGroup() == null) {
                    telegramBot.getSilent().send(messageCallback.getResponse(), Long.parseLong(messageCallback.getSender()));
                } else {
                    telegramBot.getSilent().send(messageCallback.getResponse(), Long.parseLong(messageCallback.getGroup()));
                }
            } else {
                if (messageCallback.getGroup() == null) {
                    telegramBot.getSilent().send(messageCallback.getFeedback(), Long.parseLong(messageCallback.getSender()));
                } else {
                    telegramBot.getSilent().send(messageCallback.getFeedback(), Long.parseLong(messageCallback.getGroup()));
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean failCallback(MessageCallback messageCallback) {
        telegramBot.getSilent().send(messageCallback.getFeedback(), Long.parseLong(messageCallback.getSender()));
        return false;
    }

}
