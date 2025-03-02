package com.huhuhu.project.common.login.wx.wxservice.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.common.login.wx.dto.WxPhoneNumDto;
import com.huhuhu.project.common.login.wx.dto.WxTokenDto;
import com.huhuhu.project.common.login.wx.dto.WxLoginDto;
import com.huhuhu.project.common.login.wx.dto.WxUserInfoDto;
import com.huhuhu.project.common.login.wx.enums.WxRequestError;
import com.huhuhu.project.common.login.wx.wxservice.WxLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/12 9:49
 */
@Service
@Slf4j
public class WxLoginServiceImpl implements WxLoginService {

    @Value("${wx_min_app_login_url}")
    private String wxLoginUrl;

    @Value("${wx_user_info_url}")
    private String wxUserInfoUrl;

    @Value("${wx_user_phone_url}")
    private String wxUserPhoneUrl;

    @Value("${wx_acc_token_url}")
    private String wxAccTokenUrl;

    @Override
    public WxLoginDto login(String code) {
        WxLoginDto wxLoginDto;
        try {
            String url = String.format(wxLoginUrl, code);
            String loginResult = HttpUtil.get(url);
            wxLoginDto = JSON.parseObject(loginResult, WxLoginDto.class);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(ResultCode.WX_LOGIN_ERROR.getMessage());
            throw new BusinessException(ResultCode.WX_LOGIN_ERROR);
        }
        // 校验
        Integer errcode = wxLoginDto.getErrcode();
        if (errcode != null && !errcode.equals(0)) {
            WxRequestError errorCode = WxRequestError.getByErrorCode(errcode);
            throw new BusinessException(errorCode);
        }
        return wxLoginDto;
    }

    @Override
    public WxUserInfoDto wxUserInfo(String openid) {
        WxUserInfoDto wxUserInfoDto;
        try {
            String tokenData = HttpUtil.get(wxAccTokenUrl);
            WxTokenDto wxTokenDto = JSON.parseObject(tokenData, WxTokenDto.class);
            if (wxTokenDto.getErrcode() != null) {
                throw new BusinessException(WxRequestError.getByErrorCode(wxTokenDto.getErrcode()));
            }
            String userInfoData = HttpUtil.get(String.format(wxUserInfoUrl, wxTokenDto.getAccessToken(), openid));
            wxUserInfoDto = JSON.parseObject(userInfoData, WxUserInfoDto.class);
            if (!Integer.valueOf(0).equals(wxUserInfoDto.getErrcode())) {
                throw new BusinessException(WxRequestError.getByErrorCode(wxUserInfoDto.getErrcode()));
            }
        }catch (Exception e) {
            throw new BusinessException(ResultCode.WX_LOGIN_ERROR);
        }
        return wxUserInfoDto;
    }

    @Override
    public WxPhoneNumDto userPhoneNum(String code) {
        String tokenData = HttpUtil.get(wxAccTokenUrl);
        WxTokenDto wxTokenDto = JSON.parseObject(tokenData, WxTokenDto.class);
        if (wxTokenDto.getErrcode() != null) {
            throw new BusinessException(WxRequestError.getByErrorCode(wxTokenDto.getErrcode()));
        }
        String userInfoData = HttpUtil.get(String.format(wxUserPhoneUrl, wxTokenDto.getAccessToken(), code));
        WxPhoneNumDto wxPhoneNumDto = JSON.parseObject(userInfoData, WxPhoneNumDto.class);
        if (!Integer.valueOf(0).equals(wxPhoneNumDto.getErrcode())) {
            throw new BusinessException(WxRequestError.getByErrorCode(wxPhoneNumDto.getErrcode()));
        }
        return wxPhoneNumDto;
    }
}
