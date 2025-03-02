package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Subject;
import com.huhuhu.project.form.params.SubjectPageParams;
import com.huhuhu.project.service.SubjectService;
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
 * @since 2023-07-01 06:02:37
 */
@RestController
@RequestMapping("/admin/api/project/subject")
@RequiredArgsConstructor
@Tag(name = "管理端科目接口API")
public class SubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "添加科目")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) Subject subject) {
        return CommonResult.success(subjectService.add(subject));
    }

    @Operation(summary = "修改科目")
    @PostMapping("/modify")
    public CommonResult<Boolean> modify(@RequestBody @Validated({Default.class, SystemConstant.Update.class}) Subject subject) {
        return CommonResult.success(subjectService.modify(subject));
    }

    @Operation(summary = "科目分页")
    @PostMapping("/list")
    public CommonResult<Page<Subject>> pageList(@RequestBody SubjectPageParams param) {
        return CommonResult.success(subjectService.pageList(param));
    }

    @Operation(summary = "科目详情")
    @GetMapping("/detail")
    public CommonResult<Subject> detail(Long id) {
        return CommonResult.success(subjectService.detail(id));
    }

    @Operation(summary = "根据分类获取所有科目")
    @GetMapping("/all-list")
    public CommonResult<List<Subject>> allList(Long categoryId) {
        return CommonResult.success(subjectService.allList(categoryId));
    }
}

