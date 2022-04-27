package top.imzdx.qqpush.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
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
 * @author Renxing
 */
@RestController
@RequestMapping("/msg")
@Api(tags = "消息处理")
public class MsgController {
    MsgServiceContext msgServiceContext;
    UserDao userDao;

    @Autowired
    public MsgController(MsgServiceContext msgServiceContext, UserDao userDao) {
        this.msgServiceContext = msgServiceContext;
        this.userDao = userDao;
    }

    @PostMapping("/send/{cipher}")
    @GetMapping("/send/{cipher}")
    @Operation(summary = "发送消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cipher", value = "用户密钥", dataTypeClass = String.class),
            @ApiImplicitParam(name = "msg", value = "消息类", dataTypeClass = String.class)
    })
    public Result<Void> send(@PathVariable("cipher") String cipher, @RequestBody @Valid Msg msg) {
        Optional<User> user = userDao.findByCipher(cipher);
        if (user.isPresent()) {
            msgServiceContext.getMsgService(msg.getMeta().getType()).sendMsg(user.get(), msg);
            return new Result<>("ok", null);
        }
        throw new DefinitionException("Key错误");
    }
}
