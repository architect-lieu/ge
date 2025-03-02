package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

import com.huhuhu.project.common.constant.SystemConstant;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 03:24:30
 */
@Getter
@Setter
@TableName("dtt_vip_config")
@Schema(description = "VipConfig对象")
public class VipConfig {

    @TableId(value = "vip_config_id", type = IdType.AUTO)
    @NotNull(message = "id不能为空", groups = SystemConstant.Update.class)
    private Long vipConfigId;

    @Schema(description = "vip类型 普通会员  会员  超级会员")
    @TableField("vip_type")
    @NotEmpty(message = "会员了类型不能为空")
    private String vipType;

    @Schema(description = "价格")
    @TableField("month_price")
    @NotNull(message = "价格不能为空")
    private BigDecimal monthPrice;

    @Schema(description = "季度价格")
    @TableField("quarter_price")
    @NotNull(message = "季度价格不能为空")
    private BigDecimal quarterPrice;

    @Schema(description = "年价格")
    @TableField("year_price")
    @NotNull(message = "年度价格不能为空")
    private BigDecimal yearPrice;

    @Schema(description = "每月可以搜题次数")
    @TableField("month_search_question_num")
    private Integer monthSearchQuestionNum;

    @Schema(description = "每月文档可以下载次数")
    @TableField("month_download_doc_num")
    private Integer monthDownloadDocNum;

    @Schema(description = "可刷题库数")
    @TableField("category_num")
    private Integer categoryNum;

    @Schema(description = "可以收藏的题库数")
    @TableField("collect_category_num")
    private Integer collectCategoryNum;

    @Schema(description = "练习记录保存最大天数")
    @TableField("exercise_record_max_day")
    private Integer exerciseRecordMaxDay;

    @Schema(description = "高频错题权限 1有 - 0无")
    @TableField("high_frequency_error")
    private Boolean highFrequencyError;

    @Schema(description = "模拟考试权限")
    @TableField("mock_examination")
    private Boolean mockExamination;

    @Schema(description = "模拟考试权限记录")
    @TableField("mock_examination_record")
    private Boolean mockExaminationRecord;

    @Schema(description = "选项乱序")
    @TableField("option_out_of_order")
    private Boolean optionOutOfOrder;

    @Schema(description = "错题优先")
    @TableField("error_priority")
    private Boolean errorPriority;

    @Schema(description = "未做优先")
    @TableField("undone_priority")
    private Boolean undonePriority;

    @Schema(description = "做题没广告")
    @TableField("question_without_ads")
    private Boolean questionWithoutAds;

    @Schema(description = "搜题没广告")
    @TableField("search_without_ads")
    private Boolean searchWithoutAds;

    @Schema(description = "做题统计")
    @TableField("data_statistic")
    private Boolean dataStatistic;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
