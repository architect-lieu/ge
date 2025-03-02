package com.huhuhu.project.common.sms.impl;

import com.huhuhu.project.common.exception.enums.IErrorCode;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.common.exception.enums.SmsFrameworkErrorCodeConstants;
import com.huhuhu.project.common.sms.SmsCodeMapping;
import org.springframework.stereotype.Component;

/**
 * 阿里云的 SmsCodeMapping 实现类
 *
 * 参见 https://help.aliyun.com/document_detail/101346.htm 文档
 *
 * @author 芋道源码
 */
@Component
public class AliyunSmsCodeMapping implements SmsCodeMapping {

    @Override
    public IErrorCode apply(String apiCode) {
        switch (apiCode) {
            case "OK": return ResultCode.SUCCESS;
            case "isv.ACCOUNT_NOT_EXISTS":
            case "isv.ACCOUNT_ABNORMAL":
            case "MissingAccessKeyId": return SmsFrameworkErrorCodeConstants.SMS_ACCOUNT_INVALID;
            case "isp.RAM_PERMISSION_DENY": return SmsFrameworkErrorCodeConstants.SMS_PERMISSION_DENY;
            case "isv.INVALID_JSON_PARAM":
            case "isv.INVALID_PARAMETERS": return SmsFrameworkErrorCodeConstants.SMS_API_PARAM_ERROR;
            case "isv.BUSINESS_LIMIT_CONTROL": return SmsFrameworkErrorCodeConstants.SMS_SEND_BUSINESS_LIMIT_CONTROL;
            case "isv.DAY_LIMIT_CONTROL": return SmsFrameworkErrorCodeConstants.SMS_SEND_DAY_LIMIT_CONTROL;
            case "isv.SMS_CONTENT_ILLEGAL": return SmsFrameworkErrorCodeConstants.SMS_SEND_CONTENT_INVALID;
            case "isv.SMS_TEMPLATE_ILLEGAL": return SmsFrameworkErrorCodeConstants.SMS_TEMPLATE_INVALID;
            case "isv.SMS_SIGNATURE_ILLEGAL":
            case "isv.SIGN_NAME_ILLEGAL":
            case "isv.SMS_SIGN_ILLEGAL": return SmsFrameworkErrorCodeConstants.SMS_SIGN_INVALID;
            case "isv.AMOUNT_NOT_ENOUGH":
            case "isv.OUT_OF_SERVICE": return SmsFrameworkErrorCodeConstants.SMS_ACCOUNT_MONEY_NOT_ENOUGH;
            case "isv.MOBILE_NUMBER_ILLEGAL": return SmsFrameworkErrorCodeConstants.SMS_MOBILE_INVALID;
            case "isv.TEMPLATE_MISSING_PARAMETERS": return SmsFrameworkErrorCodeConstants.SMS_TEMPLATE_PARAM_ERROR;
            default: return SmsFrameworkErrorCodeConstants.SMS_UNKNOWN;
        }
    }

}
