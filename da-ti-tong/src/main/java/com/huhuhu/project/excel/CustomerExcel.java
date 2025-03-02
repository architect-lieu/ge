package com.huhuhu.project.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerExcel {

    @Excel(name = "ID")
    private Long userId;

    @Excel(name = "昵称")
    private String nickName;

    @Excel(name = "头像")
    private String headPicture;

    @Excel(name = "手机号")
    private String mobilephone;

    @Excel(name = "是否开通会员")
    private String flag;

    @Excel(name = "vip类型")
    private String vipType;

    @Excel(name = "第一次开通vip时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date fistVipPayTime;

    @Excel(name = "最后一次续费时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date lastVipPayTime;

    @Excel(name = "vip过期时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date vipExpirationTime;

    @Excel(name = "积分")
    private Integer integral;

    @Excel(name = "用户初次来源")
    private String source;

    @Excel(name = "注册时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "做过的题的科目")
    private String subjects;
}
