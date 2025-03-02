package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Document;
import com.huhuhu.project.form.params.DocumentPageParams;
import com.huhuhu.project.service.DocumentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.groups.Default;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-03-12 06:15:19
 */
@RestController
@RequestMapping("/admin/api/project/document")
@Tag(name = "管理端文档API")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/add")
    @Operation(summary = "添加文档")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) Document document) {
        return CommonResult.success(documentService.add(document));
    }

    @PostMapping("/modify")
    @Operation(summary = "更新文档")
    public CommonResult<Boolean> modify(@RequestBody @Validated({Default.class, SystemConstant.Update.class}) Document document) {
        return CommonResult.success(documentService.modify(document));
    }

    @GetMapping("/detail")
    @Operation(summary = "详情")
    public CommonResult<Document> detail(Integer id) {
        return CommonResult.success(documentService.detail(id));
    }

    @PostMapping("/list")
    @Operation(summary = "文档分页列表")
    public CommonResult<Page<Document>> pageList(@RequestBody DocumentPageParams documentPageParams) {
        return CommonResult.success(documentService.pageList(documentPageParams));
    }

    @PostMapping("/upload/doc")
    @Operation(summary = "上传文档")
    public CommonResult<Map<String,Object>> uploadDoc(MultipartFile file) {
        return CommonResult.success(documentService.uploadDoc(file));
    }

    @PostMapping("/upload/cover-image")
    @Operation(summary = "上传封面")
    public CommonResult<Map<String,Object>> coverImage(MultipartFile file) {
        return CommonResult.success(documentService.coverImage(file));
    }
}

