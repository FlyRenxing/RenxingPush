package top.imzdx.qqpush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.qqpush.config.GeetestConfig;
import top.imzdx.qqpush.dao.NoteDao;
import top.imzdx.qqpush.dao.QqInfoDao;
import top.imzdx.qqpush.model.po.Note;
import top.imzdx.qqpush.model.po.QqInfo;
import top.imzdx.qqpush.service.SystemService;
import top.imzdx.qqpush.utils.AuthTools;
import top.imzdx.qqpush.utils.DefinitionException;
import top.imzdx.qqpush.utils.GeetestLib;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Renxing
 */
@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    QqInfoDao qqInfoDao;
    @Autowired
    NoteDao noteDao;
    @Autowired
    GeetestConfig geetestConfig;

    @Override
    public List<QqInfo> getPublicQqBot() {
        return qqInfoDao.findAll();
    }

    @Override
    public List<Note> getAllNote() {
        return noteDao.findAllNote();
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

    public HashMap<String, String> getCaptchaParams(HttpServletRequest request) {
        String clientIp = AuthTools.getIpAddr(request);
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", clientIp);
        params.put("client_type", "web");
        params.put("ip_address", clientIp);
        return params;
    }
}
