package top.imzdx.qqpush.model.po;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Table(indexes = {@Index(columnList = "keyword"), @Index(columnList = "sender")})
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class MessageCallback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;
    /**
     * 消息回调所属用户ID
     */
    private Long uid;
    /**
     * 应用类型
     *
     * @mock qq
     */
    private String appType;
    /**
     * 关键词
     *
     * @mock keyword
     */
    private String keyword;
    /**
     * 回调地址
     *
     * @mock https://www.baidu.com/callback
     */
    @SuppressWarnings("JavadocLinkAsPlainText")
    private String callbackURL;
    /**
     * 发件人
     *
     * @mock 1277489864
     */
    private String sender;
    /**
     * 所在群组
     *
     * @mock 1277489864
     */
    @Transient
    private String group;
    /**
     * 是否回应
     */
    private Boolean reply;
    /**
     * 回应语
     *
     * @mock 回调成功了
     */
    private String response;


}
