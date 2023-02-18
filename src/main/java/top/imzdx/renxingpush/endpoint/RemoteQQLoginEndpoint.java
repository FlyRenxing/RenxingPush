package top.imzdx.renxingpush.endpoint;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.imzdx.renxingpush.utils.QQBotTools;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

@ServerEndpoint("/RemoteQQLoginEndpoint/{qq}")
@Component
public class RemoteQQLoginEndpoint {
    QQBotTools qqBotTools;

    public RemoteQQLoginEndpoint() {
        ApplicationContext context = SpringUtil.getApplicationContext();
        qqBotTools = context.getBean(QQBotTools.class);
    }

    @OnOpen
    public void onOpen(Session session) throws IOException {
        Long qq = Long.valueOf(session.getPathParameters().get("qq"));
        qqBotTools.remoteLoginRequest(qq);
        session.getBasicRemote().sendText("登录请求已提交，5秒后将返回验证码url");
        new Timer().schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                String url = qqBotTools.reLoginSolverMap.get(qq).getUrl();
                if (!url.isBlank()) {
                    session.getBasicRemote().sendText(url);
                } else {
                    session.getBasicRemote().sendText("无验证码，可能登陆成功");
                }
            }
        }, 5000);
    }

    @OnMessage
    public void onMessage(String ticket, Session session) throws IOException {
        Long qq = Long.parseLong(session.getPathParameters().get("qq"));
        qqBotTools.setReLoginSolverCode(qq, ticket);
        session.getBasicRemote().sendText("ticket已提交");
    }

}
