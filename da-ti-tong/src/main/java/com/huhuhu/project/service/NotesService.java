package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Notes;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.NotesPageParams;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-04-17 10:16:23
 */
public interface NotesService extends IService<Notes> {

    Boolean add(Notes notes);

    Boolean modify(Notes notes);

    Boolean del(List<Long> ids);

    Notes detail(Long id);

    Page<Notes> pageList(NotesPageParams notesPageParams);
}
