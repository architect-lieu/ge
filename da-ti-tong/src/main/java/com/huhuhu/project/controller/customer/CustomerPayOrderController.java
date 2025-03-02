package com.huhuhu.project.controller.customer;


import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.form.params.PayVipParams;
import com.huhuhu.project.service.PayOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 10:14:18
 */
@RestController
@RequestMapping("/customer/api/project/pay-order")
@Tag(name = "用户支付订单API")
@RequiredArgsConstructor
public class CustomerPayOrderController {

    private final PayOrderService payOrderService;

    @PostMapping("/pay")
    @Operation(summary = "支付")
    public CommonResult<Map<String, String>> pay(@RequestBody PayVipParams params) {
        return CommonResult.success(payOrderService.pay(params));
    }

    @PostMapping("/h5pay")
    @Operation(summary = "支付")
    public CommonResult<Map<String, String>> h5pay(@RequestBody PayVipParams params, HttpServletRequest request) {
        return CommonResult.success(payOrderService.h5pay(params,request));
    }
}

