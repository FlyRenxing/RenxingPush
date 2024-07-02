package cc.renxing.push.service.impl;

import cc.renxing.push.model.dto.Msg;
import cc.renxing.push.model.po.MessageLog;
import cc.renxing.push.model.po.User;
import cc.renxing.push.repository.MessageLogDao;
import cc.renxing.push.repository.UserDao;
import cc.renxing.push.service.MsgService;
import cc.renxing.push.utils.MsgContentTools;
import cc.renxing.push.utils.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * @author Renxing
 */
@Service("telegram")
public class TelegramMsgServiceImpl extends MsgService {
    MsgContentTools msgContentTools;
    UserDao userDao;

    TelegramBot telegramBot;

    @Autowired
    public TelegramMsgServiceImpl(MsgContentTools msgContentTools, MessageLogDao messageLogDao, UserDao userDao, TelegramBot telegramBot) {
        super(messageLogDao);
        this.msgContentTools = msgContentTools;
        this.userDao = userDao;
        this.telegramBot = telegramBot;
    }

    public void sendMsg(User user, Msg msg) {
        MessageLog messageLog = saveMsgToDB(msg, user.getUid());

        SendMessage message = new SendMessage();
        message.setChatId(msg.getMeta().getData());
        message.setText(msg.getContent());
        try {
            telegramBot.execute(message);
        } catch (TelegramApiException e) {
            throw messageLog.fail("发送失败");
        }
    }
}
