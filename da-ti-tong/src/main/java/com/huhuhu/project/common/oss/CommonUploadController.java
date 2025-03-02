package com.huhuhu.project.common.oss;

import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.vo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/customer/common/oss")
@RequiredArgsConstructor
public class CommonUploadController {

    private final OssUtil ossUtil;

    @PostMapping("/upload")
    @Operation(summary = "公共上传接口")
    public CommonResult<Map<String, Object>> upload(MultipartFile file) {
        try {
            return CommonResult.success(ossUtil.uploadFile2(file.getInputStream(), file.getOriginalFilename()));
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

}
