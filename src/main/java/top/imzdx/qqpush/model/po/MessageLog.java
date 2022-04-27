package top.imzdx.qqpush.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MessageLog {
  @Schema(description = "消息ID")
  private Long id;
  @Schema(description = "消息内容")
  private String content;
  @Schema(description = "消息元数据")
  private String meta;
  @Schema(description = "消息对应的用户ID")
  private Long uid;
  @Schema(description = "时间")
  private java.sql.Timestamp time;


}
