package top.imzdx.renxingpush.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.imzdx.renxingpush.interceptor.AdminRequired;
import top.imzdx.renxingpush.model.dto.Result;
import top.imzdx.renxingpush.utils.QQBotTools;

/**
 * 管理员相关
 *
 * @author Renxing
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    QQBotTools qqBotTools;

    public AdminController(QQBotTools qqBotTools) {
        this.qqBotTools = qqBotTools;
    }

    /**
     * 请求登录qq机器人
     *
     * @param qq qq号
     * @return 返回ok
     */
    @PostMapping("/remoteLoginQQBot")
    @AdminRequired
    public Result<Void> remoteLoginQQBot(Long qq) {
        qqBotTools.remoteLoginRequest(qq);
        return new Result<>("ok", null);
    }

    /**
     * 获取验证码url
     *
     * @param qq 在{@link #remoteLoginQQBot(Long)}中请求的qq号
     * @return 返回验证码url
     */
    @GetMapping("/getRemoteLoginQQBotUrl")
    @AdminRequired
    public Result<String> getRemoteLoginQQBotUrl(Long qq) {
        return new Result<>("ok", qqBotTools.reLoginSolverMap.get(qq).getUrl());
    }

    /**
     * 提交qq机器人登录验证码ticket
     *
     * @param qq     在{@link #remoteLoginQQBot(Long)}中请求的qq号
     * @param ticket 验证码ticket
     * @return 返回ok
     */
    @PostMapping("/submitTicket")
    @AdminRequired
    public Result<Void> submitTicket(Long qq, String ticket) {
        qqBotTools.setReLoginSolverCode(qq, ticket);
        return new Result<>("ok", null);
    }
}
