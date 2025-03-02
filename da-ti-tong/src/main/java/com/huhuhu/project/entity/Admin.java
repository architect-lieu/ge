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
 * @since 2023-03-13 10:18:48
 */
@Getter
@Setter
@TableName("dtt_admin")
@Schema(description = "Admin对象")
public class Admin {

    @TableId(value = "admin_id", type = IdType.AUTO)
    private Long adminId;

    @TableField("admin_name")
    private String adminName;

    @TableField("admin_password")
    private String adminPassword;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
