package com.huhuhu.project.controller.customer;


import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.ExamInfo;
import com.huhuhu.project.service.ExamInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-07-01 08:07:05
 */
@RestController
@RequestMapping("/customer/api/project/exam-info")
@RequiredArgsConstructor
@Tag(name = "客户端考试信息API")
public class CustomerExamInfoController {

    private final ExamInfoService examInfoService;

    @Operation(summary = "获取科目下的考试信息")
    @GetMapping("/detail-by-category")
    public CommonResult<ExamInfo> detailBySubject(Long categoryId) {
        return CommonResult.success(examInfoService.detailBySubject(categoryId));
    }
}

