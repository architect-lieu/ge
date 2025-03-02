package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/13 22:21
 */
@Data
public class AdminLoginParams {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    private String password;
}
