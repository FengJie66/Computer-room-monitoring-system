package com.alibb.system.exception;

import com.alibb.system.constant.ReturnCode;

/**
 * 自定义断言处理类，用于抛出各种自定义异常
 */
public class Asserts {

    public static void fail(String message) {
        throw new CustomException(message);
    }

    public static void fail(ReturnCode returnCode){
        throw new CustomException(returnCode);
    }

}
