package cc.renxing.push.utils;

import cc.renxing.push.config.AppConfig;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class AliyunLib {
    private final String accessKeyId;
    private final String accessKeySecret;

    @Autowired
    public AliyunLib(AppConfig appConfig) {
        this.accessKeyId = appConfig.getAliyun().getAccessKeyId();
        this.accessKeySecret = appConfig.getAliyun().getAccessKeySecret();
    }

    public void checkImage(String url) {
        //请替换成你自己的accessKeyId、accessKeySecret
        IClientProfile profile = DefaultProfile.getProfile("cn-beijing", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-beijing", "Green", "green.cn-beijing.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);

        ImageSyncScanRequest imageSyncScanRequest = new ImageSyncScanRequest();
        imageSyncScanRequest.setSysAcceptFormat(FormatType.JSON); // 指定api返回格式
        imageSyncScanRequest.setSysMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        imageSyncScanRequest.setSysEncoding("utf-8");
        imageSyncScanRequest.setSysRegionId("cn-beijing");

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
}
