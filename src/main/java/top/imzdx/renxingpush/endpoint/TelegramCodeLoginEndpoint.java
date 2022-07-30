package top.imzdx.renxingpush.endpoint;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.imzdx.renxingpush.service.UserService;
import top.imzdx.renxingpush.utils.DefinitionException;

import java.io.IOException;

import static top.imzdx.renxingpush.service.impl.UserServiceImpl.TELEGRAM_LOGIN_PERFIXT;

@ServerEndpoint("/telegramCodeLogin")
@Component
public class TelegramCodeLoginEndpoint {
    UserService userService;

    public TelegramCodeLoginEndpoint() {
        ApplicationContext context = SpringUtil.getApplicationContext();
        userService = context.getBean(UserService.class);
    }

    /**
     * 连接成功
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        int createCount = 0;
        while (createCount < 3) {
            var code = userService.getTelegramLoginCode();
            try {
                session.getBasicRemote().sendText(TELEGRAM_LOGIN_PERFIXT + code);
            } catch (IOException e) {
                throw new DefinitionException("获取code失败：ws连接失败");
            }
            long time = System.currentTimeMillis();
            boolean isLogin = false;
            boolean timeout = false;
            while (!isLogin && !timeout) {
                if (userService.hasTelegramLogin(code)) {
                    try {
                        session.getBasicRemote().sendText("登陆成功");
                    } catch (IOException e) {
                        throw new DefinitionException("ws发送失败");
                    }
                    isLogin = true;
                }
                if (System.currentTimeMillis() - time > 2 * 60 * 1000) {
                    timeout = true;
                    createCount++;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new DefinitionException("获取code失败：线程休眠失败");
                }
            }

        }
    }
}
