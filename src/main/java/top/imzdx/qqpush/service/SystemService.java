package top.imzdx.qqpush.service;

import jakarta.servlet.http.HttpServletRequest;
import top.imzdx.qqpush.model.po.MessageCallback;
import top.imzdx.qqpush.model.po.Note;
import top.imzdx.qqpush.model.po.QQGroupWhitelist;
import top.imzdx.qqpush.model.po.QQInfo;

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
