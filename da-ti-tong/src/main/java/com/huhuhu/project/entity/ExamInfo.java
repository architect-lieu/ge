package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.huhuhu.project.common.constant.SystemConstant;
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
 * @since 2023-07-01 08:07:05
 */
@Getter
@Setter
@TableName(value = "dtt_exam_info", autoResultMap = true)
@Schema(description = "ExamInfo对象")
public class ExamInfo {

    @Schema(description = "考试信息ID")
    @TableId(value = "exam_info_id", type = IdType.AUTO)
    @NotNull(message = "考试信息ID不能为空", groups = SystemConstant.Update.class)
    private Long examInfoId;

    @Schema(description = "考试信息标题")
    @TableField("exam_info_title")
//    @NotBlank(message = "考试信息标题不能为空")
    private String examInfoTitle;

    @Schema(description = "考试信息内容")
    @TableField(value = "exam_info_content", typeHandler = FastjsonTypeHandler.class)
    private Map<String, Object> examInfoContent;

    @Schema(description = "分类id")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @TableField("subject_id")
    @Schema(description = "科目ID")
    @NotNull(message = "科目ID不能为空")
    private Long subjectId;

    @TableField(exist = false)
    private Category subject;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Data
    public static class InfoNode {
        private String key;
        private String value;
    }
}