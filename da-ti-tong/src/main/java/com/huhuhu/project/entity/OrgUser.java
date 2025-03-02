package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-06-18 04:21:09
 */
@Getter
@Setter
@TableName("dtt_org_user")
@Schema(description = "OrgUser对象")
public class OrgUser {

    @Schema(description = "激活记录id")
    @TableId("id")
    private Long id;

    @Schema(description = "机构id")
    @TableField("org_id")
    private Long orgId;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


}
