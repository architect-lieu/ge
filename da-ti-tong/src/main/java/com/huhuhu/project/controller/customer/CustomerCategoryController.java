package com.huhuhu.project.controller.customer;


import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.service.CategoryService;
import com.huhuhu.project.vo.CategoryVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/customer/api/project/category")
@Tag(name = "客户端分类API")
@RequiredArgsConstructor
public class CustomerCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/tree")
    @Operation(summary = "获取分类树")
    public CommonResult<List<CategoryVo>> tree(@RequestParam(defaultValue = "0") Long pid) {
        return CommonResult.success(categoryService.tree(pid, SystemConstant.CUSTOMER));
    }

    @GetMapping("/search")
    @Operation(summary = "获取搜索到的分类下的子分类")
    public CommonResult<List<CategoryVo>> search(@RequestParam(required = false) String keyword) {
        return CommonResult.success(categoryService.search(keyword));
    }

    @GetMapping("/index/statistic")
    @Operation(summary = "练习首页的统计[ 针对当前用户当前分类下的相关统计 ]")
    public CommonResult<Map<String, Object>> indexStatistic(Long categoryId) {
        return CommonResult.success(categoryService.indexStatistic(categoryId));
    }

    @GetMapping("/category/statistic")
    @Operation(summary = "当前分类下登陆用户的相关统计")
    public CommonResult<Map<String, Object>> categoryStatistic(Long categoryId) {
        return CommonResult.success(categoryService.categoryStatistic(categoryId));
    }
}

