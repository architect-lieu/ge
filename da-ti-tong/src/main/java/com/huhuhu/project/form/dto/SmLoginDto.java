package com.huhuhu.project.form.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @author KangXin
 * @version 1.0
 * @desc TODO
 * @date 2023/3/10 20:49
 */
@Data
public class SmLoginDto {
    @NotBlank(message = "手机号不能为空")
    private String phone;
    @NotNull(message = "验证码不能为空")
    private Integer code;
}
