package com.huhuhu.project.common.login.wx.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/12 11:36
 */
@Data
public abstract class WxBaseDto {
    @JSONField(name = "errcode")
    protected Integer errcode;
    @JSONField(name = "errmsg")
    protected String errmsg;
}
