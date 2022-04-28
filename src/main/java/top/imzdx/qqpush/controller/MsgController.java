package top.imzdx.qqpush.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.imzdx.qqpush.model.dto.Msg;
import top.imzdx.qqpush.model.dto.Result;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.UserDao;
import top.imzdx.qqpush.service.MsgServiceContext;
import top.imzdx.qqpush.utils.DefinitionException;

import java.util.Optional;


/**
 * 消息处理
 *
 * @author Renxing
 */
@RestController
@RequestMapping("/msg")
public class MsgController {
    MsgServiceContext msgServiceContext;
    UserDao userDao;

    @Autowired
    public MsgController(MsgServiceContext msgServiceContext, UserDao userDao) {
        this.msgServiceContext = msgServiceContext;
        this.userDao = userDao;
    }

    /**
     * 推送消息
     *
     * @param cipher 个人密钥|CH32p41OXu
     * @param msg    消息
     * @return 推送结果
     */
    @PostMapping("/send/{cipher}")
    public Result<Void> send(@PathVariable("cipher") String cipher, @RequestBody @Valid Msg msg) {
        Optional<User> user = userDao.findByCipher(cipher);
        if (user.isPresent()) {
            msgServiceContext.getMsgService(msg.getMeta().getType()).sendMsg(user.get(), msg);
            return new Result<>("ok", null);
        }
        throw new DefinitionException("Key错误");
    }
}
