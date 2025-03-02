package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.*;

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
 * @since 2023-07-01 09:07:37
 */
@Getter
@Setter
@TableName("dtt_message")
@Schema(description = "Message对象")
public class Message {

    @Schema(description = "消息ID")
    @TableId(value = "message_id", type = IdType.AUTO)
    @NotNull(message = "消息ID不能为空", groups = SystemConstant.Update.class)
    private Long messageId;

    @Schema(description = "消息内容")
    @TableField("message_content")
    @NotBlank(message = "消息内容不能为空")
    private String messageContent;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(exist = false)
    private Boolean readFlag;

}
