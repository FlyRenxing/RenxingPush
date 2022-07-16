package top.imzdx.qqpush.service;

import org.springframework.data.domain.Example;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import top.imzdx.qqpush.model.po.MessageCallback;
import top.imzdx.qqpush.model.po.MessageCallbackLog;
import top.imzdx.qqpush.repository.MessageCallbackDao;
import top.imzdx.qqpush.repository.MessageCallbackLogDao;

import java.util.HashSet;

public abstract class MsgCallbackService {
    private static final HashSet<String> senderSet = new HashSet<>();
    MessageCallbackDao messageCallbackDao;
    MessageCallbackLogDao messageCallbackLogDao;

    public MsgCallbackService(MessageCallbackDao messageCallbackDao, MessageCallbackLogDao messageCallbackLogDao) {
        this.messageCallbackDao = messageCallbackDao;
        this.messageCallbackLogDao = messageCallbackLogDao;
        senderSet.addAll(messageCallbackDao.findDistinctSenderBy().stream().map(MessageCallback::getSender).toList());
    }

    public MessageCallback findMessageCallback(MessageCallback messageCallback) {
        if (senderSet.contains(messageCallback.getSender())) {
            String keyword = messageCallback.getKeyword();
            messageCallback.setKeyword(null);
            for (MessageCallback callback : messageCallbackDao.findAll(Example.of(messageCallback))) {
                if (keyword.contains(callback.getKeyword())) {
                    return callback;
                }
            }
        }
        return null;
    }

    public MessageCallbackLog saveMessageCallbackLog(MessageCallbackLog messageCallbackLog) {
        return messageCallbackLogDao.save(messageCallbackLog);
    }

    public String callback(MessageCallback messageCallback) {
        int tryCount = 0;
        while (tryCount < 3) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MessageCallback> httpEntity = new HttpEntity<>(messageCallback, headers);
            try {
                ResponseEntity<String> response = restTemplate.exchange(messageCallback.getCallbackURL(), HttpMethod.POST, httpEntity, String.class);
                //解析返回的数据
                return response.getBody();
            } catch (RestClientException e) {
                tryCount++;
            }
        }
        return null;
    }

    public MessageCallback addMessageCallback(MessageCallback messageCallback) {
        senderSet.add(messageCallback.getSender());
        return messageCallbackDao.save(messageCallback);
    }
}
