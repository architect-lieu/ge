package com.huhuhu.project.controller.customer;


import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Subject;
import com.huhuhu.project.service.SubjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/customer/api/project/subject")
@RequiredArgsConstructor
@Tag(name = "管理端科目接口API")
public class CustomerSubjectController {

    private final SubjectService subjectService;

    @Operation(summary = "根据分类获取所有科目")
    @GetMapping("/all-list")
    public CommonResult<List<Subject>> allList(Long categoryId) {
        return CommonResult.success(subjectService.allList(categoryId));
    }
}

