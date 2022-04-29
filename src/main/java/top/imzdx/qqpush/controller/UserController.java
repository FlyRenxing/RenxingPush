package top.imzdx.qqpush.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.imzdx.qqpush.interceptor.LoginRequired;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.UserDao;
import top.imzdx.qqpush.service.SystemService;
import top.imzdx.qqpush.service.UserService;
import top.imzdx.qqpush.utils.AuthTools;
import top.imzdx.qqpush.utils.DefinitionException;
import top.imzdx.qqpush.utils.QQConnection;

import java.io.IOException;

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
    QQConnection qqConnection;
    boolean geetestOpen;
    String qqBackUrl;

    @Autowired
    public UserController(UserDao userDao, UserService userService, SystemService systemService, QQConnection qqConnection, @Value("${geetest.open}") boolean geetestOpen, @Value("${qq.back-url}") String qqBackUrl) {
        this.userDao = userDao;
        this.userService = userService;
        this.systemService = systemService;
        this.qqConnection = qqConnection;
        this.geetestOpen = geetestOpen;
        this.qqBackUrl = qqBackUrl;
    }

    /**
     * 用户管理
     *
     * @param name     用户名
     * @param password 密码
     * @return
     * @ignoreParams request
     */
    @PostMapping("/login")
    public Result<User> login(HttpServletRequest request,
                              @RequestParam @Valid @NotEmpty(message = "用户名不能为空") String name,
                              @RequestParam @Valid @NotEmpty(message = "用户名不能为空") String password) {
        User user = userService.findUserByName(name);
        if (user != null && user.getPassword().equals(password)) {
            request.getSession().setAttribute("uid", user.getUid());
            return new Result<>("登陆成功", user);
        }
        throw new DefinitionException("账号或密码错误");
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
//        第三步 获取access token
        String accessToken = qqConnection.getAccessToken(code);
//        第四步 获取登陆后返回的 openid、appid 以JSON对象形式返回
        ObjectNode userInfo = qqConnection.getUserOpenID(accessToken);
//        第五步获取用户有效(昵称、头像等）信息  以JSON对象形式返回
        String oauth_consumer_key = userInfo.get("client_id").asText();
        String openid = userInfo.get("openid").asText();
        ObjectNode userRealInfo = qqConnection.getUserInfo(accessToken, oauth_consumer_key, openid);

        User user = userService.findUserByOpenid(openid);
        if (user != null) {
            request.getSession().setAttribute("uid", user.getUid());
            try {
                response.sendRedirect(qqBackUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new Result<>("登陆成功", user);
        } else {
            String userName = userRealInfo.get("nickname").asText();
            String randomString = AuthTools.generateRandomString(6);
            do {
                userName = userName + "_" + randomString;
            } while (userService.findUserByName(userName) != null);

            if (userService.register(userName, randomString, openid)) {
                User newUser = userService.findUserByName(userName);
                request.getSession().setAttribute("uid", newUser.getUid());
                try {
                    response.sendRedirect(qqBackUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new Result<>("注册成功", newUser);
            }
        }
        throw new DefinitionException("QQ互联认证失败");
    }

    /**
     * 注册
     *
     * @ignoreParams request
     * @param name     用户名
     * @param password 密码
     * @return
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
        if (userService.findUserByName(name) != null) {
            throw new DefinitionException("该用户名已被注册，请更换后重试");
        }
        if (userService.register(name, password)) {
            User user = userService.findUserByName(name);
            request.getSession().setAttribute("uid", user.getUid());
            return new Result<>("注册成功", user);
        }
        throw new DefinitionException("注册异常");
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
