package top.imzdx.renxingpush.model.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 一般包含在消息类中的元数据，比如消息类别等
 *
 * @author Renxing
 * @tag 消息元数据类
 */
@Data
@Accessors(chain = true)
public class MsgMeta implements Serializable {
    public static final String MSG_TYPE_QQ = "qq";
    public static final String MSG_TYPE_QQ_GROUP = "qq_group";
    public static final String MSG_TYPE_TELEGRAM = "telegram";
    /**
     * 消息类型，目前仅支持"qq"、"qq_group、telegram"
     *
     * @mock qq
     */
    @Pattern(regexp = "(^qq$|^qq_group$|^telegram$)", message = "type暂仅支持qq、qq_group、telegram")
    private String type;
    /**
     * 消息元数据，与type对应。
     * qq-QQ号，qq_group-QQ群号，telegram-telegramID
     *
     * @mock 1277489864
     */
    @NotNull(message = "消息元数据不能为空，一般填写收信人信息")
    private String data;
    /**
     * 指定QQ机器人号码
     * @mock 1277489864
     */
    private Long qqBot;

}
