package com.huhuhu.project.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author KangXin
 * @version 1.0
 * @desc TODO
 * @date 2023/3/12 14:57
 */
@Getter
public enum UserEnum {
    SOURCE_MIN_APP(1, "MIN_APP"),
    SOURCE_APP(2, "APP"),

    IDENTITY_USER(1000, "普通会员"),
    IDENTITY_VIP(1001, "会员"),
    IDENTITY_SUPER_VIP(1002, "超级会员"),

    ;
    private final Integer code;
    private final String desc;

    UserEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<Integer, UserEnum> MAP = Arrays.stream(UserEnum.values())
            .collect(Collectors.toMap(UserEnum::getCode, Function.identity()));

    public static UserEnum getByCode(Integer code) {
        return MAP.get(code);
    }
}
