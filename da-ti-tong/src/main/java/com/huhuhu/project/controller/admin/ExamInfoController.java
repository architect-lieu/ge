package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.ExamInfo;
import com.huhuhu.project.form.params.ExamInfoPageParams;
import com.huhuhu.project.service.ExamInfoService;
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
 * @since 2023-07-01 08:07:05
 */
@RestController
@RequestMapping("/admin/api/project/exam-info")
@RequiredArgsConstructor
@Tag(name = "管理端考试信息API")
public class ExamInfoController {

    private final ExamInfoService examInfoService;

    @Operation(summary = "添加考试信息")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) ExamInfo examInfo) {
        return CommonResult.success(examInfoService.add(examInfo));
    }

    @Operation(summary = "修改考试信息")
    @PostMapping("/modify")
    public CommonResult<Boolean> modify(@RequestBody @Validated({Default.class, SystemConstant.Update.class}) ExamInfo examInfo) {
        return CommonResult.success(examInfoService.modify(examInfo));
    }

    @Operation(summary = "考试信息分页")
    @PostMapping("/list")
    public CommonResult<Page<ExamInfo>> pageList(@RequestBody ExamInfoPageParams param) {
        return CommonResult.success(examInfoService.pageList(param));
    }

    @Operation(summary = "考试信息详情")
    @GetMapping("/detail")
    public CommonResult<ExamInfo> detail(Long id) {
        return CommonResult.success(examInfoService.detail(id));
    }

    @Operation(summary = "获取科目下的考试信息")
    @GetMapping("/detail-by-subject")
    public CommonResult<ExamInfo> detailBySubject(Long subjectId) {
        return CommonResult.success(examInfoService.detailBySubject(subjectId));
    }
}

