package com.alibb.system.exception;

import com.alibb.system.constant.ReturnCode;
import lombok.Getter;

/**
 * 自定义异常
 */
public class CustomException extends RuntimeException{

    @Getter
    private ReturnCode returnCode;

    public CustomException(ReturnCode returnCode) {
        this.returnCode = returnCode;
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

}
