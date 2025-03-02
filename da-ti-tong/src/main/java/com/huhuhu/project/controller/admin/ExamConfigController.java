package com.huhuhu.project.controller.admin;


import cn.hutool.core.collection.CollUtil;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.form.dto.ExamConfigForm;
import com.huhuhu.project.service.ExamConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.groups.Default;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@RestController
@RequestMapping("/admin/api/project/examConfig")
@Tag(name = "分类下考试通用配置API")
@RequiredArgsConstructor
public class ExamConfigController {

    private final ExamConfigService examConfigService;

    @PostMapping("/add")
    @Operation(summary = "添加考试规则")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) ExamConfigForm examConfigForm) {
        if (CollUtil.isEmpty(examConfigForm.getExamConfigs())) {
            throw new BusinessException(ResultCode.LIST_DATA_IS_EMPTY);
        }
        return CommonResult.success(examConfigService.add(examConfigForm));
    }

    @GetMapping("/detail")
    @Operation(summary = "规则详情")
    public CommonResult<ExamConfigForm> detail(Long categoryId) {
        return CommonResult.success(examConfigService.detail(categoryId));
    }

    @PostMapping("/del")
    @Operation(summary = "删除规则")
    public CommonResult<Boolean> del(@RequestBody List<Long> ids) {
        return CommonResult.success(examConfigService.removeByIds(ids));
    }
}

