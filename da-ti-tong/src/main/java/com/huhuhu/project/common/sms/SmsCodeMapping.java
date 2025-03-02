package com.huhuhu.project.common.sms;


import com.huhuhu.project.common.exception.enums.IErrorCode;

import java.util.function.Function;

/**
 * 将 API 的错误码，转换为通用的错误码
 * @author 芋道源码
 */
public interface SmsCodeMapping extends Function<String, IErrorCode> {
}