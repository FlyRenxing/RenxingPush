package top.imzdx.qqpush.model.po;

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
     * 时间
     */
    @CreatedDate
    @LastModifiedDate
    private LocalDateTime time;


}
