
package com.huhuhu.project.common.login.wx.dto;

import lombok.Data;


@Data
public class WxPhoneNumDto extends WxBaseDto {
    private PhoneInfo phoneInfo;
}

@Data
class PhoneInfo {
    private String phoneNumber;
    private Watermark watermark;
    private String purePhoneNumber;
    private Long countryCode;
}

@Data
class Watermark {
    private String appid;
    private Long timestamp;
}
