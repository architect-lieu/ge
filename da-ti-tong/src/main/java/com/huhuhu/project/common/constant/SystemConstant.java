package com.huhuhu.project.common.constant;

import java.util.concurrent.TimeUnit;

/**
 * @author KangXin
 * @version 1.0
 * @desc TODO
 * @date 2023/3/10 9:56
 */
public interface SystemConstant {
    String CUSTOMER = "CUSTOMER";
    String ADMIN = "ADMIN";

    // token请求头
    String TOKEN_KEY = "Ac-Token";

    long MAX_LOGIN_TIME = TimeUnit.SECONDS.toMillis(3600 * 24);

    // 1分钟，离上次发送时间最少间隔一分钟
    int SMS_CODE_MIN_TIME = 1;

    // 验证码有效时间
    int SM_LIVE_MAX_TIME = 10;

    // 校验的标识类
    interface Add{}
    interface Update{}
}
