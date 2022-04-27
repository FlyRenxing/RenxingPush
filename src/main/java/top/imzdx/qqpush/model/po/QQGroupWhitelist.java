package top.imzdx.qqpush.model.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QQGroupWhitelist {
  @Schema(description = "id(添加时不传)")
  private Long id;
  @Schema(description = "群号码")
  private Long number;
  @Schema(description = "绑定站内用户ID")
  private Long userId;


}
