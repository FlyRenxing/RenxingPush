package cc.renxing.push.service;


import cc.renxing.push.model.dto.Msg;
import cc.renxing.push.model.po.MessageLog;
import cc.renxing.push.model.po.User;
import cc.renxing.push.repository.MessageLogDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Renxing
 */
public abstract class MsgService {
    static MessageLogDao messageLogDao;

    public MsgService(MessageLogDao messageLogDao) {
        MsgService.messageLogDao = messageLogDao;
    }

    public abstract void sendMsg(User user, Msg msg);

    public static MessageLog saveMsgToDB(Msg msg, long uid) {
        ObjectMapper mapper = new ObjectMapper();
        MessageLog messageLog = null;
        try {
            messageLog = new MessageLog()
                    .setContent(msg.getContent())
                    .setMeta(mapper.writeValueAsString(msg.getMeta()))
                    .setUid(uid)
                    .setStatus(MessageLog.STATUS_SUCCESS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return messageLogDao.save(messageLog);
    }
}
