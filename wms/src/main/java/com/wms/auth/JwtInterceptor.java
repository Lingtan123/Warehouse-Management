package com.wms.auth;

import com.wms.exception.AllException;
import com.wms.exception.myException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    /*这是一个Spring MVC拦截器，专用于http请求，属于Web层，和Spring的AOP代理机制不同
    * AOP是Java内部给方法调用的时候拦截的，用作方法增强
    * Spring MVC拦截器的顺序是:
    * 浏览器 HTTP 请求 → Tomcat → Filter 过滤器 → HandlerInterceptor 拦截器 → Controller 接口方法
    * 它会对所有http请求拦截，调用其中方法，为符合业务逻辑，不可以加入登录的http请求，否则登录报错
    * */
    private final JwtUtils jwtUtils;

    //final对象只能用构造函数注入依赖，不能用注解
    public JwtInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        /*这是跨域请求的逻辑，协议、域名、端口任意一个不同就是跨域请求
        * 简单跨域请求GET、普通HEAD、普通POST，无自定义请求头的请求都是直接发送请求
        * 复杂跨域请求PUT、DELETE、带自定义请求头的请求发送过程不同于简单请求
        * 先发送一个OPTIONS请求试探一下，询问后端是否允许这个前端域名跨域、允许哪些请求头、允许哪些方法
        * 后端返回允许之后前端才会真实发送令牌，所以必须放行OPTIONS试探请求
        * */
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        /*封装方法，统一输出401JSON响应
        * Http状态码401
        * 设置编码格式
        * 响应数据类型为JSON
        * 统一封装返回对象为JSON并写入响应
        * */
        //现在改为抛出异常，由全局异常处理器统一完成401JSON响应
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authorization) || !authorization.startsWith("Bearer ")) {
            throw new myException(AllException.TOKEN_ERROR);
        }

        //截取请求头的固定前缀 "Bearer " 后面才是纯令牌，生成的那个
        //获取真实令牌
        String token = authorization.substring(7);
        CurrentUser currentUser = jwtUtils.parseToken(token);
        if (currentUser == null || currentUser.getId() == null) {
            throw new myException(AllException.TOKEN_EXPIRED);
        }

        UserContext.setCurrentUser(currentUser);
        return true;
    }

    //请求结束，清空线程中存储的用户信息
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
