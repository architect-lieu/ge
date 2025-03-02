package com.huhuhu.project.controller.customer;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Document;
import com.huhuhu.project.form.params.DocumentPageParams;
import com.huhuhu.project.service.DocumentService;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-03-12 06:15:19
 */
@RestController
@RequestMapping("/customer/api/project/document")
@Tag(name = "客户点文档API")
@RequiredArgsConstructor
public class CustomerDocumentController {

    private final DocumentService documentService;

    @PostMapping("/list")
    @Operation(summary = "文档分页列表")
    public CommonResult<Page<Document>> pageList(@RequestBody DocumentPageParams documentPageParams) {
        return CommonResult.success(documentService.pageList(documentPageParams));
    }

    @GetMapping("/download")
    @Operation(summary = "下载文档")
    public void downloadDoc(HttpServletResponse response, String downloadFlag) {
        documentService.downloadDoc(response, downloadFlag);
    }
}

