package cc.renxing.push.service.impl;

import cc.renxing.push.model.dto.Msg;
import cc.renxing.push.model.po.User;
import cc.renxing.push.repository.MessageLogDao;
import cc.renxing.push.service.MsgService;
import cc.renxing.push.utils.QQBotTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author Renxing
 */
@Service("qq_group")
public class QQGroupMsgServiceImpl extends MsgService {
    QQBotTools qqBotTools;

    @Autowired
    public QQGroupMsgServiceImpl(MessageLogDao messageLogDao, QQBotTools qqBotTools) {
        super(messageLogDao);
        this.qqBotTools = qqBotTools;
    }

    @Override
    public void sendMsg(User user, Msg msg) {
        qqBotTools.sendMsg(user, msg);
    }

}
