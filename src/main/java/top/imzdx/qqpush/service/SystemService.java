package top.imzdx.qqpush.service;

import top.imzdx.qqpush.model.po.Note;
import top.imzdx.qqpush.model.po.QQGroupWhitelist;
import top.imzdx.qqpush.model.po.QqInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Renxing
 */
public interface SystemService {

    List<QqInfo> getPublicQqBot();

    List<Note> getAllNote();

    String generateCaptcha(HttpServletRequest request);

    boolean checkCaptcha(HttpServletRequest request);

    Boolean insertQQGroupWhitelist(QQGroupWhitelist qqGroupWhitelist);
}
