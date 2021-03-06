package top.imzdx.qqpush.utils;


import com.google.zxing.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
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
import top.imzdx.qqpush.config.AppConfig;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.repository.UserDao;
import top.imzdx.qqpush.service.UserService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Component
public class TelegramBot extends AbilityBot {
    AppConfig appConfig;

    UserService userService;

    UserDao userDao;

    public TelegramBot(DefaultBotOptions options, AppConfig appConfig, ApplicationContext appContext) {
        super(appConfig.getTelegram().getBotToken(), appConfig.getTelegram().getBotName(), options);
        this.appConfig = appConfig;
        this.userDao = appContext.getBean(UserDao.class);
        this.userService = appContext.getBean(UserService.class);
    }

    @Override
    public long creatorId() {
        return appConfig.getTelegram().getCreatorId();
    }

    public Reply qrcodeLogin() {
        // getChatId is a public utility function in rg.telegram.abilitybots.api.util.AbilityUtils
        BiConsumer<BaseAbilityBot, Update> action = (bot, update) -> {
            List<PhotoSize> photoSizeList = update.getMessage().getPhoto();
            PhotoSize photo = photoSizeList.stream().max(Comparator.comparingInt(PhotoSize::getFileSize)).orElseThrow(() -> new DefinitionException("??????????????????"));
            try (InputStream inputStream = downloadFileAsStream(getFilePath(photo))) {
                String result = ImageTools.parseQRCodeByFile(inputStream);
                if (!result.contains("RenxingPush-Login:")) {
                    silent.send("??????????????????????????????", getChatId(update));
                }
                var code = result.split(":")[1];
                User user = userService.putTelegramLoginCode(code, getChatId(update));
                silent.send("???????????????????????????" + user.getName(), getChatId(update));
            } catch (TelegramApiException | IOException e) {
                silent.send("????????????????????????????????????", getChatId(update));
            } catch (NotFoundException e) {
                silent.send("?????????????????????", getChatId(update));
            } catch (DefinitionException e) {
                silent.send(e.getMessage(), getChatId(update));
            }
        };

        return Reply.of(action, Flag.PHOTO);
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
                            default -> silent.send("?????????????????????~??????TelegramID??????" + ctx.chatId(), ctx.chatId());
                        }
                    } else {
                        silent.send("?????????????????????~??????TelegramID??????" + ctx.chatId(), ctx.chatId());
                    }
                })
                .build();
    }

    public Ability codeLogin() {
        return Ability
                .builder()
                .name("login")
                .info("??????????????????")
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
                .info("????????????")
                .input(1)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> bindUserForCipher(ctx, ctx.arguments()[0]))
                .build();
    }

    private void loginForCode(MessageContext ctx, String code) {
        try {
            User user = userService.putTelegramLoginCode(code, getChatId(ctx.update()));
            silent.send("???????????????????????????" + user.getName(), ctx.chatId());
        } catch (DefinitionException e) {
            silent.send(e.getMessage(), ctx.chatId());
        }
    }

    private void bindUserForCipher(MessageContext ctx, String cipher) {
        User user = userService.bindTelegramUser(this, ctx.update(), cipher);
        silent.send("???????????????????????????" + user.getName(), ctx.chatId());
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
