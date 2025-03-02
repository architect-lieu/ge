package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
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
@TableName(value = "dtt_question", autoResultMap = true)
@Schema(description = "Question对象")
public class  Question {

    @TableId(value = "question_id", type = IdType.AUTO)
    @NotNull(message = "问题ID不能为空", groups = SystemConstant.Update.class)
    private Long questionId;

    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "管理端返回分类数据", hidden = true)
    @TableField(exist = false)
    private Category subject;

    @Schema(description = "科目ID")
    @TableField("category_pid")
    private Long categoryPid;

    @Schema(description = "所属试题集ID")
    @TableField(exist = false)
    private Set<Long> paperIds;

    @Schema(description = "管理端的会返回", hidden = true)
    @TableField(exist = false)
    private List<Paper> paperList;

    @Schema(description = "问题")
    @TableField("question_title")
    @NotBlank(message = "问题标题不能为空")
    private String questionTitle;

    @Schema(description = "问题类型编码")
    @TableField("question_type_code")
//    @NotBlank(message = "问题类型编码不能为空")
    private String questionTypeCode;

    @Schema(description = "问题类型名称")
    @TableField("question_type_name")
    private String questionTypeName;

    @Schema(description = "选项")
    @TableField(value = "options", typeHandler = FastjsonTypeHandler.class)
    private List<String> options = new ArrayList<>();

    @Schema(description = "正确选项")
    @TableField(value = "right_options", typeHandler = FastjsonTypeHandler.class)
    private List<String> rightOptions = new ArrayList<>();

    @Schema(description = "难度： 简单 中等 困难")
    @TableField("difficulty")
    private String difficulty;

    @Schema(description = "正确回答[ 填空或者简答题 ]")
    @TableField("right_answer")
    private String rightAnswer;

    @Schema(description = "问题解析")
    private String analysis;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private Boolean collectFlag = Boolean.FALSE;

    @TableField(exist = false)
    private Boolean errorHistoryFlag;

    @TableField(exist = false)
    private ErrorQuestionRecord errorQuestionRecord;
}
