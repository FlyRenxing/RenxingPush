package cc.renxing.push.utils;

import cc.renxing.push.config.AppConfig;
import cc.renxing.push.model.po.MessageLog;
import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.dfa.WordTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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


}
