package com.wms.exception;

import com.wms.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //ResponseEntity是标准Http响应，把Result作为参数传入响应体

    @ExceptionHandler(myException.class)
    public ResponseEntity<Result> handleMyException(myException e, HttpServletRequest request) {
        AllException exception = e.getException();
        log.warn("business_exception={} message={} path={}",
                exception.name(), exception.getError(), request.getRequestURI());

        //JWT登录权限校验异常，单独区分
        if (exception == AllException.TOKEN_ERROR || exception == AllException.TOKEN_EXPIRED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Result.unauthorized(exception.getError()));
        }
        return ResponseEntity.ok(Result.fail(exception.getError()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception e, HttpServletRequest request) {
        log.error("system_exception path={}", request.getRequestURI(), e);
        return ResponseEntity.ok(Result.fail("系统异常，请联系管理人员"));
    }
}
