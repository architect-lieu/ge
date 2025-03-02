
package com.huhuhu.project.common.login.wx.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class WxUserInfoDto extends WxBaseDto {
    @JSONField(name = "city")
    private String city;
    @JSONField(name = "country")
    private String country;
    @JSONField(name = "avatarUrl")
    private String avatarUrl;
    @JSONField(name = "nickname")
    private String nickName;
    @JSONField(name = "openId")
    private String openId;
    @JSONField(name = "province")
    private String province;
    @JSONField(name = "sex")
    private Long gender;
    @JSONField(name = "unionid")
    private String unionid;
}
