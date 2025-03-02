package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.huhuhu.project.common.constant.SystemConstant;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Getter
@Setter
@TableName("dtt_chapter")
@Schema(description = "Chapter对象")
public class Chapter {

    @Schema(description = "章节ID")
    @TableId(value = "chapter_id", type = IdType.AUTO)
    @NotNull(message = "章节ID不能为空", groups = SystemConstant.Update.class)
    private Long chapterId;

    @Schema(description = "章节名称")
    @TableField("chapter_name")
    @NotBlank(message = "章节名称不能为空")
    private String chapterName;

    @Schema(description = "所属分类")
    @TableField("category_id")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @Schema(description = "科目ID")
    @TableField("subject_id")
    private Long subjectId;

    @Schema(description = "是否是真题章节")
    @TableField("true_question_chapter_flag")
    private Boolean trueQuestionChapterFlag;

    @Schema(description = "章节下的试题集", hidden = true)
    @TableField(exist = false)
    private List<Paper> papers = new ArrayList<>();

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
