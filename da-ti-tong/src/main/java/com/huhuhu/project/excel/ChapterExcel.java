package com.huhuhu.project.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ExcelTarget("chapterExcel")
public class ChapterExcel {
    @Schema(description = "章节ID")
    @Excel(name = "章节ID", groupName = "所属试题集", needMerge = true)
    private String chapterId;

    @Schema(description = "章节名称")
    @Excel(name = "章节名称", groupName = "所属试题集", needMerge = true)
    private String chapterName;
}
