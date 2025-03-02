package com.huhuhu.project.form.dto;

import com.huhuhu.project.form.params.BasePageParams;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/14 16:24
 */
@Data
public class ExamRecordPageParams extends BasePageParams {
    @Schema(description = "开始时间")
    private Date startTime;
    @Schema(description = "结束时间")
    private Date endTime;
}
