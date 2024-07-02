package cc.renxing.push.service;

import cc.renxing.push.model.po.MessageCallback;
import cc.renxing.push.model.po.MessageCallbackLog;
import cc.renxing.push.repository.MessageCallbackDao;
import cc.renxing.push.repository.MessageCallbackLogDao;
import org.springframework.data.domain.Example;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;

public abstract class MsgCallbackService {
    private static final HashSet<String> senderSet = new HashSet<>();
    MessageCallbackDao messageCallbackDao;
    MessageCallbackLogDao messageCallbackLogDao;

    public abstract boolean successCallback(MessageCallback messageCallback);

    public abstract boolean failCallback(MessageCallback messageCallback);

    public MsgCallbackService(MessageCallbackDao messageCallbackDao, MessageCallbackLogDao messageCallbackLogDao) {
        this.messageCallbackDao = messageCallbackDao;
        this.messageCallbackLogDao = messageCallbackLogDao;
        senderSet.addAll(messageCallbackDao.findDistinctSenderBy().stream().map(MessageCallback::getSender).toList());
    }

    public void haveNewMessage(MessageCallback messageCallback) {
        if (senderSet.contains(messageCallback.getSender())) {
            for (MessageCallback _callback : messageCallbackDao.findAll(Example.of(messageCallback))) {
                if (messageCallback.getMessage().contains(_callback.getKeyword())) {
                    _callback.setMessage(messageCallback.getMessage());
                    String feedback = callback(_callback);

                    MessageCallbackLog messageCallbackLog = new MessageCallbackLog()
                            .setAppType(_callback.getAppType())
                            .setContent(_callback.getMessage())
                            .setUid(_callback.getUid())
                            .setFeedback(feedback);
                    if (feedback != null) {
                        _callback.setFeedback(feedback);
                        _callback.setGroup(messageCallback.getGroup());
                        if (this.successCallback(_callback)) {
                            messageCallbackLog.success();
                        } else {
                            messageCallbackLog.fail();
                        }
                    } else {
                        this.failCallback(_callback);
                        messageCallbackLog.fail();
                    }

                }
            }
        }
    }

    private String callback(MessageCallback messageCallback) {
        int tryCount = 0;
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //10s
        requestFactory.setConnectTimeout(10 * 1000);
        requestFactory.setReadTimeout(10 * 1000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MessageCallback> httpEntity = new HttpEntity<>(messageCallback, headers);
        while (tryCount < 3) {
            try {
                ResponseEntity<String> response = restTemplate.exchange(messageCallback.getCallbackURL(), HttpMethod.GET, httpEntity, String.class);
                //解析返回的数据
                return response.getBody();
            } catch (Exception e) {
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
