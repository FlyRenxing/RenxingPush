package top.imzdx.renxingpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import top.imzdx.renxingpush.model.po.MessageCallback;
import top.imzdx.renxingpush.repository.MessageCallbackDao;
import top.imzdx.renxingpush.repository.MessageCallbackLogDao;
import top.imzdx.renxingpush.service.MsgCallbackService;
import top.imzdx.renxingpush.utils.TelegramBot;


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
                    telegramBot.silent().send(messageCallback.getResponse(), Long.parseLong(messageCallback.getSender()));
                } else {
                    telegramBot.silent().send(messageCallback.getResponse(), Long.parseLong(messageCallback.getGroup()));
                }
            } else {
                if (messageCallback.getGroup() == null) {
                    telegramBot.silent().send(messageCallback.getFeedback(), Long.parseLong(messageCallback.getSender()));
                } else {
                    telegramBot.silent().send(messageCallback.getFeedback(), Long.parseLong(messageCallback.getGroup()));
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean failCallback(MessageCallback messageCallback) {
        telegramBot.silent().send(messageCallback.getFeedback(), Long.parseLong(messageCallback.getSender()));
        return false;
    }

}
