package top.imzdx.qqpush.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Renxing
 * @description
 * @date 2021/4/15 14:25
 */
@Target({ElementType.METHOD})// 可用在类名上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface AdminRequired {

}
