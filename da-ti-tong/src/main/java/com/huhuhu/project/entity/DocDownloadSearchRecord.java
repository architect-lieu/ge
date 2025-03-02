package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 02:56:45
 */
@Getter
@Setter
@TableName(value = "dtt_doc_download_record",autoResultMap = true)
@Schema(description = "DocDownloadRecord对象")
public class DocDownloadSearchRecord {

    @TableId(value = "doc_download_search_record_id", type = IdType.AUTO)
    private Long docDownloadSearchRecordId;

    @TableField("user_id")
    private Long userId;

    @TableField("doc_id")
    private Long docId;

    @TableField("type")
    private String type;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private Document document;

    @TableField(value = "keyword", typeHandler = FastjsonTypeHandler.class)
    private List<String> keyword;
}
