package top.imzdx.renxingpush.endpoint;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import top.imzdx.renxingpush.utils.QQBotTools;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

@ServerEndpoint("/RemoteQQLoginEndpoint/{qq}")
@Component
@CrossOrigin
public class RemoteQQLoginEndpoint {
    QQBotTools qqBotTools;
    Timer timerTask;

    public RemoteQQLoginEndpoint() {
        ApplicationContext context = SpringUtil.getApplicationContext();
        qqBotTools = context.getBean(QQBotTools.class);
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Long qq = Long.valueOf(session.getPathParameters().get("qq"));
        qqBotTools.remoteLoginRequest(qq);
        session.getBasicRemote().sendText("登录请求已提交，5秒后将返回验证码url或登陆二维码的base64编码");
        new Timer().schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                String url = qqBotTools.reLoginSolverMap.get(qq).getUrl();
                File qrcodeFile = qqBotTools.reLoginSolverMap.get(qq).getQrcodeFile();
                if (!url.isBlank()) {
                    session.getBasicRemote().sendText(url);
                } else if (qrcodeFile != null) {
                    session.getBasicRemote().sendText("data:image/png;base64," + Base64.getEncoder().encodeToString(FileUtil.readBytes(qrcodeFile)));
                    //后台定时检查qqBotTools.reLoginSolverMap.get(qq).getQrcodeNeedUpdate()，如果为true则重新发送
                    timerTask = new Timer();
                    timerTask.schedule(new TimerTask() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            if (qqBotTools.reLoginSolverMap.get(qq).getQrcodeNeedUpdate()) {
                                session.getBasicRemote().sendText("data:image/png;base64," + Base64.getEncoder().encodeToString(FileUtil.readBytes(qrcodeFile)));
                                qqBotTools.reLoginSolverMap.get(qq).setQrcodeNeedUpdate(false);
                            }
                        }
                    }, 5000, 5000);
                } else {
                    session.getBasicRemote().sendText("无验证码，可能登陆成功");
                }
            }
        }, 5000);
    }

    @OnClose
    public void onClose(Session session) {
        Long qq = Long.valueOf(session.getPathParameters().get("qq"));
        qqBotTools.reLoginSolverMap.remove(qq);
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    @OnMessage
    public void onMessage(String ticket, Session session) throws IOException {
        Long qq = Long.parseLong(session.getPathParameters().get("qq"));
        qqBotTools.setReLoginSolverCode(qq, ticket);
        session.getBasicRemote().sendText("ticket已提交");
    }

}
