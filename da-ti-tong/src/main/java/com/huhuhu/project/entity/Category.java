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
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;



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
@TableName("dtt_category")
@Schema(description = "Category对象")
public class Category {

    @Schema(description = "题目 文档分类ID")
    @TableId(value = "category_id", type = IdType.AUTO)
    @NotNull(message = "分类ID不能为空", groups = SystemConstant.Update.class)
    private Long categoryId;

    @Schema(description = "分类名称")
    @TableField("category_name")
    @NotBlank(message = "分类名称不能为空")
    private String categoryName;

    @Schema(name = "父节点ID", description = "在更新的时候需要传")
    @TableField("parent_id")
    private Long parentId;

    @Schema(name = "父节点标识", hidden = true)
    @TableField("parent_flag")
    private Boolean parentFlag;

    @Schema(description = "几级分类", hidden = true)
    @TableField("level")
    private Integer level;

    @Schema(description = "创建时间", hidden = true)
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
