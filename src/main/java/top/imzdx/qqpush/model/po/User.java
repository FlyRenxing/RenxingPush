package top.imzdx.qqpush.model.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    /**
     * 用户ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long uid;
    /**
     * 用户名
     *
     * @mock 这是用户名
     */
    private String name;
    /**
     * 用户密码
     *
     * @mock this_id_password
     */
    @JsonIgnore
    private String password;
    /**
     * 是否为管理默认0否1是
     *
     * @mock 0
     */
    private Long admin;
    /**
     * 用户配置
     *
     * @mock 还没想好这里怎么用=.=
     */
    private String config;
    /**
     * 用户密钥
     *
     * @mock CH32p41OXu
     */
    private String cipher;
    /**
     * 每日最大发送次数
     *
     * @mock 200
     */
    private Long dayMaxSendCount;
    /**
     * qq登录接口的openid
     *
     * @mock 558B72975E9AB93FCEDE5C0500C9730F
     */
    @JsonIgnore
    private String openid;


}
