package com.huhuhu.project.controller.customer;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.vo.CommonResult;
import com.huhuhu.project.entity.Notes;
import com.huhuhu.project.form.params.NotesPageParams;
import com.huhuhu.project.service.NotesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.groups.Default;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author KangXin
 * @since 2023-04-17 10:16:23
 */
@RestController
@RequestMapping("/customer/api/project/notes")
@RequiredArgsConstructor
@Tag(name = "用户笔记api")
public class CustomerNotesController {

    private final NotesService notesService;

    @PostMapping("/add")
    @Operation(summary = "添加笔记")
    public CommonResult<Boolean> add(@RequestBody @Validated({Default.class}) Notes notes) {
        return CommonResult.success( notesService.add(notes));
    }

    @PostMapping("/modify")
    @Operation(summary = "修改笔记")
    public CommonResult<Boolean> modify(@RequestBody @Validated({Default.class, SystemConstant.Update.class}) Notes notes) {
        return CommonResult.success(notesService.modify(notes));
    }

    @PostMapping("/del")
    @Operation(summary = "删除笔记")
    public CommonResult<Boolean> del(@RequestBody List<Long> ids) {
        return CommonResult.success(notesService.del(ids));

    }

    @GetMapping("/detail")
    @Operation(summary = "笔记详情")
    public CommonResult<Notes> detail(Long id) {
        return CommonResult.success(notesService.detail(id));
    }

    @PostMapping("/list")
    @Operation(summary = "分页查询笔记")
    public CommonResult<Page<Notes>> pageList(@RequestBody NotesPageParams notesPageParams) {
        return CommonResult.success(notesService.pageList(notesPageParams));
    }
}

