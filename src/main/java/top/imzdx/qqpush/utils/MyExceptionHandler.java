package top.imzdx.qqpush.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.imzdx.qqpush.model.dto.Result;

/**
 * @author Renxing
 */
@ControllerAdvice
public class MyExceptionHandler {
    String env;

    public MyExceptionHandler(@Value("${spring.profiles.active}") String env) {
        this.env = env;
    }

    @ExceptionHandler(value = DefinitionException.class)
    @ResponseBody
    public Result<Void> exceptionHandler(DefinitionException e) {
        if ("dev".equals(env)) {
            e.printStackTrace();
        }
        return Result.defineError(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<Void> BindExceptionHandler(MethodArgumentNotValidException e) {
        if ("dev".equals(env)) {
            e.printStackTrace();
        }
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.defineError(message);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Void> exceptionHandler(Exception e) {
        e.printStackTrace();
        return Result.otherError(e);
    }
}
