package top.imzdx.qqpush.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.imzdx.qqpush.interceptor.AdminRequired;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.Note;
import top.imzdx.qqpush.model.po.QQGroupWhitelist;
import top.imzdx.qqpush.model.po.QQInfo;
import top.imzdx.qqpush.service.SystemService;
import top.imzdx.qqpush.service.UserService;
import top.imzdx.qqpush.utils.DefinitionException;
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
     * @param request
     * @return
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
     * @param qqGroupWhitelist 一条白名单
     * @return
     */
    @PostMapping("qqGroupWhitelist")
    @AdminRequired
    public Result<QQGroupWhitelist> insertQQGroupWhitelist(@RequestBody QQGroupWhitelist qqGroupWhitelist) {
        if (userService.findUserById(qqGroupWhitelist.getUserId()) == null) {
            throw new DefinitionException("用户不存在");
        }
        return new Result<>("ok", systemService.insertQQGroupWhitelist(qqGroupWhitelist));
    }

}
