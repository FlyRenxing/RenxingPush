package top.imzdx.qqpush.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.imzdx.qqpush.dao.UserDao;
import top.imzdx.qqpush.interceptor.LoginRequired;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.service.UserService;
import top.imzdx.qqpush.utils.DefinitionException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Renxing
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;

    @PostMapping("/login")
    public Result login(HttpServletRequest request, String name, String password) {
        User user = userService.findUserByName(name);
        if (user != null && user.getPassword().equals(password)) {
            request.getSession().setAttribute("user", user);
            System.out.println(user);
            return new Result("登陆成功", user);
        }
        throw new DefinitionException("账号或密码错误");
    }

    @PostMapping("/register")
    public Result register(HttpServletRequest request, String name, String password) {
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
        return new Result("密钥刷新成功", cipher);
    }
}
