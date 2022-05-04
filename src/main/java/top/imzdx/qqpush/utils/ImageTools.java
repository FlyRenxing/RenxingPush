package top.imzdx.qqpush.utils;

import cn.hutool.core.img.gif.GifDecoder;
import cn.hutool.core.io.resource.BytesResource;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImageTools {
    public static final String TYPE_JPG = "jpg";
    public static final String TYPE_GIF = "gif";
    public static final String TYPE_PNG = "png";
    public static final String TYPE_BMP = "bmp";
    public static final String TYPE_UNKNOWN = "unknown";
    private final boolean enableCheck;
    private final String useType;
    private final String imagePrivateCloudUrl;
    private AliyunLib aliyunLib;

    @Autowired
    public ImageTools(@Value("${app.system.checkImage.enabled}") boolean enableCheck,
                      @Value("${app.system.checkImage.useType}") String useType,
                      @Value("${app.system.checkImage.imagePrivateCloudUrl}") String imagePrivateCloudUrl,
                      AliyunLib aliyunLib) {
        this.enableCheck = enableCheck;
        this.useType = useType;
        this.imagePrivateCloudUrl = imagePrivateCloudUrl;
        this.aliyunLib = aliyunLib;
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 根据文件流判断图片类型
     *
     * @param fis
     * @return jpg/png/gif/bmp
     */
    public static String getPicType(InputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String type = bytesToHexString(b).toUpperCase();
            if (type.contains("FFD8FF")) {
                return TYPE_JPG;
            } else if (type.contains("89504E47")) {
                return TYPE_PNG;
            } else if (type.contains("47494638")) {
                return TYPE_GIF;
            } else if (type.contains("424D")) {
                return TYPE_BMP;
            } else {
                return TYPE_UNKNOWN;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void checkImageByPrivate(ByteArrayResource resource) throws DefinitionException {
        HttpRequest request = HttpRequest.post(imagePrivateCloudUrl)
                .timeout(20000);
        String picType;
        try {
            picType = getPicType(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            if (TYPE_GIF.equals(picType)) {
                List<ByteArrayResource> frames = gifDrawFrame(resource);
                MultiResource multiResource = new MultiResource();
                for (int i = 0; i < frames.size(); i++) {
                    ByteArrayResource o = frames.get(i);
                    multiResource.add(new BytesResource(o.getByteArray(), i + ".jpg"));
                }
                request.form("images", multiResource);
            } else {
                request.form("images", resource.getInputStream().readAllBytes(), "pic.jpg");
            }
            String responseStr = request.execute().body();
            ObjectMapper mapper = new ObjectMapper();
            List<PrivateImageCheckerResponse> response = mapper.readValue(responseStr, new TypeReference<>() {
            });
            response.stream().filter(PrivateImageCheckerResponse::isPorn).findFirst().ifPresent(r -> {
                throw new DefinitionException("图片审核不通过");
            });
        } catch (IOException e) {
            throw new DefinitionException("图片检测服务异常，请通知管理员或去除图片后发送。");
        }

    }

    public ByteArrayResource getImage(String url) throws DefinitionException {
        byte[] bytes = HttpUtil.downloadBytes(url);
        ByteArrayResource resource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return "image.jpg";
            }
        };
        if (enableCheck) {
            switch (useType) {
                case "aliyun" -> aliyunLib.checkImage(url);
                case "private" -> checkImageByPrivate(resource);
            }
        }
        return resource;
    }

    public List<ByteArrayResource> gifDrawFrame(ByteArrayResource resource) {
        List<ByteArrayResource> result = new ArrayList<>();
        try {
            GifDecoder gd = new GifDecoder();
            //要处理的图片
            int status = gd.read(resource.getInputStream());
            if (status != GifDecoder.STATUS_OK) {
                throw new RuntimeException("读取图片失败");
            }

            for (int i = 0; i < gd.getFrameCount(); i += gd.getFrameCount() / 3) {
                BufferedImage frame = gd.getFrame(i);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                int width = frame.getWidth();
                int height = frame.getHeight();
                BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
                int[] px = new int[width * height];
                frame.getRGB(0, 0, width, height, px, 0, width);
                output.setRGB(0, 0, width, height, px, 0, width);
                ImageIO.write(output, "jpg", outStream);
                result.add(new ByteArrayResource(outStream.toByteArray()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Data
    static class PrivateImageCheckerResponse {
        private Double sexy;
        private Double neutral;
        private Double porn;
        private Double drawing;
        private Double hentai;

        public boolean isPorn() {
            return porn > 0.5;
        }
    }

}
