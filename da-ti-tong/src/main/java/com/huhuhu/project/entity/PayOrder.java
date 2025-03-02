package com.huhuhu.project.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
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
 * @since 2023-05-02 10:14:18
 */
@Getter
@Setter
@TableName("dtt_pay_order")
@Schema(description = "PayOrder对象")
public class PayOrder {

    @Schema(description = "支付订单id")
    @TableId(value = "pay_order_id", type = IdType.AUTO)
    private Long payOrderId;

    @Schema(description = "支付订单生成的序列号")
    private String outTradeNo;

    @Schema(description = "1支付 0 未支付")
    @TableField("pay_status")
    private Boolean payStatus;

    @Schema(description = "会员类型")
    @TableField("pay_order_type")
    private String payOrderType;

    @Schema(description = "价格")
    @TableField("price")
    private BigDecimal price;

    @Schema(description = "会员价格类型")
    @TableField("vip_price_type")
    private String vipPriceType;

    @Schema(description = "真实价格")
    @TableField("real_price")
    private BigDecimal realPrice;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "用户标识")
    @TableField("openid")
    private String openid;

    @Schema(description = "请求支付返回的  小程序根据这个去支付")
    @TableField("prepay_id")
    private String prepayId;

    @Schema(description = "退款标识")
    @TableField("refund_flag")
    private Boolean refundFlag;

    @Schema(description = "调用微信方返回的数据体")
    private String resultBody;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField("vip_code")
    private Integer vipCode;
}
