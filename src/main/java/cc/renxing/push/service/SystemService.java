package cc.renxing.push.service;

import cc.renxing.push.model.po.MessageCallback;
import cc.renxing.push.model.po.Note;
import cc.renxing.push.model.po.QQGroupWhitelist;
import cc.renxing.push.model.po.QQInfo;

import java.util.List;

/**
 * @author Renxing
 */
public interface SystemService {

    List<QQInfo> getPublicQqBot();

    List<Note> getAllNote();

    boolean checkCaptcha(String lotNumber, String captchaOutput, String passToken, String genTime);

    QQGroupWhitelist insertQQGroupWhitelist(QQGroupWhitelist qqGroupWhitelist);

    List<QQGroupWhitelist> getQQGroupWhitelist(Long uid);

    boolean deleteQQGroupWhitelist(Long id);

    List<MessageCallback> getMessageCallback(Long uid);

    MessageCallback insertMessageCallback(MessageCallback messageCallback);

    boolean deleteMessageCallback(Long id);
}
