package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SubjectPageParams extends BasePageParams{
    @Schema(description = "科目名称")
    private String subjectName;
    @Schema(description = "分类ID")
    private Long categoryId;
}
