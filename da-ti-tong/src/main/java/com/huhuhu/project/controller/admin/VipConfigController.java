package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.VipConfig;
import com.huhuhu.project.form.params.VipConfigPageParams;
import com.huhuhu.project.service.VipConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.groups.Default;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 03:24:30
 */
@RestController
@RequestMapping("/admin/api/project/vip-config")
@Tag(name = "vip配置接口")
@RequiredArgsConstructor
public class VipConfigController {

    private final VipConfigService vipConfigService;

    @PostMapping("/add")
    @Operation(summary = "添加配置")
    public CommonResult<Boolean> add(@RequestBody @Validated({SystemConstant.Add.class, Default.class}) VipConfig vipConfig) {
        return CommonResult.success(vipConfigService.add(vipConfig));
    }

    @PostMapping("/modify")
    @Operation(summary = "更新配置")
    public CommonResult<Boolean> modify(@RequestBody @Validated({SystemConstant.Update.class, Default.class}) VipConfig vipConfig) {
        return CommonResult.success(vipConfigService.modify(vipConfig));
    }

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

