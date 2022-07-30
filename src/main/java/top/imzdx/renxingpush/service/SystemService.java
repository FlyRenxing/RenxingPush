package top.imzdx.renxingpush.service;

import jakarta.servlet.http.HttpServletRequest;
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

    String generateCaptcha(HttpServletRequest request);

    boolean checkCaptcha(HttpServletRequest request);

    QQGroupWhitelist insertQQGroupWhitelist(QQGroupWhitelist qqGroupWhitelist);

    List<QQGroupWhitelist> getQQGroupWhitelist(Long uid);

    boolean deleteQQGroupWhitelist(Long id);

    List<MessageCallback> getMessageCallback(Long uid);

    MessageCallback insertMessageCallback(MessageCallback messageCallback);

    boolean deleteMessageCallback(Long id);
}
