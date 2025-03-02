package com.huhuhu.project.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum QuestionTypeEnum {

    DAN_XUAN(1, "单选题"),
    DUO_XUAN(2, "多选题"),
    PAN_DUAN(3, "判断题"),
    TIAN_KONG(4, "填空题"),
    JIAN_DA(5, "简答题")
    ;


    private final Integer code;
    private final String name;

    QuestionTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static List<String> getNames() {
        return Arrays.stream(values()).map(QuestionTypeEnum::getName).collect(Collectors.toList());
    }
}
