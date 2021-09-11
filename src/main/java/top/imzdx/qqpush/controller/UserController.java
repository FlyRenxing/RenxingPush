package top.imzdx.qqpush.controller;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
public class UserController {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Result login(HttpServletRequest request,
                        @Valid @NotEmpty(message = "用户名不能为空") String name,
                        @Valid @NotEmpty(message = "用户名不能为空") String password) {
        User user = userService.findUserByName(name);
        if (user != null && user.getPassword().equals(password)) {
            request.getSession().setAttribute("user", user);
            System.out.println(user);
            return new Result("登陆成功", user);
        }
        throw new DefinitionException("账号或密码错误");
    }

    @PostMapping("/register")
    public Result register(HttpServletRequest request,
                           @Valid @Length(min = 3, message = "用户名长度需大于3") String name,
                           @Valid @NotEmpty(message = "密码不能为空") String password) {
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
    public Result refreshCipher(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String cipher = userService.refreshCipher(user.getName());
        request.getSession().setAttribute("user", userService.findUserByName(user.getName()));
        return new Result("密钥刷新成功", cipher);
    }

    @GetMapping("/profile")
    @LoginRequired
    public Result getProfile(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return new Result("ok", user);
        }
        throw new DefinitionException("当前未登录");
    }

    @PostMapping("/qq_bot")
    @LoginRequired
    public Result updateQQBot(HttpServletRequest request, @Valid @Length(min = 6, message = "机器人号码长度需大于6") long number) {
        User user = (User) request.getSession().getAttribute("user");
        userService.setQQBot(user.getUid(), number);
        user = userService.findUserByName(user.getName());
        request.getSession().setAttribute("user", user);
        return new Result("ok", user);
    }
}
