package cc.renxing.push.controller;

import cc.renxing.push.model.dto.Result;
import cc.renxing.push.model.po.Note;
import cc.renxing.push.model.po.QQInfo;
import cc.renxing.push.service.SystemService;
import cc.renxing.push.service.UserService;
import cc.renxing.push.utils.QQConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
