package top.imzdx.qqpush.model.po;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Table(name = "qq_group_whitelist")
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class QQGroupWhitelist {
    /**
     * id(添加时不传)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;
    /**
     * 群号码
     *
     * @mock 807374699
     */
    private Long number;
    /**
     * 绑定站内用户ID
     */
    private Long userId;


}
