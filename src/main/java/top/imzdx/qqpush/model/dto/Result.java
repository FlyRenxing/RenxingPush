package top.imzdx.qqpush.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Renxing
 */
@Data
public class Result implements Serializable {
    private boolean flag;
    private String msg;
    private Object data;

    public Result(String msg, Object data) {
        this.flag = true;
        this.msg = msg;
        this.data = data;
    }

    public Result(boolean b, String msg, Object data) {
        this.flag = b;
        this.msg = msg;
        this.data = data;
    }

    //自定义异常返回的结果
    public static Result defineError(String e) {
        return new Result(false, e, null);
    }

    //其他异常处理方法返回的结果
    public static Result otherError(Exception e) {
        return new Result(false, "未知异常！信息：" + e.getMessage(), null);
    }
}
