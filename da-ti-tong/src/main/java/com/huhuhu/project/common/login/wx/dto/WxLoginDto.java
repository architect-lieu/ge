
package com.huhuhu.project.common.login.wx.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxLoginDto extends WxBaseDto {
    @JSONField(name = "openid")
    private String openid;
    @JSONField(name = "session_key")
    private String sessionKey;
    @JSONField(name = "unionid")
    private String unionid;
}
