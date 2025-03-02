package com.huhuhu.project.enums;

import lombok.Getter;

@Getter
public enum DownloadSearchRecordEnum {
    DOWNLOAD(1, "DOWNLOAD"),
    SEARCH(2, "SEARCH"),
    ;
    private final Integer code;
    private final String desc;

    DownloadSearchRecordEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
