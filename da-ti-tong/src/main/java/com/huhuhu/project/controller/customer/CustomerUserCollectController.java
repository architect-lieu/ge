package com.huhuhu.project.controller.customer;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Question;
import com.huhuhu.project.entity.UserCollect;
import com.huhuhu.project.form.params.QuestionPageParams;
import com.huhuhu.project.service.UserCollectService;
import com.huhuhu.project.utils.TokenUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/customer/api/project/user-collect")
@Tag(name = "用户收藏API")
@RequiredArgsConstructor
public class CustomerUserCollectController {

    private final UserCollectService userCollectService;

    @PostMapping("/add")
    @Operation(summary = "收藏问题/分类")
    public CommonResult<Boolean> add(@RequestBody UserCollect userCollect) {
        return CommonResult.success(userCollectService.add(userCollect));
    }

    @GetMapping("/del")
    @Operation(summary = "取消收藏问题")
    public CommonResult<Boolean> del(Long questionId) {
        return CommonResult.success(userCollectService.del(questionId));
    }

    @GetMapping("/del-category")
    @Operation(summary = "取消收藏分类")
    public CommonResult<Boolean> delCategory(Long categoryId) {
        return CommonResult.success(userCollectService.delCategory(categoryId));
    }

    @GetMapping("/category/statistic")
    @Operation(summary = "用户收藏分类统计")
    public CommonResult<List<Map<String, Object>>> statistic() {
        return CommonResult.success(userCollectService.statistic());
    }

    @PostMapping("/question/list")
    @Operation(summary = "分类下收藏的题")
    public CommonResult<Page<Question>> questionList(@RequestBody QuestionPageParams params) {
        return CommonResult.success(userCollectService.questionList(params));
    }

    @GetMapping("/category/list")
    @Operation(summary = "首页题库分类收藏")
    public CommonResult<List<UserCollect>> categoryList() {
        return CommonResult.success(userCollectService.categoryList());
    }

    @GetMapping("/clear")
    @Operation(summary = "清空我的收藏")
    public CommonResult<Boolean> clear(Integer type) {
        return CommonResult.success(userCollectService.remove(Wrappers.lambdaQuery(UserCollect.class).eq(UserCollect::getUserId,
                TokenUtil.currentLoginUserId()).eq(UserCollect::getCollectType, type)));
    }
}

