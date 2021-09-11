package top.imzdx.qqpush.model.dto;

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
public class MsgMeta implements Serializable {
    @Pattern(regexp = "((^qq$|^telegram$))", message = "type暂仅支持qq、telegram")
    private String type;
    @NotNull(message = "消息元数据不能为空，一般填写收信人信息")
    private String data;

}
