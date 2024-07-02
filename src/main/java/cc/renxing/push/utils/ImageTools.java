package cc.renxing.push.utils;

import cc.renxing.push.config.AppConfig;
import cn.hutool.core.img.Img;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.img.gif.GifDecoder;
import cn.hutool.core.io.resource.BytesResource;
import cn.hutool.core.io.resource.MultiResource;
import cn.hutool.extra.qrcode.BufferedImageLuminanceSource;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
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
    private final AliyunLib aliyunLib;

    @Autowired
    public ImageTools(AppConfig appConfig, AliyunLib aliyunLib) {
        this.enableCheck = appConfig.getSystem().getCheck().getImage().isEnabled();
        this.useType = appConfig.getSystem().getCheck().getImage().getUseType();
        this.imagePrivateCloudUrl = appConfig.getSystem().getCheck().getImage().getPrivateCloudUrl();
        this.aliyunLib = aliyunLib;
    }

    public static String bytesToHexString(byte[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : src) {
            String hv = Integer.toHexString(b & 0xFF);
            if (hv.length() < 2) {
                stringBuilder.append('0');
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String getPicType(InputStream fis) {
        byte[] b = new byte[4];
        try (fis) {
            fis.read(b, 0, b.length);
            String type = bytesToHexString(b).toUpperCase();
            return switch (type) {
                case "FFD8FF" -> TYPE_JPG;
                case "89504E47" -> TYPE_PNG;
                case "47494638" -> TYPE_GIF;
                case "424D" -> TYPE_BMP;
                default -> TYPE_UNKNOWN;
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseQRCodeByFile(InputStream inputStream) throws IOException, NotFoundException {
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    public ByteArrayResource compressImage(ByteArrayResource byteArrayResource, int longestLength) throws IOException {
        BufferedImage image = ImgUtil.read(byteArrayResource.getInputStream());
        int width = image.getWidth();
        int height = image.getHeight();

        if (height > width && height > longestLength) {
            width = (int) (((double) width / height) * longestLength);
            height = longestLength;
        } else if (width > longestLength) {
            height = (int) (((double) height / width) * longestLength);
            width = longestLength;
        }

        Img img = Img.from(image);
        img.scale(width, height);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.write(byteArrayOutputStream);
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }

    public void checkImageByPrivate(ByteArrayResource resource) throws DefinitionException {
        HttpRequest request = HttpRequest.post(imagePrivateCloudUrl).timeout(20000);
        try {
            String picType = getPicType(resource.getInputStream());
            if (TYPE_GIF.equals(picType)) {
                List<ByteArrayResource> frames = gifDrawFrame(resource);
                MultiResource multiResource = new MultiResource();
                for (int i = 0; i < frames.size(); i++) {
                    multiResource.add(new BytesResource(frames.get(i).getByteArray(), i + ".jpg"));
                }
                request.form("images", multiResource);
            } else {
                request.form("images", resource.getInputStream().readAllBytes(), "pic.jpg");
            }

            String responseStr = request.execute().body();
            ObjectMapper mapper = new ObjectMapper();
            List<PrivateImageCheckerResponse> response = mapper.readValue(responseStr, new TypeReference<>() {
            });

            response.stream().filter(PrivateImageCheckerResponse::isNSFW).findFirst()
                    .ifPresent(r -> {
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
            try {
                if ("aliyun".equals(useType)) {
                    aliyunLib.checkImage(url);
                } else if ("private".equals(useType)) {
                    ByteArrayResource compressedResource = compressImage(resource, 600);
                    checkImageByPrivate(compressedResource);
                }
            } catch (IOException e) {
                throw new DefinitionException("图片处理失败");
            }
        }
        return resource;
    }

    public List<ByteArrayResource> gifDrawFrame(ByteArrayResource resource) {
        List<ByteArrayResource> result = new ArrayList<>();
        try {
            GifDecoder gd = new GifDecoder();
            int status = gd.read(resource.getInputStream());
            if (status != GifDecoder.STATUS_OK) {
                throw new RuntimeException("读取图片失败");
            }

            for (int i = 0; i < gd.getFrameCount(); i += gd.getFrameCount() / 3) {
                BufferedImage frame = gd.getFrame(i);
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                ImageIO.write(frame, "jpg", outStream);
                result.add(new ByteArrayResource(outStream.toByteArray()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean isImageSafe(String url) {
        if (!enableCheck) {
            return true; // 如果未启用检查，直接返回true
        }
        System.out.println("检查图片：" + url);
        try {
            if ("aliyun".equals(useType)) {
                aliyunLib.checkImage(url);
            } else if ("private".equals(useType)) {
                ByteArrayResource resource = getImage(url);
                ByteArrayResource compressedResource = compressImage(resource, 600);
                checkImageByPrivate(compressedResource);
            }
            return true; // 如果检查通过，返回true
        } catch (DefinitionException | IOException e) {
            return false; // 如果出现异常，返回false
        }
    }

    @Data
    static class PrivateImageCheckerResponse {
        private Double sexy;
        private Double neutral;
        private Double porn;
        private Double drawing;
        private Double hentai;

        public boolean isNSFW() {
            return sexy > 0.5 || porn > 0.5 || hentai > 0.5;
        }
    }
}
