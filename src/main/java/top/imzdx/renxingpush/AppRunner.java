package top.imzdx.renxingpush;

import cn.hutool.extra.spring.SpringUtil;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.BotJoinGroupEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import top.imzdx.renxingpush.config.AppConfig;
import top.imzdx.renxingpush.model.po.QQInfo;
import top.imzdx.renxingpush.repository.QQGroupWhitelistDao;
import top.imzdx.renxingpush.repository.QQInfoDao;
import top.imzdx.renxingpush.utils.TelegramBot;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author Renxing
 */
@Component
public class AppRunner implements ApplicationRunner {
    public static ApplicationContext appContext = SpringUtil.getApplicationContext();
    static TelegramBot telegramBot;

    @Autowired
    public AppRunner(TelegramBot telegramBot) {
        AppRunner.telegramBot = telegramBot;
    }

    public static void qqInit() {

        QQInfoDao qqInfoDao = appContext.getBean(QQInfoDao.class);
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
        /*
          修复当机器人账号设置为允许任何人添加好友时，
          mirai的联系人缓存机制会导致Bot.getFriends()抛出异常无法找到新添加的好友。
          现使用好友监听功能，由机器人接管同意请求
         */
        Listener<NewFriendRequestEvent> qqListener = GlobalEventChannel.INSTANCE.subscribeAlways(NewFriendRequestEvent.class, NewFriendRequestEvent::accept);
        qqListener.start();
        QQGroupWhitelistDao qqGroupWhitelistDao = appContext.getBean(QQGroupWhitelistDao.class);
        //邀请加群监听
        Listener<BotInvitedJoinGroupRequestEvent> qqGroupListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotInvitedJoinGroupRequestEvent.class, event -> {
            if (!qqGroupWhitelistDao.findByNumber(event.getGroupId()).isEmpty()) {
                String groupName = event.getGroupName();
                qqGroupWhitelistDao.findByNumber(event.getGroupId()).forEach(item -> {
                    item.setGroupName(groupName);
                    qqGroupWhitelistDao.save(item);
                });
                event.accept();
            } else {
                event.ignore();
                Objects.requireNonNull(event.getInvitor()).sendMessage("您好，请先在任性推控制台内申请添加白名单，通过后再次邀请。");
            }
        });
        qqGroupListener.start();
        //小群自动同意时的更新群名
        Listener<BotJoinGroupEvent> botJoinGroupEventListener = GlobalEventChannel.INSTANCE.subscribeAlways(BotJoinGroupEvent.class, event -> {
            if (!qqGroupWhitelistDao.findByNumber(event.getGroupId()).isEmpty()) {
                String groupName = event.getGroup().getName();
                qqGroupWhitelistDao.findByNumber(event.getGroupId()).forEach(item -> {
                    item.setGroupName(groupName);
                    qqGroupWhitelistDao.save(item);
                });
            }
        });
        botJoinGroupEventListener.start();
    }

    public static void telegramInit() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run(ApplicationArguments args) {
        AppConfig appConfig = appContext.getBean(AppConfig.class);
        if (appConfig.getSystem().isOpenQqMsg()) qqInit();
        if (appConfig.getSystem().isOpenTelegramMsg()) telegramInit();
    }

    public static TelegramBot getTelegramBot() {
        return telegramBot;
    }
}
