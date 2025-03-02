package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/13 13:27
 */
@Data
public class QuestionPageParams extends BasePageParams{
    @Schema(description = "问题")
    private String questionTitle;
    @Schema(description = "问题类型编码")
    private String questionTypeCode;
    @Schema(description = "问题类型名称")
    private String questionTypeName;
    @Schema(description = "难度： 简单 中等 困难")
    private String difficulty;
    @Schema(description = "所属试题集ID")
    private Long paperId;
    @Schema(description = "所属分类的ID")
    private Long categoryId;
    @Schema(description = "高频错题")
    private Boolean highFrequencyError;
}
