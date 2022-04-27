package top.imzdx.qqpush.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author Renxing
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "消息元数据类", description = "一般包含在消息类中的元数据，比如消息类别等")
public class MsgMeta implements Serializable {
    @Pattern(regexp = "((^qq$|^qq_group$))", message = "type暂仅支持qq、qq_group")
    @Schema(description = "消息类型，目前仅支持\"qq\"、\"qq_group\"")
    private String type;
    @NotNull(message = "消息元数据不能为空，一般填写收信人信息")
    @Schema(description = "消息元数据，与type对应，qq：QQ号，qq_group：QQ群号")
    private String data;

}
