package com.huhuhu.project.utils;

import cn.hutool.core.util.StrUtil;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Admin;
import com.huhuhu.project.entity.Customer;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author KangXin
 * @version 1.0
 * @date 2022/5/31 17:06
 */
@Slf4j
public class TokenUtil {

    private static final String ISS = "TANG-YUAN-INTERACTION";

    /**
     * 用户登录
     * @param user 用户信息
     */
    public static String createToken(Customer user) {
        Map<String,Object> map = new LinkedHashMap<>(3);
        map.put("userId", user.getUserId());
        map.put("username", user.getNickName());
        map.put("type", SystemConstant.CUSTOMER);
        map.put("vipFlag", user.getVipFlag());
        return JWTUtil.createToken(ISS, SystemConstant.MAX_LOGIN_TIME,map);
    }

    /**
     * 管理员登录
     * @param admin 管理员
     */
    public static String createToken(Admin admin) {
        Map<String,Object> map = new LinkedHashMap<>(3);
        map.put("userId", admin.getAdminId());
        map.put("username", admin.getAdminName());
        map.put("type", SystemConstant.ADMIN);
        return JWTUtil.createToken(ISS, SystemConstant.MAX_LOGIN_TIME,map);
    }

    /**
     * 获取载荷信息，用户信息
     */
    public static Map<String,Object> getClaims() {
        return JWTUtil.decode(getToken());
    }

    /**
     * 用户id
     */
    public static Long currentLoginUserId() {
        return Long.parseLong(getClaims().get("userId").toString());
    }

    /**
     * 用户类型
     */
    public static String currentLoginUserType() {
        return (String) getClaims().get("type");
    }

    /**
     * 用户名
     */
    public static String currentLoginUserName() {
        return (String) getClaims().get("username");
    }

    /**
     * 校验token
     */
    public static boolean check() {
        boolean login = isLogin();
        if (login) {
            JWTUtil.isVerify(getToken());
        }
        return login;
    }

    private static String getToken() {
        return WebUtil.getHeader(SystemConstant.TOKEN_KEY);
    }

    public static boolean isLogin() {
        return StrUtil.isNotBlank(getToken());
    }

    public static boolean loginCheck() {
        boolean login = isLogin();
        if (login) {
            try {
                JWTUtil.isVerify(getToken());
            }catch (Exception e) {
                return false;
            }
        }
        return login;
    }
}
