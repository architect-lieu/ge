package com.huhuhu.project.controller.admin;


import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.QuestionType;
import com.huhuhu.project.service.QuestionTypeService;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/admin/api/project/question-type")
@Tag(name = "管理端题型API")
@RequiredArgsConstructor
public class QuestionTypeController {

    private final QuestionTypeService questionTypeService;

    @PostMapping("/add")
    @Operation(summary = "添加题型")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) QuestionType questionType) {
        return CommonResult.success(questionTypeService.add(questionType));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改题型")
    public CommonResult<Boolean> modify(@RequestBody  @Validated({Default.class, SystemConstant.Update.class}) QuestionType questionType) {
        return CommonResult.success(questionTypeService.modify(questionType));
    }

    @GetMapping("/all/list")
    @Operation(summary = "题型列表")
    public CommonResult<List<QuestionType>> allList(
            @Parameter(description = "查询的关键字")
            @RequestParam(required = false) String keyword) {
        return CommonResult.success(questionTypeService.allList(keyword));
    }
}

