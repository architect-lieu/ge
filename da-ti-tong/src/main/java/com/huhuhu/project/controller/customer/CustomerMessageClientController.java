package com.huhuhu.project.controller.customer;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Message;
import com.huhuhu.project.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-07-01 09:07:37
 */
@RestController
@RequestMapping("/customer/api/project/messageClient")
@RequiredArgsConstructor
@Tag(name = "客户端消息API")
public class CustomerMessageClientController {

    private final MessageService messageService;

    @Operation(summary = "消息分页")
    @GetMapping("/list")
    public CommonResult<Page<Message>> pageList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return CommonResult.success(messageService.pageList(page, size));
    }

    @Operation(summary = "消息详情")
    @GetMapping("/detail")
    public CommonResult<Message> detail(Long id) {
        return CommonResult.success(messageService.detail(id));
    }

}

