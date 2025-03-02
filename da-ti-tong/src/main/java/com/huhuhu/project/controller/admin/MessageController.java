package com.huhuhu.project.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Message;
import com.huhuhu.project.service.MessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.groups.Default;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-07-01 09:07:37
 */
@RestController
@RequestMapping("/admin/api/project/message")
@RequiredArgsConstructor
@Tag(name = "管理端消息API")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "添加消息")
    @PostMapping("/add")
    public CommonResult<Boolean> add(@RequestBody @Validated(Default.class) Message message) {
        return CommonResult.success(messageService.add(message));
    }

    @Operation(summary = "修改消息")
    @PostMapping("/modify")
    public CommonResult<Boolean> modify(@RequestBody @Validated({Default.class, SystemConstant.Update.class}) Message message) {
        return CommonResult.success(messageService.modify(message));
    }

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

