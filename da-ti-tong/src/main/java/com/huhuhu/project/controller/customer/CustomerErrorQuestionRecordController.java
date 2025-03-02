package com.huhuhu.project.controller.customer;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.ErrorQuestionRecord;
import com.huhuhu.project.form.dto.QuestionRecordDto;
import com.huhuhu.project.service.ErrorQuestionRecordService;
import com.huhuhu.project.utils.TokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
@RequestMapping("/customer/api/project/error-question-record")
@Tag(name = "客户端错题API")
@RequiredArgsConstructor
public class CustomerErrorQuestionRecordController {

    private final ErrorQuestionRecordService errorQuestionRecordService;

    @PostMapping("/add")
    @Operation(summary = "添加错题记录")
    public CommonResult<Boolean> add(@RequestBody ErrorQuestionRecord errorQuestionRecord) {
        return CommonResult.success(errorQuestionRecordService.add(errorQuestionRecord));
    }

    @PostMapping("/add-batch")
    @Operation(summary = "批量添加做题记录")
    public CommonResult<Boolean> addBatch(@RequestBody @Validated QuestionRecordDto questionRecord) {
        return CommonResult.success(errorQuestionRecordService.addBatch(questionRecord));
    }

    @GetMapping("/list")
    @Operation(summary = "做题记录分页")
    public CommonResult<Page<ErrorQuestionRecord>> pageList(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "10") int size,
                                                            @RequestParam(value = "categoryId", required = false) Long categoryId,
                                                            @RequestParam(value = "status", required = false) Integer status) {
        return CommonResult.success(errorQuestionRecordService.pageList(page, size, categoryId, status));
    }

    @GetMapping("/clear")
    @Operation(summary = "清空记录")
    public CommonResult<Boolean> clear(Integer type) {
        return CommonResult.success(errorQuestionRecordService.remove(Wrappers.lambdaQuery(ErrorQuestionRecord.class)
                .eq(ErrorQuestionRecord::getUserId, TokenUtil.currentLoginUserId())
                .eq(ErrorQuestionRecord::getStatus, type)));
    }

    @GetMapping("/error-question")
    @Operation(summary = "错题记录根据科目分类")
    public CommonResult<List<Map<String, Object>>> errorQuestion () {
        return CommonResult.success(errorQuestionRecordService.errorQuestion());
    }
}

