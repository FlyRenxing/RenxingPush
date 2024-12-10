package cc.renxing.push.utils;


import cc.renxing.push.config.AppConfig;
import cc.renxing.push.model.po.MessageCallback;
import cc.renxing.push.model.po.User;
import cc.renxing.push.repository.UserDao;
import cc.renxing.push.service.UserService;
import cc.renxing.push.service.impl.TelegramMsgCallbackServiceImpl;
import com.google.zxing.NotFoundException;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import static cc.renxing.push.model.po.MessageCallbackLog.TYPE_TELEGRAM;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

//@Component
public class TelegramBot extends AbilityBot {
    AppConfig appConfig;

    UserService userService;

    UserDao userDao;

    TelegramMsgCallbackServiceImpl telegramMsgCallbackService;

    public TelegramBot(DefaultBotOptions options, AppConfig appConfig, UserDao userDao, UserService userService, TelegramMsgCallbackServiceImpl telegramMsgCallbackService) {
        super(appConfig.getTelegram().getBotToken(), appConfig.getTelegram().getBotName(), options);
        this.appConfig = appConfig;
        this.userDao = userDao;
        this.userService = userService;
        this.telegramMsgCallbackService = telegramMsgCallbackService;
    }

    @Override
    public long creatorId() {
        return appConfig.getTelegram().getCreatorId();
    }

    public Reply qrcodeLogin() {
        // getChatId is a public utility function in rg.telegram.abilitybots.api.util.AbilityUtils
        BiConsumer<BaseAbilityBot, Update> action = (bot, update) -> {
            List<PhotoSize> photoSizeList = update.getMessage().getPhoto();
            PhotoSize photo = photoSizeList.stream().max(Comparator.comparingInt(PhotoSize::getFileSize)).orElseThrow(() -> new DefinitionException("图片下载失败"));
            try (InputStream inputStream = downloadFileAsStream(getFilePath(photo))) {
                String result = ImageTools.parseQRCodeByFile(inputStream);
                if (!result.contains("RenxingPush-Login:")) {
                    silent.send("此二维码非登录二维码", getChatId(update));
                }
                var code = result.split(":")[1];
                User user = userService.putTelegramLoginCode(code, getChatId(update));
                silent.send("认证成功！欢迎您，" + user.getName(), getChatId(update));
            } catch (TelegramApiException | IOException e) {
                silent.send("获取图片失败，请稍后重试", getChatId(update));
            } catch (NotFoundException e) {
                silent.send("未识别到二维码", getChatId(update));
            } catch (DefinitionException e) {
                silent.send(e.getMessage(), getChatId(update));
            }
        };
        return Reply.of(action, Flag.PHOTO);
    }

    public Ability callbackMessage() {
        return Ability
                .builder()
                .name("callback")
                .info("消息回调功能")
                .input(1)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    MessageCallback messageCallback = new MessageCallback()
                            .setAppType(TYPE_TELEGRAM)
                            .setSender(String.valueOf(ctx.user().getId()))
                            .setMessage(ctx.update().getMessage().getText());
                    String type = ctx.update().getMessage().getChat().getType();
                    if (type.equals("group") || type.equals("supergroup")) {
                        messageCallback.setGroup(String.valueOf(getChatId(ctx.update())));
                    }
                    telegramMsgCallbackService.haveNewMessage(messageCallback);
                })
                .build();
    }

    public Ability start() {
        return Ability
                .builder()
                .name("start")
                .info("says hello world!")
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    if (ctx.arguments().length > 0) {
                        switch (ctx.arguments()[0].split("-")[0]) {
                            case "login" -> loginForCode(ctx, ctx.arguments()[0].split("-")[1]);
                            case "bind" -> bindUserForCipher(ctx, ctx.arguments()[0].split("-")[1]);
                            default -> silent.send("欢迎关注任性推~您的TelegramID是：" + ctx.chatId(), ctx.chatId());
                        }
                    } else {
                        silent.send("欢迎关注任性推~您的TelegramID是：" + ctx.chatId(), ctx.chatId());
                    }
                })
                .build();
    }

    public Ability codeLogin() {
        return Ability
                .builder()
                .name("login")
                .info("接收代码登录")
                .input(1)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> loginForCode(ctx, ctx.arguments()[0]))
                .build();
    }

    public Ability bindUser() {
        return Ability
                .builder()
                .name("bind")
                .info("绑定用户")
                .input(1)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> bindUserForCipher(ctx, ctx.arguments()[0]))
                .build();
    }

    private void loginForCode(MessageContext ctx, String code) {
        try {
            User user = userService.putTelegramLoginCode(code, getChatId(ctx.update()));
            silent.send("认证成功！欢迎您，" + user.getName(), ctx.chatId());
        } catch (DefinitionException e) {
            silent.send(e.getMessage(), ctx.chatId());
        }
    }

    private void bindUserForCipher(MessageContext ctx, String cipher) {
        User user = userService.bindTelegramUser(this, ctx.update(), cipher);
        silent.send("绑定成功！欢迎您，" + user.getName(), ctx.chatId());
    }

    private String getFilePath(PhotoSize photo) {
        Objects.requireNonNull(photo);

        if (photo.getFilePath() != null) { // If the file_path is already present, we are done!
            return photo.getFilePath();
        } else { // If not, let find it
            // We create a GetFile method and set the file_id from the photo
            GetFile getFileMethod = new GetFile();
            getFileMethod.setFileId(photo.getFileId());
            try {
                // We execute the method using AbsSender::execute method.
                File file = execute(getFileMethod);
                // We now have the file_path
                return file.getFilePath();
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        return null; // Just in case
    }
}
