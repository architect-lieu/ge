package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author KangXin
 * @since 2023-06-18 03:27:43
 */
@Getter
@Setter
@TableName("dtt_organization")
@Schema(description = "Organization对象")
public class Organization {

    @Schema(description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "ID不能为空")
    private Long id;

    @Schema(description = "机构名称")
    @TableField("org_name")
    @NotEmpty(message = "机构名称不能为空")
    private String orgName;

    @Schema(description = "机构编码")
    @TableField("org_code")
    @NotEmpty(message = "机构编码不能为空")
    private String orgCode;

    @Schema(description = "机构的激活码",hidden = true)
    @TableField("active_code")
    private String activeCode;

    @Schema(description = "最大激活次数")
    @TableField("max_active_times")
    @NotNull(message = "最大激活次数不能为空")
    private Integer maxActiveTimes;

    @Schema(description = "vip过期时间")
    @TableField("vip_expiration_time")
    @NotNull(message = "vip过期时间不能为空")
    private Date vipExpirationTime;

    @Schema(description = "vip标记 哪种类型的vip")
    @TableField("vip_flag")
    @NotNull(message = "vip标记不能为空")
    private Integer vipFlag;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
