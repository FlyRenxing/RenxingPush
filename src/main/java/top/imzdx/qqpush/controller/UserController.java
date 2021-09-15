package top.imzdx.qqpush.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.imzdx.qqpush.dao.UserDao;
import top.imzdx.qqpush.interceptor.LoginRequired;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.service.UserService;
import top.imzdx.qqpush.utils.DefinitionException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author Renxing
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理")
public class UserController {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    @Operation(summary = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    public Result login(HttpServletRequest request,
                        @RequestParam @Valid @NotEmpty(message = "用户名不能为空") String name,
                        @RequestParam @Valid @NotEmpty(message = "用户名不能为空") String password) {
        User user = userService.findUserByName(name);
        if (user != null && user.getPassword().equals(password)) {
            request.getSession().setAttribute("user", user);
            System.out.println(user);
            return new Result("登陆成功", user);
        }
        throw new DefinitionException("账号或密码错误");
    }

    @PostMapping("/register")
    @Operation(summary = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名"),
            @ApiImplicitParam(name = "password", value = "密码")
    })
    public Result<User> register(HttpServletRequest request,
                                 @RequestParam @Valid @Length(min = 3, max = 20, message = "用户名长度需大于3,小于20") String name,
                                 @RequestParam @Valid @NotEmpty(message = "密码不能为空") String password) {
        if (userService.findUserByName(name) != null) {
            throw new DefinitionException("该用户名已被注册，请更换后重试");
        }
        if (userService.register(name, password)) {
            User user = userService.findUserByName(name);
            request.getSession().setAttribute("user", user);
            return new Result("注册成功", user);
        }
        throw new DefinitionException("注册异常");
    }

    @GetMapping("/refreshCipher")
    @LoginRequired
    @Operation(summary = "重置个人密钥")
    public Result<String> refreshCipher(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String cipher = userService.refreshCipher(user.getName());
        request.getSession().setAttribute("user", userService.findUserByName(user.getName()));
        return new Result("密钥刷新成功", cipher);
    }

    @GetMapping("/profile")
    @LoginRequired
    @Operation(summary = "获取个人资料")
    public Result<User> getProfile(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return new Result("ok", user);
        }
        throw new DefinitionException("当前未登录");
    }

    @PostMapping("/qq_bot")
    @LoginRequired
    @Operation(summary = "换绑QQ机器人")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "机器人号码")
    })
    public Result<User> updateQQBot(HttpServletRequest request,
                                    @RequestParam @Valid @Length(min = 6, message = "机器人号码长度需大于6") long number) {
        User user = (User) request.getSession().getAttribute("user");
        userService.setQQBot(user.getUid(), number);
        user = userService.findUserByName(user.getName());
        request.getSession().setAttribute("user", user);
        return new Result("ok", user);
    }
}
