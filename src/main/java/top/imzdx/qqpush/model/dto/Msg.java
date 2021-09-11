package top.imzdx.qqpush.model.dto;

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
public class Msg implements Serializable {
    @NotNull(message = "消息内容不能为空")
    private String content;
    @Valid
    private MsgMeta meta;
}
