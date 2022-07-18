package top.imzdx.qqpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import top.imzdx.qqpush.model.dto.Msg;
import top.imzdx.qqpush.model.po.MessageLog;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.MessageLogDao;
import top.imzdx.qqpush.repository.UserDao;
import top.imzdx.qqpush.service.MsgService;
import top.imzdx.qqpush.utils.MsgContentTools;
import top.imzdx.qqpush.utils.TelegramBot;


/**
 * @author Renxing
 */
@Service("telegram")
public class TelegramMsgServiceImpl extends MsgService {
    MsgContentTools msgContentTools;
    MessageLogDao messageLogDao;
    UserDao userDao;

    TelegramBot telegramBot;

    @Autowired
    public TelegramMsgServiceImpl(MsgContentTools msgContentTools, MessageLogDao messageLogDao, UserDao userDao, TelegramBot telegramBot) {
        super(messageLogDao);
        this.msgContentTools = msgContentTools;
        this.messageLogDao = messageLogDao;
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
