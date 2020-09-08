package com.changgou.exception;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-10 11:01
 * @description 全局的异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //写一个方法 捕获到异常进行处理 返回给页面前端
    //该方法能被执行，一定是报错了
    @ExceptionHandler(value = Exception.class)
    public Result handlerException(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }
}
