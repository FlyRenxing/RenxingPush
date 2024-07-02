package cc.renxing.push.service.impl;

import cc.renxing.push.model.dto.Msg;
import cc.renxing.push.model.po.User;
import cc.renxing.push.repository.MessageLogDao;
import cc.renxing.push.repository.UserDao;
import cc.renxing.push.service.MsgService;
import cc.renxing.push.utils.MsgContentTools;
import cc.renxing.push.utils.QQBotTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
