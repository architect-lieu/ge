package com.huhuhu.project.common.login.wx.wxservice;

import com.huhuhu.project.common.login.ThirdPartyLoginService;
import com.huhuhu.project.common.login.wx.dto.WxLoginDto;
import com.huhuhu.project.common.login.wx.dto.WxPhoneNumDto;
import com.huhuhu.project.common.login.wx.dto.WxUserInfoDto;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/12 10:18
 */
public interface WxLoginService extends ThirdPartyLoginService<WxLoginDto> {
    /**
     * 获取微信用户信息
     * @param openid
     * @return
     */
    WxUserInfoDto wxUserInfo(String openid);

    WxPhoneNumDto userPhoneNum(String code);
}
