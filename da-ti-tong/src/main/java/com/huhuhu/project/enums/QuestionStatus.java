package com.huhuhu.project.enums;

import lombok.Getter;

@Getter
public enum QuestionStatus {
    RIGHT(1, "正确"),
    ERROR(2, "错误");
    private final Integer code;
    private final String name;

    QuestionStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
