package top.imzdx.qqpush.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Renxing
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "消息类", description = "定义了消息的数据结构")
public class Msg implements Serializable {
    @NotNull(message = "消息内容不能为空")
    @Schema(description = "消息内容")
    private String content;
    @Valid
    @Schema(description = "消息元数据")
    private MsgMeta meta;
}
