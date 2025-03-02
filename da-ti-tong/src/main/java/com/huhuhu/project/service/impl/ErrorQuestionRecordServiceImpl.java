package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Category;
import com.huhuhu.project.entity.ErrorQuestionRecord;
import com.huhuhu.project.entity.Question;
import com.huhuhu.project.enums.QuestionStatus;
import com.huhuhu.project.form.dto.QuestionRecordDto;
import com.huhuhu.project.mapper.CategoryMapper;
import com.huhuhu.project.mapper.ErrorQuestionRecordMapper;
import com.huhuhu.project.mapper.QuestionMapper;
import com.huhuhu.project.service.ErrorQuestionRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.KxSpringUtils;
import com.huhuhu.project.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Service
@RequiredArgsConstructor
public class ErrorQuestionRecordServiceImpl extends ServiceImpl<ErrorQuestionRecordMapper, ErrorQuestionRecord> implements ErrorQuestionRecordService {

    private final QuestionMapper questionMapper;

    private final CategoryMapper categoryMapper;


    @Override
    @Transactional
    public Boolean add(ErrorQuestionRecord errorQuestionRecord) {
        Long userId = TokenUtil.currentLoginUserId();
        errorQuestionRecord.setUserId(userId);
        boolean exists = this.baseMapper.exists(Wrappers.lambdaQuery(ErrorQuestionRecord.class)
                .eq(ErrorQuestionRecord::getQuestionId, errorQuestionRecord.getQuestionId())
                .eq(ErrorQuestionRecord::getUserId, userId));
        if (!exists) {
            return this.save(errorQuestionRecord);
        }
        return this.update(errorQuestionRecord, Wrappers.lambdaUpdate(ErrorQuestionRecord.class)
                .eq(ErrorQuestionRecord::getQuestionId, errorQuestionRecord.getQuestionId())
                .eq(ErrorQuestionRecord::getUserId, userId));
    }

    @Override
    @Transactional
    public Boolean addBatch(QuestionRecordDto questionRecord) {
        List<ErrorQuestionRecord> questionRecordList = questionRecord.getQuestionRecordList();
        for (ErrorQuestionRecord errorQuestionRecord : questionRecordList) {
            ErrorQuestionRecordService questionRecordService = KxSpringUtils.getBean(ErrorQuestionRecordService.class);
            questionRecordService.add(errorQuestionRecord);
        }
        return Boolean.TRUE;
    }

    @Override
    public Page<ErrorQuestionRecord> pageList(int page, int size, Long categoryId, Integer status) {
        Page<ErrorQuestionRecord> p = this.page(Page.of(page, size), Wrappers.lambdaQuery(ErrorQuestionRecord.class)
                .eq(ErrorQuestionRecord::getUserId, TokenUtil.currentLoginUserId())
                .eq(categoryId != null, ErrorQuestionRecord::getCategoryId, categoryId)
                .eq(status != null, ErrorQuestionRecord::getStatus, status)
                .orderByDesc(ErrorQuestionRecord::getCreateTime));
        List<ErrorQuestionRecord> records = p.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            Set<Long> ids = records.stream().map(ErrorQuestionRecord::getQuestionId).collect(Collectors.toSet());
            Set<Long> cIds = records.stream().map(ErrorQuestionRecord::getCategoryId).collect(Collectors.toSet());
            Map<Long, Question> questionMap = questionMapper.selectList(Wrappers.lambdaQuery(Question.class).in(Question::getQuestionId, ids)).stream()
                    .collect(Collectors.toMap(Question::getQuestionId, Function.identity()));
            Map<Long, Category> categoryMap = categoryMapper.selectList(Wrappers.lambdaQuery(Category.class).in(Category::getCategoryId, cIds)).stream()
                    .collect(Collectors.toMap(Category::getCategoryId, Function.identity()));
            for (ErrorQuestionRecord record : records) {
                record.setQuestion(questionMap.get(record.getQuestionId()));
                record.setCategory(categoryMap.get(record.getCategoryId()));
            }
        }
        return p;
    }

    @Override
    public List<Map<String, Object>> errorQuestion() {
        List<ErrorQuestionRecord> records = this.list(Wrappers.<ErrorQuestionRecord>lambdaQuery()
                .eq(ErrorQuestionRecord::getStatus, QuestionStatus.ERROR.getCode())
                .eq(ErrorQuestionRecord::getUserId, TokenUtil.currentLoginUserId()));
        if (CollUtil.isEmpty(records)) {
            return Collections.emptyList();
        }
        Map<Long, List<ErrorQuestionRecord>> groups = records.stream().collect(Collectors.groupingBy(ErrorQuestionRecord::getCategoryId));
        Set<Long> keys = groups.keySet();
        Map<Long, Category> categoryMap = categoryMapper.selectList(Wrappers.<Category>lambdaQuery().in(Category::getCategoryId, keys))
                .stream().collect(Collectors.toMap(Category::getCategoryId, Function.identity()));
        List<Map<String, Object>> result = new ArrayList<>();
        groups.forEach((k,v) -> {
            Map<String, Object> map = new HashMap<>();
            Category category = categoryMap.get(k);
            map.put("question", v);
            map.put("category", category);
            long count = v.size();
            map.put("count", count);
            result.add(map);
        });
        return result;
    }
}
