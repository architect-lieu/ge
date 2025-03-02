package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Question;
import com.huhuhu.project.form.params.QuestionPageParams;
import com.huhuhu.project.service.QuestionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.groups.Default;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@RestController
@RequestMapping("/admin/api/project/question")
@Tag(name = "管理端试题API")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/add")
    @Operation(summary = "添加试题")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) Question question) {
        return CommonResult.success(questionService.add(question));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改试题")
    public CommonResult<Boolean> modify(@RequestBody @Validated({Default.class, SystemConstant.Update.class}) Question question) {
        return CommonResult.success(questionService.modify(question));
    }

    @PostMapping("/list")
    @Operation(summary = "问题分页列表")
    public CommonResult<Page<Question>> pageList(@RequestBody QuestionPageParams questionPageParams) {
        return CommonResult.success(questionService.pageList(questionPageParams));
    }

    @GetMapping("/detail")
    @Operation(summary = "详情")
    public CommonResult<Question> detail(Long id) {
        return CommonResult.success(questionService.detail(id));
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

    @PostMapping("/export-question-excel")
    @Operation(summary = "导出试题集")
    public void exportQuestionExcel(@RequestBody QuestionPageParams questionPageParams, HttpServletResponse response) {
        questionService.exportQuestionExcel(questionPageParams, response);
    }

    @PostMapping("/import-question-excel")
    @Operation(summary = "导入试题集")
    public CommonResult<String> importQuestionExcel(MultipartFile file,
                                                    @NotNull(message = "分类ID不能为空") Long categoryId,
                                                    @NotNull(message = "科目ID不能为空") Long subjectId,
                                                    @RequestParam(required = false) Long examId) {
        questionService.importQuestionExcel(file,categoryId, subjectId, examId);
        return CommonResult.success("成功！");
    }

    @GetMapping("/export-question-template")
    @Operation(summary = "导出模板")
    public void exportQuestionTemplate(HttpServletResponse response) {
        questionService.exportQuestionTemplate(response);
    }
}

