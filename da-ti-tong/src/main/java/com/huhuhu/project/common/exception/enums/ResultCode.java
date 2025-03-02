package com.huhuhu.project.common.exception.enums;

/**
 * 枚举了一些常用API操作码
 * @author konghao
 * @date 2022/4/6
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(1200, "操作成功"),
    FAILED(1500, "操作失败"),
    VALIDATE_FAILED(1404, "参数检验失败"),
    UNAUTHORIZED(1401, "暂未登录或token已经过期"),
    FORBIDDEN(1403, "没有相关权限"),
    TOKEN_GET_ERROR(1405, "认证过期，请重新登录。"),

    DOWNLOAD_EXCEL_ERROR(1300, "EXCEL下载失败"),
    CREATE_QR_CODE_ERROR(1301, "二维码生成失败"),
    SEND_SM_WARMING(1302, "操作太过频繁，请稍后再试。"),
    SM_IS_EXPIRE(1303, "验证码过期，请重新验证。"),
    SM_IS_ERROR(1304, "验证码错误！"),

    WX_LOGIN_ERROR(5001, "微信登陆服务器端请求失败！"),
    AUTH_PHONE_ERROR(5002, "手机号授权失败！"),

    CHAPTER_NOT_FIND(3001, "数据不存在！"),
    USER_NOT_FIND(3002, "当前用户不存在"),
    PASSWORD_ERROR(3003, "密码错误！"),
    LIST_DATA_IS_EMPTY(3004, "集合数据不能为空！"),
    IMAGE_PARSE_ERROR(3005, "解析失败！"),
    MONTH_MAX_DOWNLOAD_ERROR(3006, "本月下载次数已达上限！"),
    PAY_ERROR(3007, "支付失败！请稍后重试。"),
    DOWNLOAD_ERROR(3008, "下载错误！"),
    MONTH_MAX_SEARCH_ERROR(3009, "本月搜题次数已达上限!"),
    EXAM_INFO_EXISTS(3010, "已存在考试信息");

    private final Integer code;
    private final String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public Integer getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
