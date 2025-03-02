package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
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
@TableName("dtt_exam_record")
@Schema(description = "ExamRecord对象")
public class ExamRecord {

    @Schema(description = "考试记录id")
    @TableId(value = "exam_record_id", type = IdType.AUTO)
    private Long examRecordId;

    @Schema(description = "用户id")
    @TableField("user_id")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "分数是总分")
    @TableField("score")
    @NotNull(message = "分数不能为空")
    private Integer score;

    @TableField(value = "exam_data", typeHandler = FastjsonTypeHandler.class)
    private ExamRule examData;

    @Schema(description = "错误数")
    @TableField("error_num")
    @NotNull(message = "错误数不能为空")
    private Integer errorNum;

    @Schema(description = "成功数")
    @TableField("success_num")
    @NotNull(message = "成功数不能为空")
    private Integer successNum;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Data
    public static class ExamRule {
        @Schema(description = "体型规则")
        private List<ExamConfig> examConfigs;

        @Schema(description = "考试时长")
        private Integer timeNum = 45;

        @Schema(description = "是否答题后显示答案")
        private Boolean showAnswer = Boolean.FALSE;

        @Schema(description = "选项乱序")
        private Boolean selectOutOfOrder = Boolean.FALSE;

        @Schema(description = "错题优先")
        private Boolean firstErrorQuestion = Boolean.FALSE;

        @Schema(description = "为答题优先")
        private Boolean notAnswerQuestionFirst;

        @Schema(description = "计分模式 ： 前端定义")
        private Integer computeScoreType;

        @Schema(description = "回答问题的结果")
        private List<AnswerResult> answerResults;
    }

    @Data
    public static class AnswerResult {
        @Schema(description = "问题ID")
        private Long questionId;
        @Schema(description = "回答的答案")
        private List<String> answers;
        @Schema(description = "是否正确")
        private Boolean rightFlag;
    }
}
