package com.huhuhu.project.enums;

import lombok.Getter;

@Getter
public enum QuestionDifficultyEnum {
    EASY(1, "简单"),
    INTERMEDIATE(2, "中等"),
    HARD(3, "困难"),
    ;

    QuestionDifficultyEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private final Integer code;
    private final String name;
}
