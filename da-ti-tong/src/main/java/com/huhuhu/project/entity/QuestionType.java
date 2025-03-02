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
@TableName("dtt_question_type")
@Schema(description = "QuestionType对象")
public class QuestionType {

    @Schema(description = "题型id")
    @TableId(value = "question_type_id", type = IdType.AUTO)
    @NotNull(message = "题型id不能为空", groups = SystemConstant.Update.class)
    private Long questionTypeId;

    @Schema(description = "题型名称")
    @TableField("question_type_name")
    @NotBlank(message = "题型名称不能为空")
    private String questionTypeName;

    @Schema(description = "题型编码")
    @TableField("question_type_code")
    @NotBlank(message = "题型编码不能为空")
    private String questionTypeCode;

    @Schema(description = "问题数量")
    @TableField("question_num")
    private Long questionNum;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
