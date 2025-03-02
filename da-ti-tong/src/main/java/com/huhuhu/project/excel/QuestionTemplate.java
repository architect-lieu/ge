package com.huhuhu.project.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class QuestionTemplate {
    @Excel(name = "题目")
    private String questionTitle;
    @Excel(name = "题型")
    private String questionType;
    @Excel(name = "科目")
    private String subject;
    @Excel(name = "选项")
    private String options;
    @Excel(name = "正确选项")
    private String rightOptions;
    @Excel(name = "难度")
    private String diff;
    @Excel(name = "题目解析")
    private String analysis;
    @Excel(name = "正确答案")
    private String rightAns;
}
