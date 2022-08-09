package top.imzdx.renxingpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.renxingpush.model.po.MessageCallback;
import top.imzdx.renxingpush.repository.MessageCallbackDao;
import top.imzdx.renxingpush.repository.MessageCallbackLogDao;
import top.imzdx.renxingpush.service.MsgCallbackService;

import static top.imzdx.renxingpush.AppRunner.getTelegramBot;

@Service
public class TelegramMsgCallbackServiceImpl extends MsgCallbackService{
    @Autowired
    public TelegramMsgCallbackServiceImpl(MessageCallbackDao messageCallbackDao, MessageCallbackLogDao messageCallbackLogDao) {
        super(messageCallbackDao, messageCallbackLogDao);
    }


    @Override
    public boolean successCallback(MessageCallback messageCallback) {
        try {
            if (messageCallback.getReply()){
                if (messageCallback.getGroup() == null){
                    getTelegramBot().silent().send(messageCallback.getResponse(), Long.parseLong(messageCallback.getSender()));
                }else {
                    getTelegramBot().silent().send(messageCallback.getResponse(), Long.parseLong(messageCallback.getGroup()));
                }
            }else {
                if (messageCallback.getGroup() == null){
                    getTelegramBot().silent().send(messageCallback.getFeedback(), Long.parseLong(messageCallback.getSender()));
                }else {
                    getTelegramBot().silent().send(messageCallback.getFeedback(), Long.parseLong(messageCallback.getGroup()));
                }
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean failCallback(MessageCallback messageCallback) {
        getTelegramBot().silent().send(messageCallback.getFeedback(), Long.parseLong(messageCallback.getSender()));
        return false;
    }
}
