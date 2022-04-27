package top.imzdx.qqpush.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.imzdx.qqpush.interceptor.AdminRequired;
import top.imzdx.qqpush.interceptor.LoginRequired;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.Note;
import top.imzdx.qqpush.model.po.QQGroupWhitelist;
import top.imzdx.qqpush.model.po.QqInfo;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.service.SystemService;
import top.imzdx.qqpush.service.UserService;
import top.imzdx.qqpush.utils.AuthTools;
import top.imzdx.qqpush.utils.DefinitionException;
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
    SystemService systemService;
    UserService userService;
    QQConnection qqConnection;

    @Autowired
    public SystemController(SystemService systemService,UserService userService, QQConnection qqConnection) {
        this.systemService = systemService;
        this.userService = userService;
        this.qqConnection = qqConnection;
    }

    @GetMapping("qqbotlist")
    @Operation(summary = "获取机器人公开列表")
    public Result<List<QqInfo>> getQQBotPublicList() {
        return new Result<>("ok", systemService.getPublicQqBot());
    }

    @GetMapping("note")
    @Operation(summary = "获取所有公告")
    public Result<List<Note>> getAllNote() {
        return new Result<>("ok", systemService.getAllNote());
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

    @PostMapping("qqGroupWhitelist")
    @AdminRequired
    @Operation(summary = "添加QQ群白名单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qqGroupWhitelist", value = "一条白名单", dataTypeClass = QQGroupWhitelist.class)
    })
    public Result<Boolean> insertQQGroupWhitelist(@RequestBody QQGroupWhitelist qqGroupWhitelist) {
        if (userService.findUserById(qqGroupWhitelist.getUserId()) == null) {
            throw new DefinitionException("用户不存在");
        }
        return new Result<>("ok", systemService.insertQQGroupWhitelist(qqGroupWhitelist));
    }

}
