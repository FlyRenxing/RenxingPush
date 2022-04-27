package top.imzdx.qqpush;

import cn.hutool.extra.spring.SpringUtil;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.imzdx.qqpush.model.po.QQInfo;
import top.imzdx.qqpush.repository.QQInfoDao;

import java.io.File;
import java.util.List;

/**
 * @author Renxing
 */
@Component
public class AppRunner implements ApplicationRunner {
    public static void qqInit() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        QQInfoDao qqInfoDao = (QQInfoDao) appContext.getBean("QQInfoDao");
        //所有账号登陆
        List<QQInfo> QQInfoList = qqInfoDao.findAll();
        for (QQInfo item :
                QQInfoList) {
            try {
                BotFactory.INSTANCE.newBot(item.getNumber(), item.getPwd(),
                        new BotConfiguration() {{
                            setCacheDir(new File("cache")); // 最终为 workingDir 目录中的 cache 目录
//                            setProtocol(MiraiProtocol.ANDROID_WATCH);
                            fileBasedDeviceInfo();
                        }}).login();
                item.setState(1);
            } catch (Exception e) {
                item.setState(0);
            }
            qqInfoDao.save(item);
        }
        /**
         * 修复当机器人账号设置为允许任何人添加好友时，
         * mirai的联系人缓存机制会导致Bot.getFriends()抛出异常无法找到新添加的好友。
         * 现使用好友监听功能，由机器人接管同意请求
         */
        Listener<NewFriendRequestEvent> qqListener = GlobalEventChannel.INSTANCE.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);
        qqListener.start();

        Listener<BotInvitedJoinGroupRequestEvent> qqGroupListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotInvitedJoinGroupRequestEvent.class, BotInvitedJoinGroupRequestEvent::accept);
        qqGroupListener.start();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        qqInit();
    }
}
