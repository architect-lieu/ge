
package com.huhuhu.project.common.login.wx.dto;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxTokenDto extends WxBaseDto {
    @JSONField(name = "access_token")
    private String accessToken;
    @JSONField(name = "expires_in")
    private Integer expiresIn;
}
