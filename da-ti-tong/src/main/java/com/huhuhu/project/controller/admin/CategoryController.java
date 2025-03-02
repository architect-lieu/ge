package com.huhuhu.project.controller.admin;


import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Category;
import com.huhuhu.project.service.CategoryService;
import com.huhuhu.project.vo.CategoryVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/admin/api/project/category")
@RequiredArgsConstructor
@Tag(name = "管理端分类API")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    @Operation(summary = "添加分类")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) Category category) {
        return CommonResult.success(categoryService.add(category));
    }

    @PostMapping("/modify")
    @Operation(summary = "更新分类")
    public CommonResult<Boolean> modify(@RequestBody @Validated({SystemConstant.Update.class,Default.class}) Category category) {
        return CommonResult.success(categoryService.modify(category));
    }

    @GetMapping("/tree")
    @Operation(summary = "获取分类树")
    public CommonResult<List<CategoryVo>> tree(@RequestParam(defaultValue = "0") Long pid) {
        return CommonResult.success(categoryService.tree(pid, SystemConstant.ADMIN));
    }

    @GetMapping("/detail/with/children")
    @Operation(summary = "分类详情连同其下的子节点")
    public CommonResult<CategoryVo> detailWithChildren(Long id) {
        return CommonResult.success(categoryService.detailWithChildren(id));
    }

    @GetMapping("/detail")
    @Operation(summary = "分类详情")
    public CommonResult<Category> detail(Long id) {
        return CommonResult.success(categoryService.detail(id));
    }

    @GetMapping("/children-list")
    @Operation(summary = "获取分类下的叶子节点")
    public CommonResult<List<CategoryVo>> childrenList(Long pid) {
        return CommonResult.success(categoryService.childrenList(pid));
    }

    @PostMapping("/del")
    @Operation(summary = "删除")
    public CommonResult<Boolean> del(@RequestBody List<Long> ids) {
        return CommonResult.success(categoryService.del(ids));
    }
}