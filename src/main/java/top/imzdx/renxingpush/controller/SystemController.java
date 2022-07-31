package top.imzdx.renxingpush.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imzdx.renxingpush.model.dto.Result;
import top.imzdx.renxingpush.model.po.Note;
import top.imzdx.renxingpush.model.po.QQInfo;
import top.imzdx.renxingpush.service.SystemService;
import top.imzdx.renxingpush.service.UserService;
import top.imzdx.renxingpush.utils.QQConnection;

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
     * @return 机器人公开列表
     */
    @GetMapping("qqbotlist")
    public Result<List<QQInfo>> getQQBotPublicList() {
        return new Result<>("ok", systemService.getPublicQqBot());
    }

    /**
     * 获取所有公告
     *
     * @return 所有公告
     */
    @GetMapping("note")
    public Result<List<Note>> getAllNote() {
        return new Result<>("ok", systemService.getAllNote());
    }

//    /**
//     * 生成Geetest极验验证码
//     *
//     * @return
//     * @ignoreParams request
//     */
//    @GetMapping("geetest")
//    public Result<String> generateCaptcha(HttpServletRequest request) {
//        return new Result<>("ok", systemService.generateCaptcha(request));
//    }

    /**
     * 获取QQ登录URL
     *
     * @return QQ登录URL
     */
    @GetMapping("qqUrl")
    public Result<String> getQQUrl() {
        return new Result<>("ok", qqConnection.getUrl());
    }

}
