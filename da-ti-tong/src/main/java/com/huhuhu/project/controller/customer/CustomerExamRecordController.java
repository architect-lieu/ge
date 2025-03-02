package com.huhuhu.project.controller.customer;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.ExamRecord;
import com.huhuhu.project.form.dto.ExamRecordPageParams;
import com.huhuhu.project.service.ExamRecordService;
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
@RequestMapping("/customer/api/project/examRecord")
@Tag(name = "考试记录API")
@RequiredArgsConstructor
public class CustomerExamRecordController {

    private final ExamRecordService examRecordService;

    @PostMapping("/add")
    @Operation(summary = "添加考试记录")
    public CommonResult<Boolean> addRecord(@RequestBody @Validated(Default.class) ExamRecord examRecord) {
        return CommonResult.success(examRecordService.addRecord(examRecord));
    }

    @PostMapping("/list/record")
    @Operation(summary = "分页查询考试记录")
    public CommonResult<Page<ExamRecord>> pageList(@RequestBody ExamRecordPageParams examRecordPageParams) {
        return CommonResult.success(examRecordService.pageList(examRecordPageParams));
    }

    @GetMapping("/detail")
    @Operation(summary = "记录的详情")
    public CommonResult<ExamRecord> detail(Long id) {
        return CommonResult.success(examRecordService.detail(id));
    }

}

