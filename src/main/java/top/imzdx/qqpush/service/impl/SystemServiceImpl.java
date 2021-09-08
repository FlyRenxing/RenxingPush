package top.imzdx.qqpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.qqpush.dao.QqInfoDao;
import top.imzdx.qqpush.model.po.QqInfo;
import top.imzdx.qqpush.service.SystemService;

import java.util.List;

/**
 * @author Renxing
 */
@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    QqInfoDao qqInfoDao;

    @Override
    public List<QqInfo> getPublicQqBot() {
        return qqInfoDao.findAll();
    }
}
