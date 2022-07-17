package top.imzdx.qqpush.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import top.imzdx.qqpush.model.dto.Msg;
import top.imzdx.qqpush.model.po.MessageLog;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.MessageLogDao;

/**
 * @author Renxing
 */
public abstract class MsgService {
    MessageLogDao messageLogDao;

    public MsgService(MessageLogDao messageLogDao) {
        this.messageLogDao = messageLogDao;
    }

    public abstract void sendMsg(User user, Msg msg);

    public MessageLog saveMsgToDB(Msg msg, long uid) {
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
