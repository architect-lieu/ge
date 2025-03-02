package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Organization;
import com.huhuhu.project.form.params.OrganizationPageParams;
import com.huhuhu.project.service.OrganizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-06-18 02:51:22
 */
@RestController
@RequestMapping("/admin/api/project/organization")
@Tag(name = "机构API")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/add")
    @Operation(summary = "添加机构")
    public CommonResult<Boolean> add(@RequestBody @Validated(SystemConstant.Add.class) Organization organization) {
        return CommonResult.success(organizationService.add(organization));
    }

    @PostMapping("/modify")
    @Operation(summary = "更新机构")
    public CommonResult<Boolean> modify(@RequestBody @Validated(SystemConstant.Update.class) Organization organization) {
        return CommonResult.success(organizationService.modify(organization));
    }

    @PostMapping("/detail")
    @Operation(summary = "机构详情")
    public CommonResult<Organization> detail(Integer id) {
        return CommonResult.success(organizationService.detail(id));
    }

    @PostMapping("/list")
    @Operation(summary = "分页查询机构")
    public CommonResult<Page<Organization>> pageList(@RequestBody OrganizationPageParams params) {
        return CommonResult.success(organizationService.pageList(params));
    }
}

