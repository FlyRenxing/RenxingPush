package top.imzdx.renxingpush.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Renxing
 */
@Service
public class MsgServiceContext {
    Map<String, MsgService> msgServiceMap;

    @Autowired
    public MsgServiceContext(Map<String, MsgService> msgServiceMap) {
        this.msgServiceMap = msgServiceMap;
    }

    public MsgService getMsgService(String type) {
        return msgServiceMap.get(type);
    }
}
