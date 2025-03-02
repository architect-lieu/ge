package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Category;
import com.huhuhu.project.entity.Subject;
import com.huhuhu.project.form.params.SubjectPageParams;
import com.huhuhu.project.mapper.QuestionMapper;
import com.huhuhu.project.mapper.SubjectMapper;
import com.huhuhu.project.service.CategoryService;
import com.huhuhu.project.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-07-01 06:02:37
 */
@Service
@RequiredArgsConstructor
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    private final CategoryService categoryService;

    private final QuestionMapper questionMapper;

    @Override
    public Boolean add(Subject subject) {
        return this.save(subject);
    }

    @Override
    public Boolean modify(Subject subject) {
        return this.updateById(subject);
    }

    @Override
    public Page<Subject> pageList(SubjectPageParams param) {
        LambdaQueryWrapper<Subject> queryWrapper = Wrappers.lambdaQuery(Subject.class)
                .eq(param.getCategoryId() != null, Subject::getCategoryId, param.getCategoryId())
                .like(StrUtil.isNotBlank(param.getSubjectName()), Subject::getSubjectName, param.getSubjectName());
        Page<Subject> page = this.page(Page.of(param.getPage(), param.getSize()), queryWrapper);
        List<Subject> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return page;
        }
        Set<Long> cIds = records.stream().map(Subject::getCategoryId).collect(Collectors.toSet());
        Map<Long, Category> categories = categoryService.list(Wrappers.lambdaQuery(Category.class)
                .in(Category::getCategoryId, cIds)).stream().collect(Collectors.toMap(Category::getCategoryId, Function.identity()));
        for (Subject record : records) {
            record.setCategory(categories.get(record.getCategoryId()));
        }
        return page;
    }

    @Override
    public Subject detail(Long id) {
        Subject subject = this.getById(id);
        if (subject == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        subject.setCategory(categoryService.detail(subject.getCategoryId()));
        return subject;
    }

    @Override
    public List<Subject> allList(Long categoryId) {
        Category category = categoryService.detail(categoryId);
        List<Subject> subjectList = this.list(Wrappers.lambdaQuery(Subject.class).eq(Subject::getCategoryId, categoryId));
        List<Long> subjectIds = subjectList.stream().map(Subject::getSubjectId).collect(Collectors.toList());
        // 获取科目下的题数
        Map<String, Object> countMap = new HashMap<>();
        if (CollUtil.isNotEmpty(subjectIds)) {
            countMap = questionMapper.selectQuestionCountBySubjectIds(subjectIds);
        }
        for (Subject subject : subjectList) {
            subject.setCategory(category);
            if (!countMap.isEmpty()) {
                subject.setTotalQuestionNum(new BigDecimal(countMap.get("id_" + subject.getSubjectId()).toString()).longValue());
            }
        }
        return subjectList;
    }
}
