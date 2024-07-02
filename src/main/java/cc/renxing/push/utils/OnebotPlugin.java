package cc.renxing.push.utils;

import cc.renxing.push.repository.QQGroupWhitelistDao;
import com.mikuac.shiro.annotation.FriendAddRequestHandler;
import com.mikuac.shiro.annotation.GroupAddRequestHandler;
import com.mikuac.shiro.annotation.common.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.request.FriendAddRequestEvent;
import com.mikuac.shiro.dto.event.request.GroupAddRequestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Shiro
@Component
public class OnebotPlugin {
    QQGroupWhitelistDao qqGroupWhitelistDao;

    @Autowired
    public OnebotPlugin(QQGroupWhitelistDao qqGroupWhitelistDao) {
        this.qqGroupWhitelistDao = qqGroupWhitelistDao;
    }

    @FriendAddRequestHandler
    public void friendAddRequestHandler(Bot bot, FriendAddRequestEvent event) {
        bot.setFriendAddRequest(event.getFlag(), true, "");
    }

    @GroupAddRequestHandler
    public void groupAddRequestHandler(Bot bot, GroupAddRequestEvent event) {
        if (!qqGroupWhitelistDao.findByNumber(event.getGroupId()).isEmpty()) {
            bot.setGroupAddRequest(event.getFlag(), event.getSubType(), true, "");
            String groupName = bot.getGroupInfo(event.getGroupId(), true).getData().getGroupName();
            qqGroupWhitelistDao.findByNumber(event.getGroupId()).forEach(item -> {
                item.setGroupName(groupName);
                qqGroupWhitelistDao.save(item);
            });
        } else {
            bot.setGroupAddRequest(event.getFlag(), event.getSubType(), false, "请先在任性推控制台申请添加白名单，通过后再次邀请");
        }
    }
}

