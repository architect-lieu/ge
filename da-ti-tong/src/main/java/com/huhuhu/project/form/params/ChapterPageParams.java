package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/12 21:34
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChapterPageParams extends BasePageParams {
    @Schema(description = "章节名称")
    private String chapterName;
    @Schema(description = "所属分类")
    @NotBlank(message = "分类ID不能为空")
    private Long categoryId;

    @Schema(description = "章节ID")
    private Long subjectId;

    @Schema(description = "是否是真题章节")
    private Boolean trueQuestionChapterFlag;
}
