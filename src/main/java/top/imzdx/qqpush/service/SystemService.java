package top.imzdx.qqpush.service;

import top.imzdx.qqpush.model.po.Note;
import top.imzdx.qqpush.model.po.QqInfo;

import java.util.List;

/**
 * @author Renxing
 */
public interface SystemService {

    List<QqInfo> getPublicQqBot();

    List<Note> getAllNote();
}
