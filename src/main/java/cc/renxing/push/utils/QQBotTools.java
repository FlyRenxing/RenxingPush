package cc.renxing.push.utils;

import cc.renxing.push.model.dto.Msg;
import cc.renxing.push.model.po.MessageLog;
import cc.renxing.push.model.po.QQGroupWhitelist;
import cc.renxing.push.model.po.QQInfo;
import cc.renxing.push.model.po.User;
import cc.renxing.push.repository.QQGroupWhitelistDao;
import cc.renxing.push.repository.QQInfoDao;
import cc.renxing.push.repository.UserDao;
import cc.renxing.push.service.MsgService;
import com.mikuac.shiro.common.utils.ShiroUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.MsgId;
import com.mikuac.shiro.enums.MsgTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static cc.renxing.push.model.po.MessageCallbackLog.TYPE_QQ;

@Component
public class QQBotTools {
    MsgContentTools msgContentTools;
    QQGroupWhitelistDao qqGroupWhitelistDao;
    UserDao userDao;
    QQInfoDao qqInfoDao;
    BotContainer onebotContainer;
    ImageTools imageTools;

    @Autowired
    public QQBotTools(MsgContentTools msgContentTools, QQGroupWhitelistDao qqGroupWhitelistDao, UserDao userDao, QQInfoDao qqInfoDao, BotContainer onebotContainer, ImageTools imageTools) {
        this.msgContentTools = msgContentTools;
        this.qqGroupWhitelistDao = qqGroupWhitelistDao;
        this.userDao = userDao;
        this.qqInfoDao = qqInfoDao;
        this.onebotContainer = onebotContainer;
        this.imageTools = imageTools;
    }

    public void sendMsg(User user, Msg msg) {
        MessageLog messageLog = MsgService.saveMsgToDB(msg, user.getUid());
        riskControl(user, messageLog);
        StringBuilder textContent = new StringBuilder();
        ShiroUtils.rawToArrayMsg(msg.getContent()).stream().filter(msg1 -> msg1.getType() == MsgTypeEnum.text).forEach(msg1 -> {
            textContent.append(msg1.getData().get("text"));
        });
        msgContentTools.checkText(textContent.toString(), messageLog);
        cqcodeFilter(msg.getContent(), messageLog, user);
        boolean isFriend = msg.getMeta().getType().equals(TYPE_QQ);
        if (!isFriend) testGroupAuthority(user, Long.valueOf(msg.getMeta().getData()), messageLog);

        long qqBotId = msg.getMeta().getQqBot() != null ? msg.getMeta().getQqBot() : user.getConfig().getQqBot();
        Bot bot = onebotContainer.robots.get(qqBotId);
        if (bot == null) {
            Optional<QQInfo> firstByState = qqInfoDao.findFirstByState(1);
            if (firstByState.isEmpty()) {
                throw messageLog.fail("未找到可用的QQ机器人");
            }
            bot = onebotContainer.robots.get(firstByState.get().getNumber());
            userDao.save(user.setConfig(user.getConfig().setQqBot(firstByState.get().getNumber())));
            messageLog.setFeedback("已自动切换到可用QQ机器人:" + firstByState.get().getNumber());

        }
        ActionData<MsgId> result;
        if (isFriend) {
            result = bot.sendPrivateMsg(Long.parseLong(msg.getMeta().getData()), msg.getContent(), false);
        } else {
            result = bot.sendGroupMsg(Long.parseLong(msg.getMeta().getData()), msg.getContent(), false);
        }
        if (result.getRetCode() != 0) {
            throw messageLog.fail("发送消息失败,可能是收件人信息错误,错误代码:" + result.getRetCode());
        }

    }

    private void cqcodeFilter(String content, MessageLog messageLog, User user) {
//        if(user.getAdmin()==null||user.getAdmin()!=1){
        //不允许record video xml json cardimage
        if (content.contains("[CQ:record") || content.contains("[CQ:video") || content.contains("[CQ:xml") || content.contains("[CQ:json") || content.contains("[CQ:cardimage")) {
            throw messageLog.fail("不允许发送该CQ类型消息");
        }
        //提取所有图片，检测图片ImageTools isImageSafe()
        ShiroUtils.rawToArrayMsg(content).stream().filter(msg -> msg.getType() == MsgTypeEnum.image)
                .forEach(msg -> {
                    if (!imageTools.isImageSafe(msg.getData().get("file"))) {
                        throw messageLog.fail("图片检测不通过");
                    }
                });
//        }
    }

    private void riskControl(User user, MessageLog messageLog) {
        long dayMaxSendCount = user.getDayMaxSendCount();
        if (userDao.getTodayUseCount(user.getUid()) >= dayMaxSendCount) {
            throw messageLog.fail("您当日消息已达到" + dayMaxSendCount + "条，请明日再试。");
        }
//        System.out.println(userDao.getLastThreeSecondUseCount(user.getUid()));
        if (userDao.getLastThreeSecondUseCount(user.getUid()) >= 3) {
            throw messageLog.fail("发送消息过于频繁，请三秒后再试。");
        }
    }

    private void testGroupAuthority(User user, Long qqGroup, MessageLog messageLog) {
        List<QQGroupWhitelist> qqGroupWhitelist = qqGroupWhitelistDao.findByNumber(qqGroup);
        if (qqGroupWhitelist.isEmpty()) {
            throw messageLog.fail("该群不在白名单中");
        }
        qqGroupWhitelist.forEach(qqGroupWhitelist1 -> {
            if (!qqGroupWhitelist1.getUserId().equals(user.getUid())) {
                throw messageLog.fail("您没有权限发送该群消息");
            }
        });
    }

}
