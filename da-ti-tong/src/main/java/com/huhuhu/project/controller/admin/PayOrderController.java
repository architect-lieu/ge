package com.huhuhu.project.controller.admin;


import com.huhuhu.project.service.PayOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 10:14:18
 */
@RestController
@RequestMapping("/admin/api/project/pay-order")
@Tag(name = "用户订单API")
@RequiredArgsConstructor
public class PayOrderController {

    private final PayOrderService payOrderService;

    @GetMapping("/refund")
    @Operation(summary = "退款")
    public void refund(String outTradeNo) {
        payOrderService.refund(outTradeNo);
    }
}

