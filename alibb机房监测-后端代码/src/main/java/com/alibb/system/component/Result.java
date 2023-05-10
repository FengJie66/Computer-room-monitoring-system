package com.alibb.system.component;

import com.alibb.system.constant.ReturnCode;
import lombok.Data;

@Data
public class Result<T> {

    private int status; // 状态码

    private String msg; // 返回信息

    private T data; // 返回的数据

    private long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> success(T data){
        Result<T> result = new Result<>();
        result.setStatus(ReturnCode.RC100.getCode());
        result.setMsg(ReturnCode.RC100.getMsg());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> fail(String msg){
        Result<T> result = new Result<>();
        result.setStatus(ReturnCode.RC999.getCode());
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> fail(int code, String msg){
        Result<T> result = new Result<>();
        result.setStatus(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> fail(ReturnCode returnCode){
        Result<T> result = new Result<>();
        result.setStatus(returnCode.getCode());
        result.setMsg(returnCode.getMsg());
        return result;
    }

}
