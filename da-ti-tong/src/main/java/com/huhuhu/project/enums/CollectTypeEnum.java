package com.huhuhu.project.enums;

import lombok.Getter;

@Getter
public enum CollectTypeEnum {
    TYPE(1, "类型收藏"),
    QUESTION(2, "问题收藏")
    ;

    CollectTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    private final Integer code;
    private final String name;

}
