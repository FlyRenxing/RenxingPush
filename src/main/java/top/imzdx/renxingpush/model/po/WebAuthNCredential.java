package top.imzdx.renxingpush.model.po;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * WebAuthN凭据
 */
@Entity
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@EqualsAndHashCode(of = "keyId")
@Accessors(chain = true)
public class WebAuthNCredential {
    /**
     * 用户
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
//可选属性optional=false,表示author不能为空。删除文章，不影响用户
    @JoinColumn(name = "uid")//设置在article表中的关联字段(外键)
    private User user;
    /**
     * 密钥ID
     */
    @Id
    private String keyId;
    /**
     * 是否可用于无用户名？？
     */
    private Boolean discoverable;
    /**
     * 签名计数
     */
    private Long signatureCount;
    /**
     * 公钥，长度最多1024
     */
    @Column(length = 1024)
    private String publicKeyCose;
    /**
     * 证明对象，长度最多1024
     */
    @Column(length = 1024)
    private String attestationObject;
    /**
     * 客户端数据，长度最多1024
     */
    @Column(length = 1024)
    private String clientDataJSON;
}