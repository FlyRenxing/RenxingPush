package top.imzdx.renxingpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.util.AbilityExtension;
import top.imzdx.renxingpush.model.po.MessageCallback;
import top.imzdx.renxingpush.repository.MessageCallbackDao;
import top.imzdx.renxingpush.repository.MessageCallbackLogDao;
import top.imzdx.renxingpush.service.MsgCallbackService;
import top.imzdx.renxingpush.utils.TelegramBot;

@Service
public class TelegramMsgCallbackServiceImpl extends MsgCallbackService  implements AbilityExtension {
    TelegramBot telegramBot;
    @Autowired
    public TelegramMsgCallbackServiceImpl(MessageCallbackDao messageCallbackDao, MessageCallbackLogDao messageCallbackLogDao, TelegramBot telegramBot) {
        super(messageCallbackDao, messageCallbackLogDao);
        this.telegramBot = telegramBot;
    }


    @Override
    public boolean successCallback(MessageCallback messageCallback) {
        return false;
    }

    @Override
    public boolean failCallback(MessageCallback messageCallback) {
        return false;
    }
}
