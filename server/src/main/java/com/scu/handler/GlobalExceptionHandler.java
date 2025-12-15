package com.scu.handler;

import com.scu.common.Result;
import com.scu.exception.BaseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public Result handleBaseException(BaseException e){
        return Result.error(e.getMessage());
    }

}
