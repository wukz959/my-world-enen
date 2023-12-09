package com.myworld.enen.config;

import com.myworld.enen.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName GlobalExceptionHandler
 * @Descripton TODO
 * @Author wkz
 * @Date 2023/11/23 21:14
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.myworld.enen.controller")
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result handleAllException(Exception e, HttpServletRequest req) {
        log.error("数据异常{}，异常类型：{}, 请求路径: {}{}",e.getMessage(),e.getClass(), req.getMethod() + ": " ,req.getRequestURI());
        return Result.error();
    }
}
