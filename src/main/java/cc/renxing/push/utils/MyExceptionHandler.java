package cc.renxing.push.utils;

import cc.renxing.push.model.dto.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Renxing
 */
@ControllerAdvice
public class MyExceptionHandler {
    boolean debug;

    public MyExceptionHandler(@Value("${app.system.debug}") boolean debug) {
        this.debug = debug;
    }

    @ExceptionHandler(value = DefinitionException.class)
    @ResponseBody
    public Result<String> exceptionHandler(DefinitionException e) {
        if (debug) {
            e.printStackTrace();
        }
        return Result.defineError(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<String> BindExceptionHandler(MethodArgumentNotValidException e) {
        if (debug) {
            e.printStackTrace();
        }
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.defineError(message);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<String> exceptionHandler(Exception e) {
        e.printStackTrace();
        return Result.otherError(e);
    }
}
