package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/13 12:55
 */
@Data
public class PaperPageParams extends BasePageParams{
    @Schema(description = "卷子名")
    private String paperName;
    @Schema(description = "所属章节ID")
    private Long chapterId;
}
