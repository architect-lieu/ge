package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/13 16:15
 */
@Data
public class DocumentPageParams extends BasePageParams{
    @Schema(description = "文档名称")
    private String docName;
    @Schema(description = "类型ID")
    private Long categoryId;
    @Schema(description = "查询热门文档")
    private Boolean hot;
}
