package com.huhuhu.project.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class QuestionExcel {

    @Excel(name = "问题ID", needMerge = true)
    private Long questionId;

    @ExcelEntity(name = "分类")
    private CategoryExcel categoryExcel;

    @ExcelCollection(name = "所属试题集")
    private List<PaperExcel> paperExcels;

    @Schema(description = "问题")
    @Excel(name = "问题", needMerge = true)
    private String questionTitle;

    @Excel(name = "题型", needMerge = true)
    private String questionTypeName;

    @ExcelCollection(name = "全部选项")
    private List<Option> optionList;

    @ExcelCollection(name = "正确选项")
    private List<RightOption> rightOptionList;

    @Excel(name = "难度： 简单 中等 困难", needMerge = true)
    private String difficulty;

    @Excel(name = "正确回答[ 填空或者简答题 ]", needMerge = true)
    private String rightAnswer;

    @Excel(name = "问题解析", needMerge = true)
    private String analysis;

    @Schema(description = "创建时间")
    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss SSS", needMerge = true)
    private Date createTime;

    @Excel(name = "更新时间", format = "yyyy-MM-dd HH:mm:ss SSS", needMerge = true)
    private Date updateTime;

    @Data
    public static class Option {
        @Excel(name = "选项",groupName = "全部选项",needMerge = true)
        private String option;
    }

    @Data
    public static class RightOption {
        @Excel(name = "选项",groupName = "正确选项",needMerge = true)
        private String rightOption;
    }
}
