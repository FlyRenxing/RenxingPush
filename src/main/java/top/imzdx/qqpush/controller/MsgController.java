package top.imzdx.qqpush.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.imzdx.qqpush.dao.UserDao;
import top.imzdx.qqpush.model.dto.Msg;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.service.MsgServiceContext;
import top.imzdx.qqpush.utils.DefinitionException;

import javax.validation.Valid;

/**
 * @author Renxing
 */
@RestController
@CrossOrigin
@RequestMapping("/msg")
public class MsgController {
    @Autowired
    MsgServiceContext msgServiceContext;
    @Autowired
    UserDao userDao;

    @PostMapping("/send/{key}")
    public Result send(@PathVariable("key") String cipher, @RequestBody @Valid Msg msg) {
        User user = userDao.findUserByCipher(cipher);
        if (user != null) {
            msgServiceContext.getMsgService(msg.getMeta().getType()).sendMsg(user, msg);
            return new Result("ok", null);
        }
        throw new DefinitionException("Key错误");
    }
}
