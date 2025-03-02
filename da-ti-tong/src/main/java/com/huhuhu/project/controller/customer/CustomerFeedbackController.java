package com.huhuhu.project.controller.customer;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Feedback;
import com.huhuhu.project.form.params.FeedbackPageParams;
import com.huhuhu.project.service.FeedbackService;
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
 * @since 2023-04-17 10:16:23
 */
@RestController
@RequestMapping("/customer/api/project/feedback")
@RequiredArgsConstructor
@Tag(name = "用户反馈api")
public class CustomerFeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/add")
    @Operation(summary = "添加反馈")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) Feedback feedback) {
        return CommonResult.success(feedbackService.add(feedback));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改反馈")
    public CommonResult<Boolean> modify(@RequestBody @Validated({SystemConstant.Update.class, Default.class}) Feedback feedback) {
        return CommonResult.success(feedbackService.modify(feedback));
    }

    @GetMapping("/detail")
    @Operation(summary = "详情")
    public CommonResult<Feedback> detail(Long id) {
        return CommonResult.success(feedbackService.detail(id));
    }

    @PostMapping("/del")
    @Operation(summary = "删除反馈")
    public CommonResult<Boolean> del(@RequestBody List<Long> ids) {
        return CommonResult.success(feedbackService.del(ids));
    }

    @PostMapping("/list")
    @Operation(summary = "分页查询反馈")
    public CommonResult<Page<Feedback>> pageList(@RequestBody FeedbackPageParams feedbackPageParams) {
        return CommonResult.success(feedbackService.pageList(feedbackPageParams));
    }
}

