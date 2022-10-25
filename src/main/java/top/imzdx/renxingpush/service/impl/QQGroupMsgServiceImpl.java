package top.imzdx.renxingpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.renxingpush.model.dto.Msg;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.repository.MessageLogDao;
import top.imzdx.renxingpush.service.MsgService;
import top.imzdx.renxingpush.utils.QQBotTools;


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
