package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-03-10 07:56:41
 */
@Getter
@Setter
@TableName("dtt_exam_config")
@Schema(description = "ExamConfig对象")
public class ExamConfig {

    @Schema(description = "考试id")
    @TableId(value = "examination_id", type = IdType.AUTO)
    @NotNull(message = "规则ID不能为空", groups = SystemConstant.Update.class)
    private Long examinationId;

    @Schema(description = "类型id")
    @TableField("category_id")
    @NotNull(message = "类型ID不能为空")
    private Long categoryId;

    @Schema(description = "题型")
    @TableField("question_type")
    @NotBlank(message = "题型不能为空")
    private String questionType;

    @Schema(description = "题数")
    @TableField("question_num")
    @NotNull(message = "问题数不能为空")
    private Integer questionNum;

    @Schema(description = "分数")
    @TableField("score")
    @NotNull(message = "分数不能为空")
    private Integer score;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
