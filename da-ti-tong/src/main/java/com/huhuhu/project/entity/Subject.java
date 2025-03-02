package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import com.huhuhu.project.common.constant.SystemConstant;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author KangXin
 * @since 2023-07-01 06:02:37
 */
@Getter
@Setter
@TableName("dtt_subject")
@Schema(description = "Subject对象")
public class Subject {

    @Schema(description = "科目id")
    @TableId(value = "subject_id", type = IdType.AUTO)
    @NotNull(message = "科目id不能为空", groups = SystemConstant.Update.class)
    private Long subjectId;

    @Schema(description = "科目名称")
    @TableField("subject_name")
    @NotBlank(message = "科目名称不能为空")
    private String subjectName;

    @Schema(description = "分类ID")
    @TableField("category_id")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @TableField(exist = false)
    private Long totalQuestionNum;

    @TableField(exist = false)
    private Category category;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
