package top.imzdx.qqpush.model.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class User implements Serializable {

    @Schema(description = "用户ID")
    private long uid;
    @Schema(description = "用户名")
    private String name;
    @JsonIgnore
    @Schema(description = "密码")
    private String password;
    @Schema(description = "是否为管理默认0否")
    private long admin;
    @Schema(description = "用户配置")
    private String config;
    @Schema(description = "用户密钥")
    private String cipher;
    @Schema(description = "每日最大发送次数")
    private long dayMaxSendCount;
    @JsonIgnore
    @Schema(description = "qq登录接口的openid")
    private String openid;


}
