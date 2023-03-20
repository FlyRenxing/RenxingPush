package top.imzdx.renxingpush.model.po;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Set;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    /**
     * 是否为管理默认0否1是
     *
     * @mock 0
     */
    private Integer admin = 0;
    /**
     * 用户配置
     */
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private UserConfig config;
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
     * @mock 558B72975E9AB93FCEDE5C050569730F
     */
    @JsonIgnore
    private String openid;

    /**
     * telegramID
     *
     * @mock 123
     */
    @JsonIgnore
    private Long telegramId;

    /**
     * webauthn的id
     *
     * @mock PbIUaLwJZRZXPXPvFsbMlp6gkPbTtnwPeLnmndoabJDoqFq0nJudfprub8DLUXcwr+xlUBWFDdSObQX2AGhO4A==
     */
    @JsonIgnore
    private String webauthnHandle;

    /**
     * webauthn凭据
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<WebAuthNCredential> webAuthNCredentials;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class UserConfig {
        /**
         * 绑定的机器人QQ号
         *
         * @mock 2816669521
         */
        @Id
        @JsonAlias("qq_bot")
        private Long qqBot;
    }

}
