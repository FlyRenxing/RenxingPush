package top.imzdx.qqpush.utils;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.dfa.WordTree;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.green.model.v20180509.ImageSyncScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import top.imzdx.qqpush.model.po.MessageLog;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Renxing
 */
@Component
public class MsgContentTools {
    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;
    WordTree badWord = new WordTree();

    public MsgContentTools() throws IOException {

        Resource resource = new ClassPathResource("static/badWord.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        br.lines().forEach(s -> badWord.addWord(Base64Decoder.decodeStr(s)));
    }

    public void badWordDFA(String content, MessageLog messageLog) {
        boolean isNumber = true;
        List<String> matchAll = badWord.matchAll(content, -1, false, false);
        for (String s : matchAll) {
            isNumber &= s.matches("\\d*");
        }
        if (matchAll.size() != 0 && !isNumber) {
            throw messageLog.fail("消息有敏感词，请检查后再试。提示：" + matchAll);
        }
    }

    public void checkImageForAliYun(String url) {
        //请替换成你自己的accessKeyId、accessKeySecret
        IClientProfile profile = DefaultProfile.getProfile("cn-beijing", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-beijing", "Green", "green.cn-beijing.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);

        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        imageSyncScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        imageSyncScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        imageSyncScanRequest.setEncoding("utf-8");
        imageSyncScanRequest.setRegionId("cn-beijing");

        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task = new LinkedHashMap<String, Object>();
        task.put("dataId", UUID.randomUUID().toString());
        task.put("url", url);
        task.put("time", new Date());

        tasks.add(task);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        /*
          porn: 色情
          terrorism: 暴恐
          qrcode: 二维码
          ad: 图片广告
          ocr: 文字识别
         */
        map.put("scenes", Arrays.asList("porn", "terrorism"));
        map.put("tasks", tasks);

        try {
            imageSyncScanRequest.setHttpContent(mapper.writeValueAsBytes(map), "UTF-8", FormatType.JSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        imageSyncScanRequest.setConnectTimeout(3000);
        imageSyncScanRequest.setReadTimeout(10000);

        try {
            HttpResponse httpResponse = client.doAction(imageSyncScanRequest);

            if (httpResponse.isSuccess()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode scrResponse = objectMapper.readTree(httpResponse.getHttpContent());//将Json串以树状结构读入内存
                System.out.println(scrResponse.asText());
                if (200 == scrResponse.get("code").asInt()) {
                    JsonNode taskResults = scrResponse.get("data");
                    for (JsonNode taskResult : taskResults) {
                        if (200 == taskResult.get("code").asInt()) {
                            JsonNode sceneResults = taskResult.get("results");
                            for (JsonNode sceneResult : sceneResults) {
                                String scene = sceneResult.get("scene").asText();
                                String suggestion = sceneResult.get("suggestion").asText();
                                if (!"pass".equals(suggestion)) {
                                    throw new DefinitionException("图片有敏感词，请检查后再试。提示：" + scene);
                                }
                            }
                        } else {
                            throw new DefinitionException("内容鉴别服务任务过程失败！code:" + taskResult.get("code").asInt());
                        }
                    }
                } else {
                    throw new DefinitionException("内容鉴别服务检测不成功！code:" + scrResponse.get("code").asInt());
                }
            } else {
                throw new DefinitionException("内容鉴别服务连接异常！status:" + httpResponse.getStatus());
            }
        } catch (ClientException | IOException e) {
            throw new DefinitionException("内容鉴别服务连接异常！", e);
        }
    }

    public Message buildQQMessage(String content, MessageLog messageLog, Contact contact) {
        badWordDFA(content, messageLog);
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
                            checkImageForAliYun(matcher.group(1));
                            chainBuilder.add(ExternalResource.uploadAsImage(getInputStreamByUrl(matcher.group(1)), contact));
                        } catch (IllegalArgumentException e) {
                            throw messageLog.fail("图片内容有误，支持格式为gif/png/bmp/jpg");
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

    private InputStream getInputStreamByUrl(String strUrl) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(conn.getInputStream(), output);
            return new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
