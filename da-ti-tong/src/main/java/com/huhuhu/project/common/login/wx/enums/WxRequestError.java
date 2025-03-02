package com.huhuhu.project.common.login.wx.enums;

import com.huhuhu.project.common.exception.enums.IErrorCode;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/12 10:20
 */
public enum WxRequestError implements IErrorCode {
    CODE_ERROR(40029, "CODE无效"),
    API_ERROR(45011, "API调用太频繁，请稍候再试"),
    RISK_LOGIN(40226, "高风险等级用户，小程序登录拦截。"),
    SYSTEM_ERROR(-1, "系统繁忙，请稍候再试"),
    USED_CODE(40163, "oauth_code已使用"),


    SECRET_ERROR(40001, "AppSecret错误或者AppSecret不属于这个公众号，请开发者确认AppSecret的正确性"),
    GRANT_TYPE_ERROR(40002, "请确保grant_type字段值为client_credential"),
    OPENID_ERROR(40003, "openid错误"),
    BLACK_IP_ERROR(40164, "调用接口的IP地址不在白名单中，请在接口IP白名单中进行设置。"),
    IP_ENTER_ERROR(89503, "此IP调用需要管理员确认,请联系管理员"),
    IP_WAIT_ERROR(89501, "此IP正在等待管理员确认,请联系管理员"),
    IP_TIME_LIMIT_DAY_ERROR(89506, "24小时内该IP被管理员拒绝调用两次，24小时内不可再使用该IP 调用"),
    IP_TIME_LIMIT_HOUR_ERROR(89507, "1小时内该IP被管理员拒绝调用一次，1小时内不可再使用该IP调用");
    private final Integer code;
    private final String message;

    private static final Map<Integer, WxRequestError> MAP = Arrays.stream(WxRequestError.values()).collect(Collectors.toMap(WxRequestError::getCode, Function.identity()));

    WxRequestError(Integer code, String message) {
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

    public static WxRequestError getByErrorCode(Integer code) {
        WxRequestError wxRequestError = MAP.get(code);
        if (wxRequestError == null) {
            return WxRequestError.SYSTEM_ERROR;
        }
        return wxRequestError;
    }

}
