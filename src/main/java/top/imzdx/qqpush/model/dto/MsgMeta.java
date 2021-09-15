package top.imzdx.qqpush.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Renxing
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "消息元数据类", description = "一般包含在消息类中的元数据，比如消息类别等")
public class MsgMeta implements Serializable {
    @Pattern(regexp = "((^qq$|^telegram$))", message = "type暂仅支持qq、telegram")
    @Schema(description = "消息类型，目前仅支持\"qq\"")
    private String type;
    @NotNull(message = "消息元数据不能为空，一般填写收信人信息")
    @Schema(description = "消息元数据，当type为qq时，此处为接收人qq号")
    private String data;

}
