package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.form.params.CustomerPageParams;
import com.huhuhu.project.service.CustomerService;
import com.huhuhu.project.vo.CustomerVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@RestController
@RequestMapping("/admin/api/project/customer")
@Tag(name = "管理端客户API")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerService customerService;

    @PostMapping("/list")
    @Operation(summary = "用户列表")
    public CommonResult<Page<CustomerVo>> customerList(@RequestBody CustomerPageParams customerPageParams) {
        return CommonResult.success(customerService.customerList(customerPageParams));
    }

    @GetMapping("/detail")
    @Operation(summary = "用户详情")
    public CommonResult<CustomerVo> detail(Long id) {
        return CommonResult.success(customerService.detail(id));
    }

    @PostMapping("/export")
    @Operation(summary = "导出用户数据")
    public void export(HttpServletResponse response, @RequestBody CustomerPageParams customerPageParams) {
        customerService.export(response, customerPageParams);
    }
}

