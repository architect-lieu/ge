package com.huhuhu.project.controller.customer;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.VipConfig;
import com.huhuhu.project.form.params.VipConfigPageParams;
import com.huhuhu.project.service.VipConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 03:24:30
 */
@RestController
@RequestMapping("/customer/api/project/vip-config")
@Tag(name = "vip配置接口")
@RequiredArgsConstructor
public class CustomerVipConfigController {

    private final VipConfigService vipConfigService;

    @PostMapping("/list")
    @Operation(summary = "分页查询vip配置")
    public CommonResult<Page<VipConfig>> list(@RequestBody VipConfigPageParams vipConfigPageParams) {
        return CommonResult.success(vipConfigService.pageList(vipConfigPageParams));
    }

    @GetMapping("/detail")
    @Operation(summary = "详情")
    public CommonResult<VipConfig> detail(Long id) {
        return CommonResult.success(vipConfigService.detail(id));
    }

}

