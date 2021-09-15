package top.imzdx.qqpush.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Note {
  @Schema(description = "公告ID")
  private long id;
  @Schema(description = "公告内容")
  private String main;
  @Schema(description = "公告在前端显示的颜色")
  private String color;

}
