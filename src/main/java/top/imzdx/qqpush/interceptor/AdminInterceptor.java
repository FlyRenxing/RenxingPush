package top.imzdx.qqpush.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.imzdx.qqpush.model.po.User;
import top.imzdx.qqpush.utils.DefinitionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * @author Renxing
 * @description
 * @date 2021/4/11 13:44
 */
public class AdminInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }
        // ①:START 方法注解级拦截器
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录
        AdminRequired methodAnnotation = method.getAnnotation(AdminRequired.class);
        // 有 @LoginRequired 注解，需要认证
        if (methodAnnotation != null) {
            // 这写你拦截需要干的事儿，比如取缓存，SESSION，权限判断等
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                throw new DefinitionException("当前未登录");
            }
            long userType = user.getAdmin();
            if (userType == 1) {
                return true;
            } else {
                throw new DefinitionException("您不是管理员");
            }

        }

        return true;


    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
