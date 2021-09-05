package top.imzdx.qqpush.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * @author Renxing
 */
@Data
@Accessors(chain = true)
public class Msg implements Serializable {
    private String content;
    @Valid
    private MsgMeta meta;
}
