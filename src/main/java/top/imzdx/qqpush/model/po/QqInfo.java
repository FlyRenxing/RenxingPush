package top.imzdx.qqpush.model.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class QqInfo {
    @Schema(description = "QQ号")
    private Long number;
    @JsonIgnore
    @Schema(description = "密码")
    private String pwd;
    @Schema(description = "QQ昵称")
    private String name;
    @Schema(description = "在线状态")
    private Long state;
    @Schema(description = "备注")
    private String remarks;


}
