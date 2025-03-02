package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


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
@TableName("dtt_feedback")
@Schema(description = "Feedback对象")
public class Feedback {

    @TableId("feedback_id")
    private Long feedbackId;

    @Schema(description = "反馈内容")
    @TableField("feedback_content")
    @NotBlank(message = "反馈内容不能为空")
    private String feedbackContent;

    @Schema(description = "反馈类型-前端选择")
    @TableField("type")
    @NotBlank(message = "反馈类型不能为空")
    private String type;

    @Schema(description = "哪里的反馈-对应错题 小程序反馈")
    @TableField("use_flag")
    private Integer useFlag;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "问题id-错题的反馈会有")
    @TableField("question_id")
    private Long questionId;

    @Schema(description = "分类id-错题的反馈会有")
    @TableField("category_id")
    private Long categoryId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private Question question;

}
