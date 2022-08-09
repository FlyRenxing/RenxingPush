package top.imzdx.renxingpush.model.po;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import top.imzdx.renxingpush.repository.MessageCallbackLogDao;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class MessageCallbackLog {
    public static final boolean STATUS_FAIL = false;
    public static final boolean STATUS_SUCCESS = true;

    public static final String TYPE_QQ = "qq";
    public static final String TYPE_TELEGRAM = "telegram";
    public static final String FEEDBACK_OK = "ok";

    /**
     * 消息ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;
    /**
     * 消息内容
     *
     * @mock 这是消息内容
     */
    private String content;
    /**
     * 应用类型
     *
     * @mock qq
     */
    private String appType;
    /**
     * 消息对应的用户ID
     */
    private Long uid;
    /**
     * 消息状态
     */
    private boolean status = STATUS_SUCCESS;
    /**
     * 反馈消息
     */
    @Column(length = 3000)
    private String feedback;
    /**
     * 时间
     */
    @CreatedDate
    @LastModifiedDate
    private LocalDateTime time;

    public void success(){
        this.status = STATUS_SUCCESS;
        save();
    }

    public void fail() {
        this.status = STATUS_FAIL;
        save();
    }

    private void save(){
        MessageCallbackLogDao messageCallbackLogDao = SpringUtil.getBean(MessageCallbackLogDao.class);
        messageCallbackLogDao.save(this);
    }

    public MessageCallbackLog setFeedback(String feedback) {
        if (feedback!=null&& feedback.length()>3000){
            feedback = feedback.substring(0,3000);
        }
        this.feedback = feedback;
        return this;
    }
}
