package com.huhuhu.project.service;

import cn.felord.payment.wechat.v3.model.ResponseSignVerifyParams;
import com.huhuhu.project.entity.PayOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.PayVipParams;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 10:14:18
 */
public interface PayOrderService extends IService<PayOrder> {

    /**
     * 支付接口
     * @param params
     * @return
     */
    Map<String, String> pay(PayVipParams params);

    Map<String, String> h5pay(PayVipParams params,HttpServletRequest request);

    /**
     * 支付回调
     * @param params
     * @return
     */
    Map<String,?> payCallback(ResponseSignVerifyParams params);

    /**
     * 退款
     * @param outTradeNo
     */
    void refund(String outTradeNo);

    /**
     * 退款回调
     * @param params
     * @return
     */
    Map<String,?> refundCallBack(ResponseSignVerifyParams params);
}
