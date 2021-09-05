package top.imzdx.qqpush.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Renxing
 */
@Service
public class MsgServiceContext {
    @Autowired
    Map<String, MsgService> msgServiceMap;

    public MsgService getMsgService(String type) {
        return msgServiceMap.get(type);
    }
}
