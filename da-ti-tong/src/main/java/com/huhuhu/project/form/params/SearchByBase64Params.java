package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class SearchByBase64Params extends BasePageParams{
    @Schema(description = "图片base64")
    @NotEmpty(message = "base64内容不能为空")
    private String eCode;
}
