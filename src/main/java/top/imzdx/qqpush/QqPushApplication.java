package top.imzdx.qqpush;

import cn.hutool.extra.spring.SpringUtil;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import top.imzdx.qqpush.dao.QqInfoDao;
import top.imzdx.qqpush.model.po.QqInfo;

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
                            setProtocol(MiraiProtocol.ANDROID_WATCH);
                            fileBasedDeviceInfo();
                        }}).login();
                qqInfoDao.updateState(item.getNumber(), 1);
            } catch (Exception e) {
                qqInfoDao.updateState(item.getNumber(), 0);
            }
        }


    }

}
