package com.huhuhu.project.service.impl;

import cn.felord.payment.wechat.v3.WechatApiProvider;
import cn.felord.payment.wechat.v3.WechatResponseEntity;
import cn.felord.payment.wechat.v3.model.*;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Customer;
import com.huhuhu.project.entity.PayOrder;
import com.huhuhu.project.entity.VipConfig;
import com.huhuhu.project.enums.UserEnum;
import com.huhuhu.project.enums.VipConfigEnum;
import com.huhuhu.project.form.params.PayVipParams;
import com.huhuhu.project.mapper.CustomerMapper;
import com.huhuhu.project.mapper.PayOrderMapper;
import com.huhuhu.project.mapper.VipConfigMapper;
import com.huhuhu.project.service.PayOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 10:14:18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {

//    private final WechatApiProvider wechatApiProvider;

    private final CustomerMapper customerMapper;

    private final VipConfigMapper vipConfigMapper;

    @Value("${order.pay.callback}")
    private String payNotifyUrl;

    @Value("${order.refund.callback}")
    private String refundNotifyUrl;


    @Override
    @Transactional
    public Map<String, String> pay(PayVipParams params) {
        PayParams payParams = getPayParams(params);
//        WechatResponseEntity<ObjectNode> result = wechatApiProvider.directPayApi("miniapp").jsPay(payParams);
        WechatResponseEntity<ObjectNode> result = null;
        if (result.is2xxSuccessful()) {
            log.info("支付返回：" + JSON.toJSONString(result));
            log.info("支付的主要数据：" + result.getBody().toString());
            Map<String, String> map = new HashMap<>();
            map.put("prepay_id", result.getBody().get("prepay_id").textValue());
            map.put("appId", result.getBody().get("appId").textValue());
            map.put("package", result.getBody().get("package").textValue());
            map.put("nonceStr", result.getBody().get("nonceStr").textValue());
            map.put("paySign", result.getBody().get("paySign").textValue());
            map.put("timeStamp", result.getBody().get("timeStamp").textValue());
            map.put("signType", result.getBody().get("signType").textValue());
            savePayOrderInfo(payParams, result, params);
            return map;
        }
        throw new BusinessException(ResultCode.PAY_ERROR);
    }

    @Override
    public Map<String, String> h5pay(PayVipParams params,HttpServletRequest request) {
        PayParams payParams = geth5PayParams(params,request);
//        WechatResponseEntity<ObjectNode> result = wechatApiProvider.directPayApi("miniapp").h5Pay(payParams);
        WechatResponseEntity<ObjectNode> result = null;
        if (result.is2xxSuccessful()) {
            log.info("支付返回：" + JSON.toJSONString(result));
            log.info("支付的主要数据：" + result.getBody().toString());
            Map<String, String> map = new HashMap<>();
            map.put("prepay_id", result.getBody().get("prepay_id").textValue());
            map.put("appId", result.getBody().get("appId").textValue());
            map.put("package", result.getBody().get("package").textValue());
            map.put("nonceStr", result.getBody().get("nonceStr").textValue());
            map.put("paySign", result.getBody().get("paySign").textValue());
            map.put("timeStamp", result.getBody().get("timeStamp").textValue());
            map.put("signType", result.getBody().get("signType").textValue());
            savePayOrderInfo(payParams, result, params);
            return map;
        }
        throw new BusinessException(ResultCode.PAY_ERROR);
    }

    @Override
    @Transactional
    public Map<String, ?> payCallback(ResponseSignVerifyParams params) {
//        return wechatApiProvider.callback("miniapp").transactionCallback(params, data -> {
//            String outTradeNo = data.getOutTradeNo();
//            // 查询订单 修改订单状态为已经支付
//            this.lambdaUpdate()
//                    .set(PayOrder::getPayStatus, Boolean.TRUE)
//                    .eq(PayOrder::getOutTradeNo, outTradeNo)
//                    .update();
//            PayOrder payOrder = this.getOne(Wrappers.lambdaQuery(PayOrder.class).eq(PayOrder::getOutTradeNo, outTradeNo));
//            String vipPriceType = payOrder.getVipPriceType();
//            Customer customer = customerMapper.selectById(payOrder.getUserId());
//            DateTime time = new DateTime();
//            time.setMutable(false);
//            if (customer.getFistVipPayTime() == null) {
//                customer.setFistVipPayTime(time);
//            }
//            customer.setLastVipPayTime(time);
//            customer.setVipFlag(payOrder.getVipCode());
//            // 查询vip的支付类型
//            if (VipConfigEnum.MONTH_PRICE.getDesc().equals(vipPriceType)) {
//                time = time.offset(DateField.MONTH, 1);
//            }else if (VipConfigEnum.QUARTER_PRICE.getDesc().equals(vipPriceType)) {
//                time = time.offset(DateField.MONTH, 3);
//            }else if (VipConfigEnum.YEAR_PRICE.getDesc().equals(vipPriceType)) {
//                time = time.offset(DateField.YEAR, 1);
//            }
//            customer.setVipExpirationTime(time);
//            customerMapper.updateById(customer);
//        });
        return null;
    }

    @Override
    public void refund(String outTradeNo) {
        // 查询订单
        PayOrder payOrder = this.getOne(Wrappers.lambdaQuery(PayOrder.class).eq(PayOrder::getOutTradeNo, outTradeNo)
                .eq(PayOrder::getPayStatus, 1)
                .eq(PayOrder::getRefundFlag, 0));
        if (payOrder == null) {
            return;
        }
        RefundParams refundParams = new RefundParams();
        refundParams.setOutTradeNo(outTradeNo);
        refundParams.setOutRefundNo(outTradeNo + "_refund");
        RefundParams.RefundAmount refundAmount = new RefundParams.RefundAmount();
        refundAmount.setRefund(payOrder.getRealPrice().multiply(BigDecimal.valueOf(100)).intValue());
        refundAmount.setTotal(payOrder.getRealPrice().multiply(BigDecimal.valueOf(100)).intValue());
        refundParams.setAmount(refundAmount);
        refundParams.setNotifyUrl(refundNotifyUrl);
        refundParams.setReason("会员退款");
//        wechatApiProvider.directPayApi("miniapp").refund(refundParams);
    }

    @Override
    @Transactional
    public Map<String, ?> refundCallBack(ResponseSignVerifyParams params) {
//        return wechatApiProvider.callback("miniapp").refundCallback(params, data -> {
//            String outTradeNo = data.getOutTradeNo();
//            this.lambdaUpdate()
//                    .set(PayOrder::getRefundFlag, 1)
//                    .eq(PayOrder::getOutTradeNo, outTradeNo)
//                    .update();
//            //
//            PayOrder payOrder = this.getOne(Wrappers.lambdaQuery(PayOrder.class).select(PayOrder::getUserId)
//                    .eq(PayOrder::getOutTradeNo, outTradeNo));
//            // 更新用户不是vip过期
//            Customer customer = customerMapper.selectOne(Wrappers.lambdaQuery(Customer.class).eq(Customer::getUserId, payOrder.getUserId())
//                    .select(Customer::getUserId));
//            customer.setVipFlag(UserEnum.IDENTITY_USER.getCode());
//            customerMapper.updateById(customer);
//        });
        return null;
    }

    private PayParams getPayParams(PayVipParams params) {
        // 获取当前用户
        Long userId = TokenUtil.currentLoginUserId();
        Customer customer = customerMapper.selectOne(Wrappers.lambdaQuery(Customer.class)
                .select(Customer::getOpenid).eq(Customer::getUserId, userId));
        PayParams payParams = new PayParams();
        payParams.setNotifyUrl(payNotifyUrl);
        Payer payer = new Payer();
        payer.setOpenid(customer.getOpenid());
        payParams.setPayer(payer);
        String outTradeNo = createOutTradeNo();
        payParams.setOutTradeNo(outTradeNo);
        payParams.setDescription(params.getDesc());
        // 查询vip 配置
        Long vipId = params.getVipId();
        VipConfig vipConfig = vipConfigMapper.selectOne(Wrappers.lambdaQuery(VipConfig.class).eq(VipConfig::getVipConfigId, vipId));
        BigDecimal price;
        if (vipConfig == null) {
            price = new BigDecimal("0.01");
        }else {
            price = vipConfig.getMonthPrice();
            if (VipConfigEnum.QUARTER_PRICE.getDesc().equals(params.getPayType())) {
                price = vipConfig.getQuarterPrice();
            }else if (VipConfigEnum.YEAR_PRICE.getDesc().equals(params.getPayType())) {
                price = vipConfig.getYearPrice();
            }
        }
        payParams.setAmount(createAmount(price));
        return payParams;
    }

    private PayParams geth5PayParams(PayVipParams params,HttpServletRequest request) {
        // 获取当前用户
        Long userId = TokenUtil.currentLoginUserId();
        Customer customer = customerMapper.selectOne(Wrappers.lambdaQuery(Customer.class)
                .select(Customer::getOpenid).eq(Customer::getUserId, userId));
        PayParams payParams = new PayParams();
        payParams.setNotifyUrl(payNotifyUrl);
        String ipAddr = getIpAddr(request);
        SceneInfo sceneInfo = createSceneInfo(ipAddr);
        payParams.setSceneInfo(sceneInfo);
        String outTradeNo = createOutTradeNo();
        payParams.setOutTradeNo(outTradeNo);
        payParams.setDescription(params.getDesc());
        // 查询vip 配置
        Long vipId = params.getVipId();
        VipConfig vipConfig = vipConfigMapper.selectOne(Wrappers.lambdaQuery(VipConfig.class).eq(VipConfig::getVipConfigId, vipId));
        BigDecimal price;
        if (vipConfig == null) {
            price = new BigDecimal("0.01");
        }else {
            price = vipConfig.getMonthPrice();
            if (VipConfigEnum.QUARTER_PRICE.getDesc().equals(params.getPayType())) {
                price = vipConfig.getQuarterPrice();
            }else if (VipConfigEnum.YEAR_PRICE.getDesc().equals(params.getPayType())) {
                price = vipConfig.getYearPrice();
            }
        }
        payParams.setAmount(createAmount(price));
        return payParams;
    }

    private void savePayOrderInfo(PayParams payParams, WechatResponseEntity<ObjectNode> body, PayVipParams params) {
        PayOrder payOrder = new PayOrder();
        payOrder.setOutTradeNo(payParams.getOutTradeNo());
        payOrder.setPayOrderType(payParams.getDescription());
        payOrder.setOpenid(payParams.getPayer().getOpenid());
        payOrder.setPayStatus(Boolean.FALSE);
        payOrder.setRefundFlag(Boolean.FALSE);
        payOrder.setUserId(TokenUtil.currentLoginUserId());
        payOrder.setPrice(BigDecimal.valueOf(payParams.getAmount().getTotal()).divide(BigDecimal.valueOf(100), 2,RoundingMode.HALF_UP));
        payOrder.setRealPrice(BigDecimal.valueOf(payParams.getAmount().getTotal()).divide(BigDecimal.valueOf(100), 2,RoundingMode.HALF_UP));
        payOrder.setPrepayId(body.getBody().get("prepay_id").textValue());
        payOrder.setResultBody(body.getBody().toString());
        payOrder.setVipPriceType(params.getPayType());
        payOrder.setVipCode(params.getVipCode());
        this.save(payOrder);
    }

    private String createOutTradeNo() {
        String outTradeNo = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        outTradeNo += TokenUtil.getClaims().get("vipFlag");
        outTradeNo += RandomUtil.randomNumbers(11);
        return outTradeNo;
    }

    private Amount createAmount(BigDecimal price) {
        Amount amount = new Amount();
        amount.setTotal(price.multiply(BigDecimal.valueOf(100)).intValue());
        return amount;
    }

    private SceneInfo createSceneInfo(String payer_client_ip) {
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setPayerClientIp(payer_client_ip);
        H5Info h5Info = new H5Info();
        h5Info.setType(H5Info.H5SceneType.valueOf("Wap"));//场景类型：iOS, Android, Wap
        sceneInfo.setH5Info(h5Info);
        return sceneInfo;
    }

    /**
     * 获取用户实际ip
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

}
