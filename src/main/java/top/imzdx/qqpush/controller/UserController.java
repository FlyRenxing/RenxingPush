package top.imzdx.qqpush.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.imzdx.qqpush.config.AppConfig;
import top.imzdx.qqpush.interceptor.LoginRequired;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.UserDao;
import top.imzdx.qqpush.service.SystemService;
import top.imzdx.qqpush.service.UserService;
import top.imzdx.qqpush.utils.AuthTools;
import top.imzdx.qqpush.utils.DefinitionException;

/**
 * 用户相关
 *
 * @author Renxing
 */
@RestController
@RequestMapping("/user")
public class UserController {
    UserDao userDao;
    UserService userService;
    SystemService systemService;

    boolean geetestOpen;


    @Autowired
    public UserController(UserDao userDao, UserService userService, SystemService systemService, AppConfig appConfig) {
        this.userDao = userDao;
        this.userService = userService;
        this.systemService = systemService;
        this.geetestOpen = appConfig.getGeetest().isEnabled();
    }

    /**
     * 用户管理
     *
     * @param user 用户，包含用户名密码
     * @return
     * @ignoreParams request
     */
    @PostMapping("/login")
    public Result<User> login(
            @RequestBody @Valid User user) {
        return new Result<>("登陆成功", userService.login(user.getName(), user.getPassword()));
    }

    /**
     * QQ登录回调
     *
     * @param code qq互联返回的code
     * @return
     * @ignore
     * @ignoreParams request
     * @ignoreParams response
     */
    @GetMapping("/qqLogin")
    @CrossOrigin
    public Result<User> qqLogin(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestParam("code") String code) {
        return new Result<>("登陆成功", userService.qqLogin(request, response, code));
    }

    /**
     * 注册
     *
     * @param name     用户名
     * @param password 密码
     * @return
     * @ignoreParams request
     * @apiNote 当开启极验验证码时需附带geetest_challenge，geetest_validate，geetest_seccode参数
     */
    @PostMapping("/register")
    public Result<User> register(HttpServletRequest request,
                                 @RequestParam @Valid @Length(min = 3, max = 20, message = "用户名长度需大于3,小于20") String name,
                                 @RequestParam @Valid @NotEmpty(message = "密码不能为空") String password) {
        if (geetestOpen) {
            if (!systemService.checkCaptcha(request)) {
                throw new DefinitionException("验证码错误");
            }
        }
        User user = userService.register(name, password);
        AuthTools.login(user.getUid());
        return new Result<>("注册成功", user);
    }

    /**
     * 重置个人密钥
     *
     * @return
     */
    @GetMapping("/refreshCipher")
    @LoginRequired
    public Result<String> refreshCipher() {
        User user = AuthTools.getUser();
        String cipher = userService.refreshCipher(user.getName());
        return new Result<>("密钥刷新成功", cipher);
    }

    /**
     * 获取个人资料
     *
     * @return
     */
    @GetMapping("/profile")
    @LoginRequired
    public Result<User> getProfile() {
        User user = AuthTools.getUser();
        if (user != null) {
            return new Result<>("ok", user);
        }
        throw new DefinitionException("当前未登录");
    }

    /**
     * 换绑QQ机器人
     *
     * @param number 机器人号码
     * @return
     */
    @PostMapping("/qq_bot")
    @LoginRequired
    public Result<User> updateQQBot(@RequestParam @Valid @Length(min = 6, message = "机器人号码长度需大于6") long number) {
        User user = AuthTools.getUser();
        userService.setQQBot(user, number);
        user = userService.findUserByName(user.getName());
        return new Result<>("ok", user);
    }

    /**
     * 获取当日用户使用次数
     *
     * @return
     */
    @GetMapping("/ToDayUseCount")
    @LoginRequired
    public Result<Integer> selectToDayUserUseCount() {
        User user = AuthTools.getUser();
        return new Result<>("ok", userService.selectToDayUserUseCount(user.getUid()));
    }

}
