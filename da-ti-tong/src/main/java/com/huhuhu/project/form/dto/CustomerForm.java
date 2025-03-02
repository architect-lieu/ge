package com.huhuhu.project.form.dto;

import cn.hutool.core.bean.BeanUtil;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.entity.Customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/11 9:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerForm extends BaseForm<Customer> {
    @NotNull(groups = SystemConstant.Update.class, message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "昵称")
    @NotBlank(message = "昵称不能为空")
    private String nickName;

    @Schema(description = "头像")
//    @NotBlank(message = "请上传头像")
    private String headPicture;

    @Schema(description = "手机号")
//    @NotBlank(message = "手机号不能为空")
    private String mobilephone;

    @Schema(description = "密码")
//    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "再次输入密码")
//    @NotBlank(message = "请再次输入密码")
    private String rePassword;

    @Override
    public Customer build() {
        Customer customer = new Customer();
        BeanUtil.copyProperties(this, customer);
        return customer;
    }
}
