package cc.renxing.push.utils;

import cc.renxing.push.model.po.QQInfo;
import cc.renxing.push.repository.QQInfoDao;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.CoreEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;

@Primary
@Component
public class OnebotCoreEvent extends CoreEvent {

    QQInfoDao qqInfoDao;

    @Autowired
    public OnebotCoreEvent(QQInfoDao QQInfoDao) {
        this.qqInfoDao = QQInfoDao;
        qqInfoDao.findAll().forEach(qqInfo -> {
            qqInfo.setState(0);
            qqInfoDao.save(qqInfo);
        });
    }


    @Override
    public void online(Bot bot) {
        // 客户端上线事件
        // 例如上线后发送消息给指定的群或好友
        // 如需获取上线的机器人账号可以调用 bot.getSelfId()
        System.out.println("诶～我上线啦" + bot.getSelfId());
        Optional<QQInfo> qqInfo = qqInfoDao.findByNumber(bot.getSelfId());
        if (qqInfo.isPresent()) {
            qqInfo.get().setState(1);
            qqInfo.get().setName(bot.getLoginInfo().getData().getNickname());
            qqInfoDao.save(qqInfo.get());
        } else {
            QQInfo qqInfo1 = new QQInfo();
            qqInfo1.setNumber(bot.getSelfId());
            qqInfo1.setState(1);
            qqInfoDao.save(qqInfo1);
        }

    }

    @Override
    public void offline(long account) {
        // 客户端离线事件
        System.out.println("诶～我又离线了" + account);
        qqInfoDao.findByNumber(account).ifPresent(qqInfo -> {
            qqInfo.setState(0);
            qqInfoDao.save(qqInfo);
        });

    }

    @Override
    public boolean session(WebSocketSession session) {
        // 可以通过 session.getHandshakeHeaders().getFirst("x-self-id") 获取上线的机器人账号
        // 例如当服务端为开放服务时，并且只有白名单内的账号才允许连接，此时可以检查账号是否存在于白名内
        // 不存在的话返回 false 即可禁止连接
        System.out.println("有新的连接：" + session.getId());
        String id = session.getHandshakeHeaders().getFirst("x-self-id");
        if (id != null) {
            //如果不存在返回false
            if (qqInfoDao.findByNumber(Long.parseLong(id)).isEmpty()) {
                System.out.println("拒绝连接：" + session.getId() + " 机器人账号：" + id);
                return false;
            }

        }

        return true;
    }

}

