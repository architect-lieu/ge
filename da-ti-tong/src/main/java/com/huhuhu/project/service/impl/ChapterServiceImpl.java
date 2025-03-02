package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Chapter;
import com.huhuhu.project.entity.Paper;
import com.huhuhu.project.form.params.ChapterPageParams;
import com.huhuhu.project.mapper.ChapterMapper;
import com.huhuhu.project.mapper.PaperMapper;
import com.huhuhu.project.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Service
@RequiredArgsConstructor
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    private final PaperMapper paperMapper;

    @Override
    public Boolean add(Chapter chapter) {
        return this.save(chapter);
    }

    @Override
    public Page<Chapter> pageList(ChapterPageParams chapterPageParams) {
        String chapterName = chapterPageParams.getChapterName();
        Long categoryId = chapterPageParams.getCategoryId();
        Long subjectId = chapterPageParams.getSubjectId();
        Boolean trueQuestionChapterFlag = chapterPageParams.getTrueQuestionChapterFlag();
        LambdaQueryWrapper<Chapter> queryWrapper = Wrappers.lambdaQuery(Chapter.class).like(StrUtil.isNotBlank(chapterName), Chapter::getChapterName, chapterName)
                .eq(categoryId != null, Chapter::getCategoryId, categoryId)
                .eq(trueQuestionChapterFlag != null, Chapter::getTrueQuestionChapterFlag, trueQuestionChapterFlag)
                .eq(subjectId != null, Chapter::getSubjectId, subjectId)
                .orderByDesc(Chapter::getCreateTime);
        return this.page(Page.of(chapterPageParams.getPage(), chapterPageParams.getSize()),queryWrapper);
    }

    @Override
    public Chapter detail(Long id) {
        Chapter chapter = this.getById(id);
        if (chapter == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return chapter;
    }

    @Override
    public List<Chapter> listByCategoryId(Long categoryId, Boolean trueQuestionChapterFlag) {
        List<Chapter> chapters = this.list(Wrappers.lambdaQuery(Chapter.class)
                .eq(categoryId != null,Chapter::getCategoryId, categoryId)
                .eq(trueQuestionChapterFlag != null,Chapter::getTrueQuestionChapterFlag, trueQuestionChapterFlag));
        List<Long> ids = chapters.stream().map(Chapter::getChapterId).collect(Collectors.toList());
        // 查询所属章节的试题集
        if (CollUtil.isNotEmpty(ids)) {
            List<Paper> papers = paperMapper.selectList(Wrappers.lambdaQuery(Paper.class).in(Paper::getChapterId, ids));
            if (CollUtil.isNotEmpty(papers)) {
                Map<Long, List<Paper>> groupMap = papers.stream().collect(Collectors.groupingBy(Paper::getChapterId));
                for (Chapter chapter : chapters) {
                    List<Paper> paperList = groupMap.get(chapter.getChapterId());
                    chapter.setPapers(paperList);
                }
                // 统计paper下的题目数量
                Set<Long> paperIds = papers.stream().map(Paper::getPaperId).collect(Collectors.toSet());
                List<Map<String, Long>> paperQuestionCounts = paperMapper.statisticPaperQuestionNum(paperIds);
                if (CollUtil.isNotEmpty(paperQuestionCounts)) {
                    Map<Long, Long> numMap = paperQuestionCounts.stream().collect(Collectors.toMap(item -> item.get("paperId"), item -> item.get("num")));
                    for (Paper paper : papers) {
                        Long num = numMap.get(paper.getPaperId());
                        paper.setQuestionNum(num == null ? 0 : num);
                    }
                }
            }
        }
        return chapters;
    }

    @Override
    public Boolean modify(Chapter chapter) {
        return this.updateById(chapter);
    }

    @Override
    public Boolean settingSubject(Long subjectId, List<Long> chapterIds) {
        return this.lambdaUpdate().set(subjectId != null, Chapter::getSubjectId, subjectId)
                .in(CollUtil.isNotEmpty(chapterIds) ,Chapter::getChapterId, chapterIds).update();
    }
}
