package top.imzdx.qqpush.model.po;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@AllArgsConstructor // 自动所有参数的构造方法方法
@NoArgsConstructor // 自动无参的构造方法方法
@Builder
@Accessors(chain = true)
public class Note {
    /**
     * 公告ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;
    /**
     * 公告内容
     *
     * @mock 这是公告内容
     */
    private String main;
    /**
     * 公告在前端显示的颜色
     *
     * @mock #ff0000
     */
    private String color;

}
