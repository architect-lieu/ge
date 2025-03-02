package com.huhuhu.project.enums;

import lombok.Getter;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/5/3 11:42
 */
@Getter
public enum VipConfigEnum {
    MONTH_PRICE(1, "month_price"),
    QUARTER_PRICE(2, "quarter_price"),
    YEAR_PRICE(3, "year_price")
    ;
    private final Integer code;
    private final String desc;

    VipConfigEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
