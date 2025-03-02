package com.huhuhu.project.controller.admin;


import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.form.params.AdminLoginParams;
import com.huhuhu.project.service.AdminService;
import com.huhuhu.project.vo.AdminVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-03-13 10:18:48
 */
@RestController
@RequestMapping("/admin/api/project/admin")
@Tag(name = "管理员API")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @PostMapping("/login")
    @Operation(summary = "登陆")
    public CommonResult<AdminVo> login(@RequestBody AdminLoginParams adminLoginParam) {
        return CommonResult.success(adminService.login(adminLoginParam));
    }

}

