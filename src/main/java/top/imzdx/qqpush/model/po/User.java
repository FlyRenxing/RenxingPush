package top.imzdx.qqpush.model.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    @Schema(description = "用户ID")
    private Long uid;
    @Schema(description = "用户名")
    private String name;
    @JsonIgnore
    @Schema(description = "密码")
    private String password;
    @Schema(description = "是否为管理默认0否")
    private Long admin;
    @Schema(description = "用户配置")
    private String config;
    @Schema(description = "用户密钥")
    private String cipher;
    @Schema(description = "每日最大发送次数")
    private Long dayMaxSendCount;
    @JsonIgnore
    @Schema(description = "qq登录接口的openid")
    private String openid;


}
