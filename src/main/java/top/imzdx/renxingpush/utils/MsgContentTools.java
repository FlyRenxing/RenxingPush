package top.imzdx.renxingpush.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.dfa.WordTree;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import top.imzdx.renxingpush.config.AppConfig;
import top.imzdx.renxingpush.model.po.MessageLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Renxing
 */
@Component
public class MsgContentTools {
    ImageTools imageTools;
    WordTree badWord = new WordTree();
    BaiduLib baiduLib;
    boolean checkTextEnable;
    String checkTextUseType;

    @Autowired
    public MsgContentTools(ImageTools imageTools, BaiduLib baiduLib, AppConfig appConfig) throws IOException {
        this.imageTools = imageTools;
        this.baiduLib = baiduLib;
        this.checkTextEnable = appConfig.getSystem().getCheck().getText().isEnabled();
        this.checkTextUseType = appConfig.getSystem().getCheck().getText().getUseType();

        Resource resource = new ClassPathResource("static/badWord.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        br.lines().forEach(s -> badWord.addWord(Base64Decoder.decodeStr(s)));
    }

    public void checkText(String content, MessageLog messageLog) {
        if (!checkTextEnable) return;
        try {
            switch (checkTextUseType) {
                case "baidu" -> baiduLib.textCensorUserDefined(content);
                case "private" -> badWordDFA(content);
            }
        } catch (DefinitionException e) {
            throw messageLog.fail("内容审核失败。提示：" + e.getMessage());
        }
    }

    public void badWordDFA(String content) {
        boolean isNumber = true;
        List<String> matchAll = badWord.matchAll(content, -1, false, false);
        for (String s : matchAll) {
            s = s.replace(" ", "").replace(" ", "");
            isNumber &= s.matches("\\d*");
        }
        if (matchAll.size() != 0 && !isNumber) {
            throw new DefinitionException("消息有敏感词，请检查后再试。提示：" + matchAll);
        }
    }

    public Message buildQQMessage(String content, MessageLog messageLog, Contact contact) {
        checkText(content, messageLog);

        MessageChain chain = MiraiCode.deserializeMiraiCode(content);
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        for (int i = 0; i < chain.size(); i++) {
            SingleMessage singleMessage = chain.get(i);
            if (singleMessage instanceof LightApp || singleMessage instanceof SimpleServiceMessage || singleMessage instanceof FileMessage) {
                throw messageLog.fail("消息内容包含禁止的mirai消息类型");
            } else {
                if (singleMessage instanceof PlainText) {
                    String regex = "\\[mirai:image:(.*?)\\]";
                    Matcher matcher = Pattern.compile(regex).matcher(((PlainText) singleMessage).getContent());
                    if (matcher.find(0)) {
                        try {
                            String imageUrl = matcher.group(1);
                            ByteArrayResource image = imageTools.getImage(imageUrl);
                            chainBuilder.add(ExternalResource.uploadAsImage(image.getInputStream(), contact));
                        } catch (DefinitionException e) {
                            throw messageLog.fail(e.getMessage());
                        } catch (IllegalArgumentException e) {
                            throw messageLog.fail("图片内容有误，支持格式为gif/png/bmp/jpg");
                        } catch (IOException e) {
                            throw messageLog.fail("下载图片内容失败");
                        }
                    } else {
                        chainBuilder.add(singleMessage);
                    }
                } else {
                    chainBuilder.add(singleMessage);
                }
            }
        }
        return chainBuilder.build();
    }
}
