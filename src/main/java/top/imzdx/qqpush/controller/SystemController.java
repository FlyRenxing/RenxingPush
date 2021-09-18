package top.imzdx.qqpush.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.Note;
import top.imzdx.qqpush.model.po.QqInfo;
import top.imzdx.qqpush.service.SystemService;
import top.imzdx.qqpush.utils.QQConnection;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Renxing
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "系统类")
public class SystemController {
    @Autowired
    SystemService systemService;
    @Autowired
    QQConnection qqConnection;

    @GetMapping("qqbotlist")
    @Operation(summary = "获取机器人公开列表")
    public Result<List<QqInfo>> getQQBotPublicList() {
        return new Result("ok", systemService.getPublicQqBot());
    }

    @GetMapping("note")
    @Operation(summary = "获取所有公告")
    public Result<List<Note>> getAllNote() {
        return new Result("ok", systemService.getAllNote());
    }

    @GetMapping("geetest")
    @Operation(summary = "生成Geetest极验验证码")
    public Result<String> generateCaptcha(HttpServletRequest request) {
        return new Result<>("ok", systemService.generateCaptcha(request));
    }

    @GetMapping("qqUrl")
    @Operation(summary = "获取QQ登录URL")
    public Result<String> getQQUrl() {
        return new Result<>("ok", qqConnection.getUrl());
    }

}
