package com.huhuhu.project.controller.customer;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Question;
import com.huhuhu.project.form.params.QuestionPageParams;
import com.huhuhu.project.form.params.SearchByBase64Params;
import com.huhuhu.project.service.QuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.huhuhu.project.common.exception.enums.ResultCode.IMAGE_PARSE_ERROR;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@RestController
@RequestMapping("/customer/api/project/question")
@Tag(name = "问题API")
@RequiredArgsConstructor
public class CustomerQuestionController {

    private final QuestionService questionService;

    @PostMapping("/list")
    @Operation(summary = "问题分页列表")
    public CommonResult<Page<Question>> pageList(@RequestBody QuestionPageParams questionPageParams) {
        return CommonResult.success(questionService.pageList(questionPageParams));
    }

    @GetMapping("/type/statistic")
    @Operation(summary = "题型统计[ 看需求调用 ]")
    public CommonResult<Map<String, Map<String, Object>>> typeStatistic(Long categoryId) {
        return CommonResult.success(questionService.typeStatistic(categoryId));
    }

    @GetMapping("/paper/statistic")
    @Operation(summary = "试题集统计[ 看需求调用 ]")
    public CommonResult<Map<String, List<Map<String, Object>>>> paperStatistic(Long categoryId, Boolean trueQuestionChapterFlag) {
        return CommonResult.success(questionService.paperStatistic(categoryId, trueQuestionChapterFlag));
    }

    @PostMapping("/image/search")
    @Operation(summary = "拍照查题-文件流")
    public CommonResult<Page<Question>> imageSearch(MultipartFile file,
                                                    @RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        try {
            return CommonResult.success(questionService.imageSearch(file, page, size));
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(IMAGE_PARSE_ERROR);
        }
    }

    @PostMapping("/image/search-by-base64")
    @Operation(summary = "拍照查题-base64")
    public CommonResult<Page<Question>> imageSearchByBase64(@RequestBody @Validated SearchByBase64Params searchByBase64Params) {
        return CommonResult.success(questionService.imageSearchByBase64(searchByBase64Params));
    }
}

