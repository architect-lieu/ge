package com.huhuhu.project.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/10 20:45
 */
@Data
public class CustomerVo {
    private Long userId;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "头像")
    private String headPicture;

    @Schema(description = "手机号")
    private String mobilephone;

    @Schema(description = "最后登录时间")
    private Date lastLoginTime;

    @Schema(description = "会员标识")
    private Integer vipFlag;

    @Schema(description = "第一次开通vip时间")
    private Date fistVipPayTime;

    @Schema(description = "最后一次续费时间")
    private Date lastVipPayTime;

    @Schema(description = "积分")
    private Integer integral;

    @Schema(description = "token")
    private String token;

    @Schema(description = "用于小程序新用户标识")
    private Boolean minAppNewUserFlag;

    @Schema(description = "vip过期时间")
    private Date vipExpirationTime;

    private String sessionKey;

    private String openid;

    @Schema(description = "用户来源")
    private String source;

    private String subjects;
}
