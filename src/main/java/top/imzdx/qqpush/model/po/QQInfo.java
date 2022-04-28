package top.imzdx.qqpush.model.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Table(name = "qq_info")
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class QQInfo {
    @Id
    @Schema(description = "QQ号")
    private Long number;
    @JsonIgnore
    @Schema(description = "密码")
    private String pwd;
    @Schema(description = "QQ昵称")
    private String name;
    @Schema(description = "在线状态")
    private Integer state;
    @Schema(description = "备注")
    private String remarks;

}
