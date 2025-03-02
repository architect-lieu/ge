package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Notes;
import com.huhuhu.project.form.params.NotesPageParams;
import com.huhuhu.project.mapper.NotesMapper;
import com.huhuhu.project.mapper.QuestionMapper;
import com.huhuhu.project.service.NotesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-04-17 10:16:23
 */
@Service
@RequiredArgsConstructor
public class NotesServiceImpl extends ServiceImpl<NotesMapper, Notes> implements NotesService {

    private final QuestionMapper questionMapper;

    @Override
    public Boolean add(Notes notes) {
        return this.save(notes);
    }

    @Override
    public Boolean modify(Notes notes) {
        return this.updateById(notes);
    }

    @Override
    @Transactional
    public Boolean del(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public Notes detail(Long id) {
        Notes notes = this.getById(id);
        if (notes == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        notes.setQuestion(questionMapper.selectById(notes.getQuestionId()));
        return notes;
    }

    @Override
    public Page<Notes> pageList(NotesPageParams notesPageParams) {
        Long categoryId = notesPageParams.getCategoryId();
        String content = notesPageParams.getContent();
        Page<Notes> page = this.page(Page.of(notesPageParams.getPage(), notesPageParams.getSize()), Wrappers.lambdaQuery(Notes.class)
                .eq(categoryId != null, Notes::getCategoryId, categoryId)
                .eq(Notes::getUserId, TokenUtil.currentLoginUserId())
                .like(StrUtil.isNotBlank(content), Notes::getNotesContent, content));
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<Notes> records = page.getRecords();
            for (Notes record : records) {
                record.setQuestion(questionMapper.selectById(record.getQuestionId()));
            }
        }
        return page;
    }


}
