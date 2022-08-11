package top.imzdx.renxingpush.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.imzdx.renxingpush.config.AppConfig;
import top.imzdx.renxingpush.interceptor.LoginRequired;
import top.imzdx.renxingpush.model.dto.LoginDTO;
import top.imzdx.renxingpush.model.dto.Result;
import top.imzdx.renxingpush.model.dto.TelegramAuthenticationRequest;
import top.imzdx.renxingpush.model.po.MessageCallback;
import top.imzdx.renxingpush.model.po.QQGroupWhitelist;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.repository.UserDao;
import top.imzdx.renxingpush.service.SystemService;
import top.imzdx.renxingpush.service.UserService;
import top.imzdx.renxingpush.utils.AuthTools;
import top.imzdx.renxingpush.utils.DefinitionException;

import java.util.List;

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
     * 用户登录
     *
     * @param user 用户，包含用户名密码
     * @return
     * @ignoreParams request
     */
    @PostMapping("/login")
    public Result<User> login(
            @Valid LoginDTO user) {
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
     * telegram登录
     *
     * @param telegramUser telegram返回的user
     * @return 用户信息
     */
    @PostMapping("/telegramLogin")
    @CrossOrigin
    public Result<User> telegramLogin(@RequestBody TelegramAuthenticationRequest telegramUser) {
        if (!AuthTools.verifyAuth(telegramUser)) {
            throw new DefinitionException("Telegram验签失败");
        }
        User user = userService.telegramLogin(telegramUser);
        AuthTools.login(user.getUid());
        return new Result<>("登陆成功", user);
    }

    /**
     * telegram二维码登录
     */
    @PostMapping("/telegramQRCodeLogin")
    public Result<User> telegramQRCodeLogin(@RequestParam("code") String code) {
        return new Result<>("登录成功", userService.telegramQRCodeLogin(code));
    }

    /**
     * 注册
     *
     * @param name     用户名
     * @param password 密码
     * @return
     * @apiNote 当开启极验验证码时需附带lot_number、captcha_output、pass_token、gen_time参数
     */
    @PostMapping("/register")
    public Result<User> register(
            @RequestParam @Valid @Length(min = 3, max = 20, message = "用户名长度需大于3,小于20") String name,
            @RequestParam @Valid @NotEmpty(message = "密码不能为空") String password,
            @RequestParam(required = false) String lot_number,
            @RequestParam(required = false) String captcha_output,
            @RequestParam(required = false) String pass_token,
            @RequestParam(required = false) String gen_time) {
        if (geetestOpen && !systemService.checkCaptcha(lot_number, captcha_output, pass_token, gen_time)) {
            throw new DefinitionException("验证码错误");
        }
        User user = userService.register(new User().setName(name).setPassword(password));
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

    /**
     * 添加QQ群白名单
     *
     * @param number 群号码
     * @return
     */
    @PostMapping("qqGroupWhitelist")
    @LoginRequired
    public Result<QQGroupWhitelist> insertQQGroupWhitelist(@RequestParam @NotNull Long number) {
        QQGroupWhitelist qqGroupWhitelist = new QQGroupWhitelist();
        qqGroupWhitelist.setNumber(number);
        qqGroupWhitelist.setUserId(AuthTools.getUser().getUid());
        return new Result<>("ok", systemService.insertQQGroupWhitelist(qqGroupWhitelist));
    }

    /**
     * 获取QQ群白名单列表
     */
    @GetMapping("qqGroupWhitelist")
    @LoginRequired
    public Result<List<QQGroupWhitelist>> getQQGroupWhitelist() {
        return new Result<>("ok", systemService.getQQGroupWhitelist(AuthTools.getUser().getUid()));
    }

    /**
     * 删除QQ群白名单
     *
     * @param id 白名单id
     */
    @DeleteMapping("qqGroupWhitelist/{id}")
    @LoginRequired
    public Result<Boolean> deleteQQGroupWhitelist(@PathVariable Long id) {
        return new Result<>("ok", systemService.deleteQQGroupWhitelist(id));
    }

    /**
     * 添加消息回调
     */
    @PostMapping("/messageCallback")
    @LoginRequired
    public Result<MessageCallback> insertMessageCallback(@RequestBody MessageCallback messageCallback) {
        return new Result<>("ok", systemService.insertMessageCallback(messageCallback));
    }

    /**
     * 获取消息回调列表
     */
    @GetMapping("/messageCallback")
    @LoginRequired
    public Result<List<MessageCallback>> getMessageCallback() {
        return new Result<>("ok", systemService.getMessageCallback(AuthTools.getUser().getUid()));
    }

    /**
     * 删除消息回调
     *
     * @param id 消息回调id
     */
    @DeleteMapping("/messageCallback/{id}")
    @LoginRequired
    public Result<Boolean> deleteMessageCallback(@PathVariable Long id) {
        return new Result<>("ok", systemService.deleteMessageCallback(id));
    }

    /**
     * 退出账号
     */
    @GetMapping("/logout")
    @LoginRequired
    public Result<Boolean> logout() {
        AuthTools.logout();
        return new Result<>("ok", true);
    }
}
