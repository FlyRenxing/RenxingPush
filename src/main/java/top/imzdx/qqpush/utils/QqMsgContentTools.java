package top.imzdx.qqpush.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.dfa.WordTree;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Renxing
 */
@Component
public class QqMsgContentTools {
    WordTree badWord = new WordTree();

    public Message buildMessage(String content) {
        badWordDFA(content);
        //TODO 加入图片的消息链构建功能
        MessageChainBuilder chainBuilder = new MessageChainBuilder();
        buildFace(content, chainBuilder);
        return chainBuilder.build();
    }

    public QqMsgContentTools() throws IOException {

        Resource resource = new ClassPathResource("static/badWord.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        br.lines().forEach(s -> badWord.addWord(Base64Decoder.decodeStr(s)));
    }

    private void buildFace(String content, MessageChainBuilder chainBuilder) {
        //提取表情
        List<Message> faceList = new ArrayList<>();
        String regex = "@face=(.*?)@";
        Matcher matcher = Pattern.compile(regex).matcher(content);
        int matcher_start = 0;
        while (matcher.find(matcher_start)) {
            faceList.add(MiraiCode.deserializeMiraiCode("[mirai:face:" + matcher.group(1) + "]"));
            matcher_start = matcher.end();
        }
        //开始拼接
        String[] split = content.split(regex);
        int i = 0;
        //开头为表情的情况
        if (content.startsWith("@face=")) {
            i++;
            chainBuilder.append(faceList.get(0));
            faceList.remove(0);
        }
        //正常拼接
        while (i < split.length) {
            String s = split[i];
            //下一段文字为表情时会多出空字符串，故忽略他
            if (!"".equals(s)) {
                chainBuilder.append(s);
            }
            if (faceList.size() != 0) {
                chainBuilder.append(faceList.get(0));
                faceList.remove(0);
            }
            i++;
        }
        //结尾是表情的情况
        if (faceList.size() != 0) {
            for (Message o : faceList) {
                chainBuilder.append(o);
            }
        }
    }

    public void badWordDFA(String content) {
        List<String> matchAll = badWord.matchAll(content, -1, false, false);
        if (matchAll.size() != 0) {
            throw new DefinitionException("消息有敏感词，请检查后再试。提示：" + matchAll);
        }
    }

}
