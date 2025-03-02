package com.huhuhu.project.form.params;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/12 14:42
 */
@Data
public class MinAppAuthPhoneParam {
    @NotBlank(message = "加密的数据不能为空！")
    private String encryptedData;
    @NotBlank(message = "iv不能为空")
    private String iv;
    @NotBlank(message = "获取的sessionKey不能为空")
    private String sessionKey;
    @NotBlank(message = "获取的openId不能为空")
    private String openId;
    @NotBlank(message = "获取的unionId不能为空")
    private String unionId;
    @NotBlank(message = "用户信息加密的数据不能为空！")
    private String encryptedDataUserInfo;
    @NotBlank(message = "用户信息iv不能为空")
    private String ivUserInfo;
}
