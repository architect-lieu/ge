package com.huhuhu.project.controller.customer;


import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Chapter;
import com.huhuhu.project.service.ChapterService;
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
@RequestMapping("/customer/api/project/chapter")
@Tag(name = "客户端章节API")
@RequiredArgsConstructor
public class CustomerChapterController {

    private final ChapterService chapterService;

    @GetMapping("/list/by-category-id")
    @Operation(summary = "查询科目下所有的章节以及章节下的试题集")
    public CommonResult<List<Chapter>> listByCategoryId(Long categoryId,
                                                        @RequestParam(required = false) Boolean trueQuestionChapterFlag) {
        return CommonResult.success(chapterService.listByCategoryId(categoryId, trueQuestionChapterFlag));
    }
}

