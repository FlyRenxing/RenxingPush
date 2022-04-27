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
public class Note {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
  @Schema(description = "公告ID")
  private Long id;
  @Schema(description = "公告内容")
  private String main;
  @Schema(description = "公告在前端显示的颜色")
  private String color;

}
