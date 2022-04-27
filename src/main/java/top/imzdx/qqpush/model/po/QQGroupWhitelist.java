package top.imzdx.qqpush.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class QQGroupWhitelist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
  @Schema(description = "id(添加时不传)")
  private Long id;
  @Schema(description = "群号码")
  private Long number;
  @Schema(description = "绑定站内用户ID")
  private Long userId;


}
