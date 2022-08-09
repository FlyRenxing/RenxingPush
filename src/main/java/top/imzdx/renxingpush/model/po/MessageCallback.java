package top.imzdx.renxingpush.model.po;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

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
     * 应用类型:qq/qq_group/telegram
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
     * 回调反馈
     *
     * @mock feedbackOK
     */
    @Transient
    @Length(max = 3000)
    private String feedback;
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
     * 原消息内容
     *
     * @mock 我是一条消息
     */
    @Transient
    private String message;
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

    public void setFeedback(String feedback) {
        if (feedback != null&&feedback.length() > 3000) {
            this.feedback = feedback.substring(0, 3000);
        } else {
            this.feedback = feedback;
        }
    }
}
