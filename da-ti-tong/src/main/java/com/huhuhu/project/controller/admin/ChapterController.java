package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Chapter;
import com.huhuhu.project.form.params.ChapterPageParams;
import com.huhuhu.project.service.ChapterService;
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
@RequestMapping("/admin/api/project/chapter")
@Tag(name = "管理端章节API")
@RequiredArgsConstructor
public class ChapterController {

    private final ChapterService chapterService;

    @PostMapping("/add")
    @Operation(summary = "添加章节")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) Chapter chapter) {
        return CommonResult.success(chapterService.add(chapter));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改章节")
    public CommonResult<Boolean> modify(@RequestBody @Validated({Default.class, SystemConstant.Update.class}) Chapter chapter) {
        return CommonResult.success(chapterService.modify(chapter));
    }

    @PostMapping("/page/list")
    @Operation(summary = "分页查找章节")
    public CommonResult<Page<Chapter>> pageList(@RequestBody ChapterPageParams chapterPageParams) {
        return CommonResult.success(chapterService.pageList(chapterPageParams));
    }

    @GetMapping("/detail")
    @Operation(summary = "章节详情")
    public CommonResult<Chapter> detail(Long id) {
        return CommonResult.success(chapterService.detail(id));
    }

    @GetMapping("/list/by-category-id")
    @Operation(summary = "查询分类下所有的章节以及章节下的试题集")
    public CommonResult<List<Chapter>> listByCategoryId(Long category,
                                                        @RequestParam(required = false) Boolean trueQuestionChapterFlag) {
        return CommonResult.success(chapterService.listByCategoryId(category, trueQuestionChapterFlag));
    }

    @PostMapping("/setting-subject/{subjectId}")
    @Operation(summary = "关联科目")
    public CommonResult<Boolean> settingSubject(@PathVariable("subjectId") Long subjectId,
                                                @RequestBody List<Long> chapterIds) {
        return CommonResult.success(chapterService.settingSubject(subjectId, chapterIds));
    }
}

