package com.huhuhu.project.controller.customer;


import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.QuestionType;
import com.huhuhu.project.service.QuestionTypeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/customer/api/project/questionType")
@Tag(name = "题型API")
@RequiredArgsConstructor
public class CustomerQuestionTypeController {

    private final QuestionTypeService questionTypeService;

    @GetMapping("/all/list")
    @Operation(summary = "题型列表")
    public CommonResult<List<QuestionType>> allList(
            @Parameter(description = "查询的关键字")
            @RequestParam(required = false) String keyword) {
        return CommonResult.success(questionTypeService.allList(keyword));
    }

}

