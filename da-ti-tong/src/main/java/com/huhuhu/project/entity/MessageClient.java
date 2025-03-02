package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

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
@TableName("dtt_message_client")
@Schema(description = "MessageClient对象")
public class MessageClient {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "客户id")
    @TableField("client_id")
    private Long clientId;

    @Schema(description = "消息id")
    @TableField("message_id")
    private Long messageId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


}
