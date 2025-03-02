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
@TableName("dtt_paper")
@Schema(description = "Paper对象")
public class Paper {

    @Schema(description = "卷子id")
    @TableId(value = "paper_id", type = IdType.AUTO)
    @NotNull(message = "试题集ID不能为空", groups = SystemConstant.Update.class)
    private Long paperId;

    @Schema(description = "卷子名")
    @TableField("paper_name")
    @NotBlank(message = "试题集名称不能为空")
    private String paperName;

    @Schema(description = "所属章节ID")
    @TableField("chapter_id")
    @NotNull(message = "所属章节ID")
    private Long chapterId;

    @Schema(description = "题目数量")
    @TableField("question_num")
    private Long questionNum;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
