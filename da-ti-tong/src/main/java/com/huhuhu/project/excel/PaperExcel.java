package com.huhuhu.project.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PaperExcel {
    @Schema(description = "卷子id")
    @Excel(name = "试题集ID", groupName = "所属试题集", needMerge = true)
    private String paperId;

    @Schema(description = "卷子名")
    @Excel(name = "试题集名称", groupName = "所属试题集", needMerge = true)
    private String paperName;

    @ExcelEntity(name = "分类")
    private ChapterExcel chapterExcel;

    private Long chapterId;
}
