package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Category;
import com.huhuhu.project.entity.ExamInfo;
import com.huhuhu.project.form.params.ExamInfoPageParams;
import com.huhuhu.project.mapper.CategoryMapper;
import com.huhuhu.project.mapper.ExamInfoMapper;
import com.huhuhu.project.service.ExamInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
 * @since 2023-07-01 08:07:05
 */
@Service
@RequiredArgsConstructor
public class ExamInfoServiceImpl extends ServiceImpl<ExamInfoMapper, ExamInfo> implements ExamInfoService {

    private final CategoryMapper categoryMapper;

    @Override
    public Boolean add(ExamInfo examInfo) {
        // 根据科目id查询是否存在
        Long subjectId = examInfo.getSubjectId();
        ExamInfo info = this.getOne(Wrappers.lambdaQuery(ExamInfo.class).eq(ExamInfo::getSubjectId, subjectId));
        if (info != null) {
            throw new BusinessException(ResultCode.EXAM_INFO_EXISTS);
        }
        return this.save(examInfo);
    }

    @Override
    public Boolean modify(ExamInfo examInfo) {
        Long subjectId = examInfo.getSubjectId();
        ExamInfo info = this.getOne(Wrappers.lambdaQuery(ExamInfo.class).eq(ExamInfo::getSubjectId, subjectId));
        if (info == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return this.updateById(examInfo);
    }

    @Override
    public Page<ExamInfo> pageList(ExamInfoPageParams param) {
        String title = param.getTitle();
        LambdaQueryWrapper<ExamInfo> queryWrapper = Wrappers.lambdaQuery(ExamInfo.class)
                .like(StrUtil.isNotBlank(title), ExamInfo::getExamInfoTitle, title);
        Page<ExamInfo> page = this.page(Page.of(param.getPage(), param.getSize()), queryWrapper);
        List<ExamInfo> records = page.getRecords();
        if (CollUtil.isEmpty(records)) {
            return page;
        }
        Set<Long> subjectIds = records.stream().map(ExamInfo::getSubjectId).collect(Collectors.toSet());
        Map<Long, Category> subjectMap = categoryMapper.selectList(Wrappers.lambdaQuery(Category.class).in(Category::getCategoryId, subjectIds))
                .stream().collect(Collectors.toMap(Category::getCategoryId, Function.identity()));
        for (ExamInfo record : records) {
            record.setSubject(subjectMap.get(record.getSubjectId()));
        }
        return page;
    }

    @Override
    public ExamInfo detail(Long id) {
        ExamInfo examInfo = this.getById(id);
        if (examInfo == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        Category subject = categoryMapper.selectById(examInfo.getSubjectId());
        examInfo.setSubject(subject);
        return examInfo;
    }

    @Override
    public ExamInfo detailBySubject(Long categoryId) {
        ExamInfo info = this.getOne(Wrappers.lambdaQuery(ExamInfo.class)
                .eq(ExamInfo::getSubjectId, categoryId));
        if (info == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return info;
    }


}
