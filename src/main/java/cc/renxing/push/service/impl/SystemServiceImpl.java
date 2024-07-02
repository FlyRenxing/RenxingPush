package cc.renxing.push.service.impl;

import cc.renxing.push.config.AppConfig;
import cc.renxing.push.config.GeetestConfig;
import cc.renxing.push.model.po.*;
import cc.renxing.push.repository.MessageCallbackDao;
import cc.renxing.push.repository.NoteDao;
import cc.renxing.push.repository.QQGroupWhitelistDao;
import cc.renxing.push.repository.QQInfoDao;
import cc.renxing.push.service.MsgCallbackService;
import cc.renxing.push.service.SystemService;
import cc.renxing.push.utils.AuthTools;
import cc.renxing.push.utils.DefinitionException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author Renxing
 */
@Service
public class SystemServiceImpl implements SystemService {
    QQInfoDao qqInfoDao;
    QQGroupWhitelistDao qqGroupWhitelistDao;
    MessageCallbackDao messageCallbackDao;
    MsgCallbackService msgCallbackService;
    NoteDao noteDao;
    GeetestConfig geetestConfig;

    AppConfig appConfig;

    @Autowired
    public SystemServiceImpl(QQInfoDao qqInfoDao, QQGroupWhitelistDao qqGroupWhitelistDao, MessageCallbackDao messageCallbackDao, @Qualifier("QQMsgCallbackServiceImpl")MsgCallbackService msgCallbackService, NoteDao noteDao, GeetestConfig geetestConfig, AppConfig appConfig) {
        this.qqInfoDao = qqInfoDao;
        this.qqGroupWhitelistDao = qqGroupWhitelistDao;
        this.messageCallbackDao = messageCallbackDao;
        this.msgCallbackService = msgCallbackService;
        this.noteDao = noteDao;
        this.geetestConfig = geetestConfig;
        this.appConfig = appConfig;
    }

    @Override
    public List<QQInfo> getPublicQqBot() {
        return qqInfoDao.findAll();
    }

    @Override
    public List<Note> getAllNote() {
        return noteDao.findAll();
    }

    @Override
    public boolean checkCaptcha(String lotNumber, String captchaOutput, String passToken, String genTime) {
        if (lotNumber==null||captchaOutput==null||passToken==null||genTime==null){
            throw new DefinitionException("验证码信息缺失");
        }
        // 1.初始化极验参数信息
        // 1.initialize geetest parameter
        String captchaId = geetestConfig.getGeetest_id();
        String captchaKey = geetestConfig.getGeetest_key();
        String domain = "http://gcaptcha4.geetest.com";

        // 3.生成签名
        // 3.generate signature
        // 生成签名使用标准的hmac算法，使用用户当前完成验证的流水号lot_number作为原始消息message，使用客户验证私钥作为key
        // use standard hmac algorithms to generate signatures, and take the user's current verification serial number lot_number as the original message, and the client's verification private key as the key
        // 采用sha256散列算法将message和key进行单向散列生成最终的签名
        // use sha256 hash algorithm to hash message and key in one direction to generate the final signature
        String signToken = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, captchaKey).hmacHex(lotNumber);

        // 4.上传校验参数到极验二次验证接口, 校验用户验证状态
        // 4.upload verification parameters to the secondary verification interface of GeeTest to validate the user verification status
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("lot_number", lotNumber);
        queryParams.add("captcha_output", captchaOutput);
        queryParams.add("pass_token", passToken);
        queryParams.add("gen_time", genTime);
        queryParams.add("sign_token", signToken);
        // captcha_id 参数建议放在 url 后面, 方便请求异常时可以在日志中根据id快速定位到异常请求
        // geetest recommends to put captcha_id parameter after url, so that when a request exception occurs, it can be quickly located in the log according to the id
        String url = String.format(domain + "/validate" + "?captcha_id=%s", captchaId);
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //注意处理接口异常情况，当请求极验二次验证接口异常时做出相应异常处理
        // pay attention to interface exceptions, and make corresponding exception handling when requesting GeeTest secondary verification interface exceptions or response status is not 200
        //保证不会因为接口请求超时或服务未响应而阻碍业务流程
        // website's business will not be interrupted due to interface request timeout or server not-responding
        JsonNode jsonNode;
        try {
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(queryParams, headers);
            ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
            String resBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readTree(resBody);
        } catch (Exception e) {
            throw new DefinitionException("验证码处理失败");
        }
        // 5.根据极验返回的用户验证状态, 网站主进行自己的业务逻辑
        // 5. taking the user authentication status returned from geetest into consideration, the website owner follows his own business logic
//        System.out.println(jsonNode.toString());
        return jsonNode.get("result").asText().equals("success");

    }

    @Override
    public QQGroupWhitelist insertQQGroupWhitelist(QQGroupWhitelist qqGroupWhitelist) {
        User user = AuthTools.getUser();
        var isAdmin = user.getAdmin() != 0;
        if (!isAdmin && !appConfig.getSystem().isOpenQqgroupWhitelistApply()) {
            throw new DefinitionException("您没有权限进行此操作");
        }
        qqGroupWhitelistDao.findOne(Example.of(qqGroupWhitelist)).ifPresent(qqGroupWhitelist1 -> {
            throw new DefinitionException("您已有该群的权限");
        });
        return qqGroupWhitelistDao.save(qqGroupWhitelist);
    }

    @Override
    public List<QQGroupWhitelist> getQQGroupWhitelist(Long uid) {
        return qqGroupWhitelistDao.findByUserId(uid);
    }

    @Override
    public boolean deleteQQGroupWhitelist(Long id) {
        User user = AuthTools.getUser();
        qqGroupWhitelistDao.findById(id).ifPresent(qqGroupWhitelist -> {
            if (user.getAdmin() != 0 || qqGroupWhitelist.getUserId().equals(user.getUid())) {
                qqGroupWhitelistDao.deleteById(id);
            } else {
                throw new DefinitionException("您没有权限进行此操作");
            }
        });
        return true;
    }

    @Override
    public List<MessageCallback> getMessageCallback(Long uid) {
        return messageCallbackDao.findByUid(uid);
    }

    @Override
    public MessageCallback insertMessageCallback(MessageCallback messageCallback) {
        User user = AuthTools.getUser();
        messageCallback.setUid(user.getUid());

        return msgCallbackService.addMessageCallback(messageCallback);
    }

    @Override
    public boolean deleteMessageCallback(Long id) {
        User user = AuthTools.getUser();
        messageCallbackDao.findById(id).ifPresent(messageCallback -> {
            if (user.getAdmin() != 0 || messageCallback.getUid().equals(user.getUid())) {
                messageCallbackDao.deleteById(id);
            } else {
                throw new DefinitionException("您没有权限进行此操作");
            }
        });
        return true;
    }
}
