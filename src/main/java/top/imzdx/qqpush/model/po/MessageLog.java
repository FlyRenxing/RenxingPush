package top.imzdx.qqpush.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class MessageLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
  @Schema(description = "消息ID")
  private Long id;
  @Schema(description = "消息内容")
  private String content;
  @Schema(description = "消息元数据")
  private String meta;
  @Schema(description = "消息对应的用户ID")
  private Long uid;
  @CreatedDate
  @LastModifiedDate
  @Schema(description = "时间")
  private LocalDateTime time;


}
