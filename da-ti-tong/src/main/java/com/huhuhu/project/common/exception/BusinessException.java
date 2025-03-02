package com.huhuhu.project.common.exception;

import com.huhuhu.project.common.exception.enums.IErrorCode;
import lombok.Getter;

/**
 * @author KangXin
 * @version 1.0
 * @date 2022/5/31 16:33
 */
@Getter
public class BusinessException extends RuntimeException{
    private IErrorCode errorCode;
    private int code;
    private String message;

    public BusinessException(IErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(Throwable e) {
        this.code = 3001;
        this.message = e.getMessage();
    }
}