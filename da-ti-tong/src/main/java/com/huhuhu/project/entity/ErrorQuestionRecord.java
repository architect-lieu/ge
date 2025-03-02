package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
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
@TableName(value = "dtt_error_question_record", autoResultMap = true)
@Schema(description = "ErrorQuestionRecord对象")
public class ErrorQuestionRecord {

    @TableId(value = "error_record_id", type = IdType.AUTO)
    @Schema(hidden = true)
    private Long errorRecordId;

    @Schema(description = "问题id")
    @TableField("question_id")
    @NotNull(message = "问题id不能为空")
    private Long questionId;

    @TableField(exist = false)
    private Question question;

    @Schema(description = "问题的分类ID")
    @NotNull(message = "问题的分类ID不能为空")
    private Long categoryId;

    @TableField(exist = false)
    private Category category;

    @Schema(description = "答题状态：1对 2错")
    @NotNull(message = "答题状态不能为空")
    private Integer status;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "题型")
    @TableField("question_type_name")
    private String questionTypeName;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @Schema(hidden = true)
    private Date createTime;

    @TableField(value = "answer", typeHandler = FastjsonTypeHandler.class)
    private List<String> answer;
}
