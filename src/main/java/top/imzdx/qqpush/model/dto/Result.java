package top.imzdx.qqpush.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 所有的最终反馈会被包装为此类
 *
 * @author Renxing
 * @tag 结果类
 */
@Data
public class Result<T> implements Serializable {
    /**
     * 结果标志，true为成功，false为失败
     */
    private boolean flag;
    /**
     * 结果消息，一般反馈给用户
     */
    private String msg;
    /**
     * 结果数据，一般反馈给前端进行处理
     */
    private T data;

    public Result(String msg, T data) {
        this.flag = true;
        this.msg = msg;
        this.data = data;
    }

    public Result(boolean b, String msg, T data) {
        this.flag = b;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 自定义异常返回的结果
     *
     * @param e 异常消息
     * @return
     */
    public static Result<String> defineError(String e) {
        return new Result<>(false, e, null);
    }

    /**
     * 其他异常处理方法返回的结果
     *
     * @param e 异常
     * @return
     */
    public static Result<String> otherError(Exception e) {
        return new Result<>(false, "未知异常！信息：" + e.getMessage(), null);
    }
}
