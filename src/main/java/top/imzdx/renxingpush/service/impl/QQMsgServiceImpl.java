package top.imzdx.renxingpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.renxingpush.model.dto.Msg;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.repository.MessageLogDao;
import top.imzdx.renxingpush.repository.UserDao;
import top.imzdx.renxingpush.service.MsgService;
import top.imzdx.renxingpush.utils.MsgContentTools;
import top.imzdx.renxingpush.utils.QQBotTools;


/**
 * @author Renxing
 */
@Service("qq")
public class QQMsgServiceImpl extends MsgService {
    MsgContentTools msgContentTools;
    UserDao userDao;
    QQBotTools qqBotTools;

    @Autowired
    public QQMsgServiceImpl(MsgContentTools msgContentTools, MessageLogDao messageLogDao, UserDao userDao, QQBotTools qqBotTools) {
        super(messageLogDao);
        this.msgContentTools = msgContentTools;
        this.userDao = userDao;
        this.qqBotTools = qqBotTools;
    }

    @Override
    public void sendMsg(User user, Msg msg) {
        qqBotTools.sendMsg(user, msg);
    }


}
