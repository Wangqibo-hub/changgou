package com.changgou.exception;

/**
 * @author Wangqibo
 * @version 1.0
 * @date 2020-08-10 11:05
 * @description 自定义异常类
 */
public class MyException extends Exception {
    private String message;

    public MyException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
