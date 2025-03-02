package com.huhuhu.project.common.exception.enums;

/**
 * @author KangXin
 * @version 1.0
 * @desc TODO
 * @date 2023/3/10 21:23
 */
public class ErrorCode implements IErrorCode {

    /**
     * 错误码
     */
    private final Integer code;
    /**
     * 错误提示
     */
    private final String msg;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
