package top.imzdx.qqpush.service.impl;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imzdx.qqpush.model.dto.Msg;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.service.MsgService;
import top.imzdx.qqpush.utils.DefinitionException;
import top.imzdx.qqpush.utils.QqMsgContentTools;


/**
 * @author Renxing
 */
@Service("qq")
public class QQMsgServiceImpl implements MsgService {
    @Autowired
    QqMsgContentTools qqMsgContentTools;

    @Override
    public void sendMsg(User user, Msg msg) {
        System.out.println(user);
        try {
            long qq = Long.parseLong(msg.getMeta().getData());
            Friend friend = Bot.findInstance(JSONObject.parseObject(user.getConfig()).getLong("qq_bot"))
                    .getFriend(qq);
            if (friend != null) {
                friend.sendMessage(qqMsgContentTools.buildMessage(msg.getContent()));
                return;
            }
        } catch (NumberFormatException e) {
            throw new DefinitionException("收件号码不正确");
        }
        throw new DefinitionException("您没有添加指定机器人为好友");
    }
}
