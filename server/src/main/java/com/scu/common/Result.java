package com.scu.common;


import com.scu.constant.MessageConstant;
import com.scu.constant.ResultCodeConstant;
import lombok.Data;

@Data
public class Result <T>{
    private T data;
    private String message;
    private Integer code;

    public static <T> Result<T> success(){
        return success(null);
    }

    public static <T> Result<T> success(T  data){
        Result<T> result = new Result<T>();
        result.setData(data);
        result.setCode(ResultCodeConstant.SUCCESS);
        result.setMessage(MessageConstant.SUCCESS);
        return result;
    }

    public static <T> Result<T> error(String message){
        Result<T> result = new Result<T>();
        result.setCode(ResultCodeConstant.FAILURE);
        result.setMessage(message);
        return result;
    }
}
