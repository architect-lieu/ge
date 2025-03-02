package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Paper;
import com.huhuhu.project.form.params.PaperPageParams;
import com.huhuhu.project.service.PaperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.groups.Default;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@RestController
@RequestMapping("/admin/api/project/paper")
@Tag(name = "管理端章节下试题集API")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;

    @PostMapping("/add")
    @Operation(summary = "添加试题集")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) Paper paper) {
        return CommonResult.success(paperService.add(paper));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改试题集")
    public CommonResult<Boolean> modify(@RequestBody @Validated({Default.class, SystemConstant.Update.class}) Paper paper) {
        return CommonResult.success(paperService.modify(paper));
    }

    @PostMapping("/list")
    @Operation(summary = "试题集列表")
    public CommonResult<Page<Paper>> pageList(@RequestBody PaperPageParams params) {
        return CommonResult.success(paperService.pageList(params));
    }

    @GetMapping("/detail")
    @Operation(summary = "题集详情")
    public CommonResult<Paper> detail(Long id) {
        return CommonResult.success(paperService.detail(id));
    }
}

