package com.huhuhu.project.controller.customer;


import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.DocDownloadSearchRecord;
import com.huhuhu.project.service.DocDownloadRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 02:56:45
 */
@RestController
@RequestMapping("/customer/api/project/doc-download-record")
@Tag(name = "文档下载和搜索API")
@RequiredArgsConstructor
public class CustomerDocDownloadRecordController {

    private final DocDownloadRecordService docDownloadRecordService;

    @Operation(summary = "查询搜索记录和下载记录")
    @GetMapping("/search-download-record")
    public CommonResult<List<DocDownloadSearchRecord>> searchDownloadRecord(Integer type) {
        return CommonResult.success(docDownloadRecordService.searchDownloadRecord(type));
    }
}

