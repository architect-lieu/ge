
package com.huhuhu.project.common.login.wx.dto;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxPhoneInfo {
    @JSONField(name = "countryCode")
    private Integer countryCode;
    @JSONField(name = "phoneNumber")
    private String phoneNumber;
    @JSONField(name = "purePhoneNumber")
    private String purePhoneNumber;
    @JSONField(name = "watermark")
    private Watermark watermark;

    @Data
    private static class Watermark {
        private String appid;
        private long timestamp;
    }

}
