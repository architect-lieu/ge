package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
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
 * @since 2023-03-12 06:15:19
 */
@Getter
@Setter
@TableName(value = "dtt_document", autoResultMap = true)
@Schema(description = "Document对象")
public class Document {

    @TableId(value = "doc_id", type = IdType.AUTO)
    @NotNull(message = "文档ID不能为空", groups = SystemConstant.Update.class)
    private Long docId;

    @Schema(description = "文档名称")
    @TableField("doc_name")
    @NotBlank(message = "文档名称不能为空")
    private String docName;

    @TableField("hot_flag")
    @Schema(description = "热门标识")
    private Boolean hotFlag;

    @TableField("free_flag")
    @Schema(description = "免费标识")
    private Boolean freeFlag;

    @TableField("cover_image")
    @Schema(description = "封面")
    private String coverImage;

    @Schema(description = "下载路径")
    @TableField("doc_download_url")
    @NotBlank(message = "下载路径不能为空")
    private String docDownloadUrl;

    @Schema(description = "类型ID")
    @NotNull(message = "类型ID不能为空")
    private Long categoryId;

    @NotNull
    private Long categoryPid;

    @Schema(description = "文档类型")
    @TableField("type")
    private String type;

    @Schema(description = "下载标识")
    @TableField("download_flag")
    private String downloadFlag;

    @Schema(description = "用于获取临时下载地址")
    @NotBlank(message = "用于获取临时下载地址的文件key不能为空")
    private String downloadUrlKey;

    @Schema(description = "文档标签")
    @TableField(value = "tags", typeHandler = FastjsonTypeHandler.class)
    private List<String> tags;

    @Schema(description = "预览图片")
    @TableField(value = "preview_images", typeHandler = FastjsonTypeHandler.class)
    private List<String> previewImages;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Schema(description = "下载数量")
    @TableField(exist = false)
    private Integer downloadNum;

}
