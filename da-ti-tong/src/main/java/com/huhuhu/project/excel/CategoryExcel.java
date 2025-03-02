package com.huhuhu.project.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ExcelTarget("categoryExcel")
public class CategoryExcel {
    @Schema(description = "题目 文档分类ID")
    @Excel(name = "分类ID",needMerge = true)
    private String categoryId;

    @Schema(description = "分类名称")
    @Excel(name = "分类名称",needMerge = true)
    private String categoryName;
}
