package top.imzdx.qqpush.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import top.imzdx.qqpush.config.AppConfig;
import top.imzdx.qqpush.config.GeetestConfig;
import top.imzdx.qqpush.model.po.*;
import top.imzdx.qqpush.repository.MessageCallbackDao;
import top.imzdx.qqpush.repository.NoteDao;
import top.imzdx.qqpush.repository.QQGroupWhitelistDao;
import top.imzdx.qqpush.repository.QQInfoDao;
import top.imzdx.qqpush.service.MsgCallbackService;
import top.imzdx.qqpush.service.SystemService;
import top.imzdx.qqpush.utils.AuthTools;
import top.imzdx.qqpush.utils.DefinitionException;
import top.imzdx.qqpush.utils.GeetestLib;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
    public SystemServiceImpl(QQInfoDao qqInfoDao, QQGroupWhitelistDao qqGroupWhitelistDao, MessageCallbackDao messageCallbackDao, MsgCallbackService msgCallbackService, NoteDao noteDao, GeetestConfig geetestConfig, AppConfig appConfig) {
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
    public String generateCaptcha(HttpServletRequest request) {
        // 初始化极验服务
        GeetestLib lib = new GeetestLib(geetestConfig.getGeetest_id(), geetestConfig.getGeetest_key(), geetestConfig.isnewfailback());
        // 验证预处理
        int status = lib.preProcess(getCaptchaParams(request));
        // 将服务状态存放到 Session 中，在第二步验证时会用到
        request.getSession().setAttribute(lib.gtServerStatusSessionKey, status);
        // 返回生成字串
        return lib.getResponseStr();
    }

    @Override
    public boolean checkCaptcha(HttpServletRequest request) {
        // 初始化极验服务
        GeetestLib lib = new GeetestLib(geetestConfig.getGeetest_id(), geetestConfig.getGeetest_key(), geetestConfig.isnewfailback());
        // 接收前端参数，由前端 JS 内部封装处理
        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);
        // 取出第一步初始化验证时存储的服务状态
        int status = (Integer) request.getSession().getAttribute(lib.gtServerStatusSessionKey);
        int result = 0;
        if (status == 1) {
            // 服务器在线
            try {
                result = lib.enhencedValidateRequest(challenge, validate, seccode, getCaptchaParams(request));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // 服务器离线
            result = lib.failbackValidateRequest(challenge, validate, seccode);
        }
        if (result == 1) {
            return true;
        } else {
            throw new DefinitionException("行为验证失败，请检查使用环境");
        }
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

    public HashMap<String, String> getCaptchaParams(HttpServletRequest request) {
        String clientIp = AuthTools.getIpAddr(request);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", clientIp);
        params.put("client_type", "web");
        params.put("ip_address", clientIp);
        return params;
    }
}
