package cc.renxing.push.model.po;

import cc.renxing.push.repository.MessageLogDao;
import cc.renxing.push.utils.DefinitionException;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class MessageLog {
    public static final boolean STATUS_FAIL = false;
    public static final boolean STATUS_SUCCESS = true;

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
    @Column(length = 3000)
    private String content;
    /**
     * 消息元数据
     *
     * @mock {"type": "qq","data": "1277489864"}
     */
    private String meta;
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
    private String feedback;
    /**
     * 时间
     */
    @CreatedDate
    @LastModifiedDate
    private LocalDateTime time;

    public DefinitionException fail(String feedback) {
        this.status = STATUS_FAIL;
        this.feedback = feedback;
        MessageLogDao messageLogDao = SpringUtil.getBean(MessageLogDao.class);
        messageLogDao.save(this);
        return new DefinitionException(feedback);
    }

}
