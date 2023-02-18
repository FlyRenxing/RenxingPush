package top.imzdx.renxingpush.utils;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.imzdx.renxingpush.model.dto.Msg;
import top.imzdx.renxingpush.model.po.MessageLog;
import top.imzdx.renxingpush.model.po.QQGroupWhitelist;
import top.imzdx.renxingpush.model.po.QQInfo;
import top.imzdx.renxingpush.model.po.User;
import top.imzdx.renxingpush.repository.QQGroupWhitelistDao;
import top.imzdx.renxingpush.repository.QQInfoDao;
import top.imzdx.renxingpush.repository.UserDao;

import java.io.File;
import java.util.*;

import static top.imzdx.renxingpush.model.po.MessageCallbackLog.TYPE_QQ;
import static top.imzdx.renxingpush.service.MsgService.saveMsgToDB;

@Component
public class QQBotTools {
    MsgContentTools msgContentTools;
    QQGroupWhitelistDao qqGroupWhitelistDao;
    UserDao userDao;
    QQInfoDao qqInfoDao;

    public Map<Long, ReLoginSolver> reLoginSolverMap = new HashMap<>();

    @Autowired
    public QQBotTools(MsgContentTools msgContentTools, QQGroupWhitelistDao qqGroupWhitelistDao, UserDao userDao, QQInfoDao qqInfoDao) {
        this.msgContentTools = msgContentTools;
        this.qqGroupWhitelistDao = qqGroupWhitelistDao;
        this.userDao = userDao;
        this.qqInfoDao = qqInfoDao;
    }

    public Friend findFriend(Long qqBotNumber, Long qqNumber) throws NullQQBotException, NullQQFriendException {
        Bot bot = Bot.findInstance(qqBotNumber);
        if (bot == null) throw new NullQQBotException("机器人不存在");
        Friend friend = bot.getFriend(qqNumber);
        if (friend == null) throw new NullQQBotException("好友不存在");
        return friend;
    }

    public Group findGroup(Long qqBotNumber, Long qqNumber) throws NullQQBotException, NullQQGroupException {
        Bot bot = Bot.findInstance(qqBotNumber);
        if (bot == null) throw new NullQQBotException("机器人不存在");
        Group group = bot.getGroup(qqNumber);
        if (group == null) throw new NullQQGroupException("群不存在");
        return group;
    }

    public void sendMsg(User user, Msg msg) {
        MessageLog messageLog = saveMsgToDB(msg, user.getUid());
        boolean isFriend = msg.getMeta().getType().equals(TYPE_QQ);
        riskControl(user, messageLog);
        if (!isFriend) testGroupAuthority(user, Long.valueOf(msg.getMeta().getData()), messageLog);

        long qqBot = msg.getMeta().getQqBot() != null ? msg.getMeta().getQqBot() : user.getConfig().getQqBot();
        try {
            Contact contact = isFriend ?
                    findFriend(qqBot, Long.valueOf(msg.getMeta().getData())) : findGroup(qqBot, Long.valueOf(msg.getMeta().getData()));
            if (contact != null) {
                contact.sendMessage(msgContentTools.buildQQMessage(msg.getContent(), messageLog, contact));
                return;
            }
        } catch (NumberFormatException e) {
            throw messageLog.fail("收信群号码不正确");
        } catch (NullQQBotException e) {
            qqInfoDao.findByNumber(qqBot).ifPresent(qqInfo -> {
                qqInfo.setState(0);
                qqInfoDao.save(qqInfo);
            });
            throw messageLog.fail("所选QQ机器人已失效，请前往官网重新设定默认机器人或附带qqBot参数指定机器人。当前所选机器人号码：" + qqBot);
        } catch (NullQQGroupException e) {
            throw messageLog.fail("收信群号码不正确或所选机器人未进群聊");
        } catch (NullQQFriendException e) {
            throw messageLog.fail("收信好友不存在");
        }
        throw messageLog.fail("该机器人离线或您没有将该机器人拉入目标qq群（先加好友然后拉群）。当前所选机器人号码：" + qqBot);
    }

    private void riskControl(User user, MessageLog messageLog) {
        long dayMaxSendCount = user.getDayMaxSendCount();
        if (userDao.getTodayUseCount(user.getUid()) >= dayMaxSendCount) {
            throw messageLog.fail("您当日消息已达到" + dayMaxSendCount + "条，请明日再试。");
        }
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

    public void remoteLoginRequest(Long qq) {
        QQInfo qqInfo = qqInfoDao.findByNumber(qq).orElseThrow(() -> new DefinitionException("机器人不存在"));
        ReLoginSolver solver = new ReLoginSolver();
        reLoginSolverMap.put(qq, solver);
        //创建一个用于执行任务的线程
        Thread thread = new Thread(() -> {
            BotFactory.INSTANCE.newBot(qqInfo.getNumber(), qqInfo.getPwd(),
                    new BotConfiguration() {{
                        setLoginSolver(solver);
                        setCacheDir(new File("cache")); // 最终为 workingDir 目录中的 cache 目录
                        setProtocol(MiraiProtocol.ANDROID_PAD);
                        fileBasedDeviceInfo();
                    }}).login();
        });
        thread.start();
        //十分钟后自动关闭
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                thread.stop();
            }
        }, 10 * 60 * 1000);

    }

    public void setReLoginSolverCode(Long qq, String code) {
        ReLoginSolver solver = reLoginSolverMap.get(qq);
        if (solver != null) {
            solver.getVerificationResult().complete(code);
        } else {
            throw new DefinitionException("机器人不存在");
        }
    }

    public static class NullQQBotException extends DefinitionException {
        public NullQQBotException(String msg) {
            super(msg);
        }
    }

    public static class NullQQFriendException extends DefinitionException {
        public NullQQFriendException(String msg) {
            super(msg);
        }
    }

    public static class NullQQGroupException extends DefinitionException {
        public NullQQGroupException(String msg) {
            super(msg);
        }
    }
//    class ReLoginSolver extends LoginSolver{
//
//        @Nullable
//        @Override
//        public Object onSolvePicCaptcha(@NotNull Bot bot, @NotNull byte[] bytes, @NotNull Continuation<? super String> continuation) {
//            return null;
//        }
//
//        @Nullable
//        @Override
//        public Object onSolveSliderCaptcha(@NotNull Bot bot, @NotNull String s, @NotNull Continuation<? super String> continuation) {
//            System.out.println("url:"+s);
//            continuation.resumeWith(Result.success("123"));
//            return null;
//        }
//    }
}
