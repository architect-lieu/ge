package com.huhuhu.project.common.sms;

import java.util.Map;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/10 19:26
 */
public interface SmsService {

    /**
     * 发送短信
     * @param phone
     * @return
     */
    boolean sendMessage(String phone, Map<String, Object> templateParam);

}
