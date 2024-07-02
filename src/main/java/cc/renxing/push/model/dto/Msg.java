package cc.renxing.push.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 定义了消息的数据结构
 *
 * @author Renxing
 * @tag 消息类
 */
@Data
@Accessors(chain = true)
public class Msg implements Serializable {
    /**
     * 消息内容
     *
     * @mock 这是一条消息
     */
    @NotNull(message = "消息内容不能为空")
    @Length(max = 3000, message = "消息内容长度不能超过3000")
    private String content;
    /**
     * 消息元数据
     */
    @Valid
    private MsgMeta meta;

    public void setContent(String content) {
        if (content.length() > 3000) {
            content = content.substring(0, 3000);
        }
        this.content = content;
    }
}
