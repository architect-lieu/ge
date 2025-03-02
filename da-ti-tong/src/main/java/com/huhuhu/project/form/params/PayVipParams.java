package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/5/2 22:47
 */
@Data
public class PayVipParams {
    @Schema(description = "传入VIP类型")
    @NotBlank(message = "VIP类型不能为空")
    private String desc;

    @Schema(description = "会员id")
    @NotNull(message = "会员id不能为空")
    private Long vipId;

    @Schema(description = "会员编码")
    @NotNull(message = "会员code不能为空 普通1000 vip:1001 super vip:1002")
    private Integer vipCode;

    @Schema(description = "支付类型：月month_price 季度 年")
    private String payType;
}
