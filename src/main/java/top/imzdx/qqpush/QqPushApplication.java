package top.imzdx.qqpush;

import cn.hutool.extra.spring.SpringUtil;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import top.imzdx.qqpush.dao.QqInfoDao;
import top.imzdx.qqpush.model.po.QqInfo;

import java.io.File;
import java.util.List;

@SpringBootApplication
public class QqPushApplication {

    public static void main(String[] args) {
        SpringApplication.run(QqPushApplication.class, args);
        botLogin();
    }

    public static void botLogin() {
        ApplicationContext appContext = SpringUtil.getApplicationContext();
        QqInfoDao qqInfoDao = (QqInfoDao) appContext.getBean("qqInfoDao");
        //所有账号登陆
        List<QqInfo> qqInfoList = qqInfoDao.findAll();
        for (QqInfo item :
                qqInfoList) {
            try {
                BotFactory.INSTANCE.newBot(item.getNumber(), item.getPwd(),
                        new BotConfiguration() {{
                            setCacheDir(new File("cache")); // 最终为 workingDir 目录中的 cache 目录
//                            setProtocol(MiraiProtocol.ANDROID_WATCH);
                            fileBasedDeviceInfo();
                        }}).login();
                qqInfoDao.updateState(item.getNumber(), 1);
            } catch (Exception e) {
                qqInfoDao.updateState(item.getNumber(), 0);
            }
        }
        /**
         * 修复当机器人账号设置为允许任何人添加好友时，
         * mirai的联系人缓存机制会导致Bot.getFriends()抛出异常无法找到新添加的好友。
         * 现使用好友监听功能，由机器人接管同意请求
         */
        Listener listener = GlobalEventChannel.INSTANCE.subscribeAlways(NewFriendRequestEvent.class, event -> {
            event.accept();
        });
        listener.start();
    }

}
