package com.huhuhu.project.controller.customer;


import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.login.wx.dto.WxPhoneNumDto;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.form.dto.CustomerForm;
import com.huhuhu.project.form.dto.SmLoginDto;
import com.huhuhu.project.form.params.MinAppAuthPhoneParam;
import com.huhuhu.project.form.params.VipActiveParams;
import com.huhuhu.project.service.CustomerService;
import com.huhuhu.project.vo.CustomerVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.groups.Default;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@RestController
@RequestMapping("/customer/api/project/customer")
@Tag(name = "客户端用户API")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/send/sm")
    @Operation(summary = "发送短信")
    public CommonResult<Boolean> sendSm(String phone) {
        return CommonResult.success(customerService.sendSm(phone));
    }

    @PostMapping("/sm/login")
    @Operation(summary = "短信登录")
    public CommonResult<CustomerVo> smLogin(@RequestBody @Validated(Default.class) SmLoginDto smLoginDto) {
        return CommonResult.success(customerService.smLogin(smLoginDto));
    }

    @GetMapping("/min-app/login")
    @Operation(summary = "小程序登录")
    public CommonResult<CustomerVo> minAppLogin(String code) {
        return CommonResult.success(customerService.minAppLogin(code));
    }

    @GetMapping("/userPhoneNum")
    @Operation(summary = "获取用户手机号")
    public CommonResult<WxPhoneNumDto> userPhoneNum(String code) {
        return CommonResult.success(customerService.userPhoneNum(code));
    }

    @PostMapping("/modify")
    @Operation(summary = "更新用户信息")
    public CommonResult<Boolean> modify(@RequestBody @Validated({SystemConstant.Update.class,Default.class}) CustomerForm customerForm) {
        return CommonResult.success(customerService.modify(customerForm));
    }

    @PostMapping("/min-app/phone")
    @Operation(summary = "授权手机号")
    public CommonResult<Boolean> minAppPhone(@RequestBody @Validated(Default.class) MinAppAuthPhoneParam minAppAuthPhoneParam) {
        return CommonResult.success(customerService.minAppPhone(minAppAuthPhoneParam));
    }

    @GetMapping("/vip-statistic")
    @Operation(summary = "会员统计")
    public CommonResult<Map<String, Long>> vipStatistic() {
        return CommonResult.success(customerService.vipStatistic());
    }

    @PostMapping("/vip-active")
    @Operation(summary = "vip激活")
    public CommonResult<Boolean> vipActive(@RequestBody VipActiveParams params) {
        return CommonResult.success(customerService.vipActive(params));
    }
}

