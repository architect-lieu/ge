package com.huhuhu.project.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/12 19:09
 */
@Data
public class CategoryVo {
    @Schema(description = "题目 文档分类ID")
    private Long categoryId;
    @Schema(description = "分类名称")
    private String categoryName;
    @Schema(description = "父节点ID")
    private Long parentId;
    @Schema(description = "父节点标识")
    private Boolean parentFlag;
    @Schema(description = "几级分类", hidden = true)
    private Integer level;
    @Schema(description = "子节点")
    private List<CategoryVo> children;
    @Schema(description = "用户收藏标识")
    private Boolean userCollectFlag;
    private Date updateTime;
    private Long questionNum;
}