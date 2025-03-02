package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.ChapterPageParams;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
public interface ChapterService extends IService<Chapter> {

    /**
     * 添加章节
     * @param chapter
     * @return
     */
    Boolean add(Chapter chapter);

    /**
     * 分页查询章节
     * @param chapterPageParams
     * @return
     */
    Page<Chapter> pageList(ChapterPageParams chapterPageParams);

    /**
     * 章节详情
     * @param id
     * @return
     */
    Chapter detail(Long id);

    /**
     * 查询分类下所有的章节以及章节下的试题集
     * @param categoryId
     * @param trueQuestionChapterFlag
     * @return
     */
    List<Chapter> listByCategoryId(Long categoryId, Boolean trueQuestionChapterFlag);

    /**
     * 更新章节
     * @param chapter
     * @return
     */
    Boolean modify(Chapter chapter);

    Boolean settingSubject(Long subjectId, List<Long> chapterIds);
}
