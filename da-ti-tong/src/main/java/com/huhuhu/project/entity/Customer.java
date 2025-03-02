package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Getter
@Setter
@TableName("dtt_customer")
@Schema(description = "Customer对象")
public class Customer {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @Schema(description = "昵称")
    @TableField("nick_name")
    private String nickName;

    @Schema(description = "头像")
    @TableField("head_picture")
    private String headPicture;

    @Schema(description = "wx openid")
    @TableField("openid")
    private String openid;

    @TableField("union_id")
    private String unionId;

    @Schema(description = "手机号")
    @TableField("mobilephone")
    private String mobilephone;

    @Schema(description = "sessionKey", hidden = true)
    private String sessionKey;

    @Schema(description = "密码",hidden = true)
    @TableField("password")
    private String password;

    @Schema(description = "登录时短信验证码",hidden = true)
    @TableField("ms_code")
    private Integer msCode;

    @Schema(description = "登录时短信验证码生成时间",hidden = true)
    @TableField("ms_code_time")
    private Date msCodeTime;

    @Schema(description = "最后登录时间")
    @TableField("last_login_time")
    private Date lastLoginTime;

    @Schema(description = "会员标识")
    @TableField("vip_flag")
    private Integer vipFlag;

    @Schema(description = "第一次开通vip时间")
    @TableField("fist_vip_pay_time")
    private Date fistVipPayTime;

    @Schema(description = "最后一次续费时间")
    @TableField("last_vip_pay_time")
    private Date lastVipPayTime;

    @Schema(description = "vip过期时间")
    @TableField("vip_expiration_time")
    private Date vipExpirationTime;

    @Schema(description = "积分")
    @TableField("integral")
    private Integer integral;

    @Schema(description = "用户初次来源")
    @TableField("source")
    private String source;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
