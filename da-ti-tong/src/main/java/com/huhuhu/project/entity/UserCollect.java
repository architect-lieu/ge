package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

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
@TableName("dtt_user_collect")
@Schema(description = "UserCollect对象")
public class UserCollect {

    @TableId(value = "collect_id", type = IdType.AUTO)
    @Schema(hidden = true)
    private Long collectId;

    @Schema(description = "问题id")
    @TableField("question_id")
    private Long questionId;

    @Schema(description = "分类ID")
    @TableField("category_id")
    private Long categoryId;

    @Schema(description = "题型名称")
    @TableField("question_type_name")
    private String questionTypeName;

    @Schema(description = "用户id", hidden = true)
    @TableField("user_id")
    private Long userId;

    @Schema(hidden = true)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("collect_type")
    @NotNull(message = "收藏类型：1收藏分类  2收藏问题")
    private Integer collectType;

    @TableField(exist = false)
    private Category category;

    @TableField(exist = false)
    private Long categoryQuestionNum;
}
