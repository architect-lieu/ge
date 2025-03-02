package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author KangXin
 * @since 2023-03-15 10:17:11
 */
@Getter
@Setter
@TableName("dtt_paper_question")
@Schema(description = "PaperQuestion对象")
public class PaperQuestion {

    @TableId(value = "paper_question_id", type = IdType.AUTO)
    private Long paperQuestionId;

    @Schema(description = "试题集id")
    @TableField("paper_id")
    private Long paperId;

    @Schema(description = "问题id")
    @TableField("question_id")
    private Long questionId;


}
