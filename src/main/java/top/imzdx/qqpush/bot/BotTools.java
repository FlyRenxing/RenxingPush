package top.imzdx.qqpush.bot;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.imzdx.qqpush.dao.QqInfoDao;
import top.imzdx.qqpush.model.po.QqInfo;

import java.util.List;

/**
 * @author Renxing
 */
@Component
public class BotTools {
    @Autowired
    public BotTools(QqInfoDao qqInfoDao) {
        //读取数据库内账号
        List<QqInfo> qqInfoList = qqInfoDao.findAll();
        for (QqInfo item :
                qqInfoList) {
            BotFactory.INSTANCE.newBot(item.getNumber(), item.getPwd());
        }
        //所有账号登陆
        Bot.getInstancesSequence().iterator().forEachRemaining(Bot::login);

    }
}
