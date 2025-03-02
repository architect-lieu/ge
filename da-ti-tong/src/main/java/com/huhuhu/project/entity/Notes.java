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
 * @since 2023-04-17 10:16:23
 */
@Getter
@Setter
@TableName("dtt_notes")
@Schema(description = "Notes对象")
public class Notes {

    @TableId(value = "notes_id", type = IdType.AUTO)
    @NotNull(message = "笔记ID不能为空", groups = {SystemConstant.Update.class})
    private Long notesId;

    @Schema(description = "笔记内容")
    @TableField("notes_content")
    @NotBlank(message = "笔记内容不能为空")
    private String notesContent;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "问题id")
    @TableField("question_id")
    @NotNull(message = "问题id不能为空")
    private Long questionId;

    @Schema(description = "问题分类id")
    @NotNull(message = "分类id不能为空")
    private Long categoryId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private Question question;

}
