package com.huhuhu.project.common.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.huhuhu.project.common.exception.enums.IErrorCode;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.common.vo.CommonResult;
import io.jsonwebtoken.JwtException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author KangXin
 * @version 1.0
 * @desc 全局的异常处理类
 * @date 2022/4/6 19:49
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     * @param e 捕捉的异常
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public CommonResult<?> handlerMallServiceException(BusinessException e, HttpServletResponse response){
        IErrorCode errorCode = e.getErrorCode();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        if (errorCode == null) {
            return CommonResult.failed(e.getMessage());
        }
        return CommonResult.failed(errorCode);
    }

    /**
     * token 异常
     * @param e e
     * @return
     */
    @ExceptionHandler({JWTVerificationException.class, JwtException.class})
    public CommonResult<?> handlerJWTVerificationException(Exception e){
        return CommonResult.failed(ResultCode.TOKEN_GET_ERROR);
    }

    /**
     * 表单校验
     * @param e 参数校验异常
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> handlerValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        Map<String,String> message = new HashMap<>();
        for (ObjectError error : allErrors) {
            FieldError fe = (FieldError)error;
            message.put(fe.getField(),error.getDefaultMessage());
        }
        return CommonResult.validateFailed("表单数据错误", message);
    }

    @ExceptionHandler(Exception.class)
    public CommonResult<?> serverError(Exception e){
        e.printStackTrace();
        return CommonResult.failed(String.format("服务器端错误:%s", e.getMessage()));
    }

}
