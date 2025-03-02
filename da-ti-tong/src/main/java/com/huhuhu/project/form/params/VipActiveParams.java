package com.huhuhu.project.form.params;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class VipActiveParams {
//    @NotEmpty(message = "机构编码不能为空")
//    private String orgCode;
    @NotEmpty(message = "激活码不能为空")
    private String activeCode;
}
