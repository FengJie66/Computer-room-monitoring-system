package com.alibb.system.exception;

import com.alibb.system.component.Result;
import com.alibb.system.constant.ReturnCode;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        logger.error("未知异常：{}", e, e.getMessage());
        return Result.fail(ReturnCode.RC500);
    }

    @ExceptionHandler(CustomException.class)
    public Result handleCustomException(CustomException e) {
        logger.warn("自定义异常：{}", e, e.getMessage());
        return Result.fail(e.getReturnCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.warn(e.getMessage(),e);
        BindingResult bindingResult = e.getBindingResult();
        String message = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null) {
                message = "字段：" + fieldError.getField() + "，" +fieldError.getDefaultMessage();
            }
        }
        return Result.fail(ReturnCode.RC403.getCode(),message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return Result.fail(ReturnCode.RC403);
    }

    /**
     * 抛出AccessDeniedException，防止被handleException先消费掉
     */
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleInvalidFormatException(HttpMessageNotReadableException e) throws HttpMessageNotReadableException {
        return Result.fail(ReturnCode.RC403);
    }


}
