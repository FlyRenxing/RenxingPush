package top.imzdx.renxingpush.model.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;
import net.mamoe.mirai.utils.BotConfiguration;

@Entity
@Table(name = "qq_info")
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class QQInfo {
    /**
     * QQ号
     *
     * @mock 1277489864
     */
    @Id
    private Long number;
    /**
     * qq密码
     *
     * @mock this_is_password
     */
    @JsonIgnore
    private String pwd;
    /**
     * 登陆协议
     * ANDROID_PHONE,0
     * ANDROID_PAD,1
     * ANDROID_WATCH,2
     * IPAD,3
     * MACOS,4
     *
     * @mock 2
     */
    private BotConfiguration.MiraiProtocol protocol;
    /**
     * 是否二维码登陆,目前仅支持macOS和AndroidPAD协议
     *
     * @mock true
     */
    private Boolean qrCodeLogin;
    /**
     * QQ昵称
     *
     * @mock 会飞的任性
     */
    private String name;
    /**
     * 在线状态
     *
     * @mock 1
     */
    private Integer state;
    /**
     * 备注
     *
     * @mock 我是备注
     */
    private String remarks;

}
