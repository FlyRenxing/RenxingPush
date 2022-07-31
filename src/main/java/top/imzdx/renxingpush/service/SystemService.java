package top.imzdx.renxingpush.service;

import top.imzdx.renxingpush.model.po.MessageCallback;
import top.imzdx.renxingpush.model.po.Note;
import top.imzdx.renxingpush.model.po.QQGroupWhitelist;
import top.imzdx.renxingpush.model.po.QQInfo;

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
