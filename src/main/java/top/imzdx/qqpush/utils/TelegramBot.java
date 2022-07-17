package top.imzdx.qqpush.utils;


import com.google.zxing.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
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

import static org.telegram.abilitybots.api.objects.Flag.MESSAGE;
import static org.telegram.abilitybots.api.objects.Flag.REPLY;
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

    public Ability bindUser() {
        return Ability.builder().name("bind").info("绑定用户").privacy(PUBLIC).locality(USER).input(1).action(ctx -> silent.forceReply("123", ctx.chatId()))
                // The signature of a reply is -> (Consumer<Update> action, Predicate<Update>... conditions)
                // So, we  first declare the action that takes an update (NOT A MESSAGECONTEXT) like the action above
                // The reason of that is that a reply can be so versatile depending on the message, context becomes an inefficient wrapping
                .reply((bot, update) -> {
                    // Prints to console
                    System.out.println("I'm in a reply!");
                    // Sends message
                    silent.send("It's been nice playing with you!", update.getMessage().getChatId());
                }, MESSAGE, REPLY)
                // You can add more replies by calling .reply(...)
                .build();
    }

    public Reply qrcodeBind() {
        // getChatId is a public utility function in rg.telegram.abilitybots.api.util.AbilityUtils
        BiConsumer<BaseAbilityBot, Update> action = (bot, update) -> {
            List<PhotoSize> photoSizeList = update.getMessage().getPhoto();
            PhotoSize photo = photoSizeList.stream().max(Comparator.comparingInt(PhotoSize::getFileSize)).orElseThrow(() -> new DefinitionException("图片下载失败"));
            try (InputStream inputStream = downloadFileAsStream(getFilePath(photo))) {
                String result = ImageTools.parseQRCodeByFile(inputStream);
                User user = userService.bindTelegramUser(getChatId(update), result);
                if (Objects.equals(user.getTelegramId(), getChatId(update))) {
                    silent.send("绑定成功！", getChatId(update));
                }
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
