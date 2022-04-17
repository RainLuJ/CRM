package com.lujun61.crm.exception;

/**
 * @description 登录异常处理类
 * @author Jun Lu
 * @date 2022-04-16 09:58:43
 */
public class LoginException extends Exception {
    public LoginException() {
    }

    public LoginException(String msg) {
        super(msg);
    }
}
