package top.imzdx.qqpush.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imzdx.qqpush.interceptor.LoginRequired;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.Note;
import top.imzdx.qqpush.model.po.QQGroupWhitelist;
import top.imzdx.qqpush.model.po.QQInfo;
import top.imzdx.qqpush.service.SystemService;
import top.imzdx.qqpush.service.UserService;
import top.imzdx.qqpush.utils.AuthTools;
import top.imzdx.qqpush.utils.QQConnection;

import java.util.List;

/**
 * 系统类
 *
 * @author Renxing
 */
@RestController
@RequestMapping("/sys")

public class SystemController {
    SystemService systemService;
    UserService userService;
    QQConnection qqConnection;

    @Autowired
    public SystemController(SystemService systemService, UserService userService, QQConnection qqConnection) {
        this.systemService = systemService;
        this.userService = userService;
        this.qqConnection = qqConnection;
    }

    /**
     * 获取机器人公开列表
     *
     * @return
     */
    @GetMapping("qqbotlist")
    public Result<List<QQInfo>> getQQBotPublicList() {
        return new Result<>("ok", systemService.getPublicQqBot());
    }

    /**
     * 获取所有公告
     *
     * @return
     */
    @GetMapping("note")
    public Result<List<Note>> getAllNote() {
        return new Result<>("ok", systemService.getAllNote());
    }

    /**
     * 生成Geetest极验验证码
     *
     * @return
     * @ignoreParams request
     */
    @GetMapping("geetest")
    public Result<String> generateCaptcha(HttpServletRequest request) {
        return new Result<>("ok", systemService.generateCaptcha(request));
    }

    /**
     * 获取QQ登录URL
     *
     * @return
     */
    @GetMapping("qqUrl")
    public Result<String> getQQUrl() {
        return new Result<>("ok", qqConnection.getUrl());
    }

    /**
     * 添加QQ群白名单
     *
     * @param number 群号码
     * @return
     */
    @PostMapping("qqGroupWhitelist")
    @LoginRequired
    public Result<QQGroupWhitelist> insertQQGroupWhitelist(Long number) {
        QQGroupWhitelist qqGroupWhitelist = new QQGroupWhitelist();
        qqGroupWhitelist.setNumber(number);
        qqGroupWhitelist.setUserId(AuthTools.getUser().getUid());
        return new Result<>("ok", systemService.insertQQGroupWhitelist(qqGroupWhitelist));
    }

}
