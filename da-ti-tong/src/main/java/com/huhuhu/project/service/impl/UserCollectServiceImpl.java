package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Category;
import com.huhuhu.project.entity.Question;
import com.huhuhu.project.entity.UserCollect;
import com.huhuhu.project.enums.CollectTypeEnum;
import com.huhuhu.project.form.params.QuestionPageParams;
import com.huhuhu.project.mapper.CategoryMapper;
import com.huhuhu.project.mapper.QuestionMapper;
import com.huhuhu.project.mapper.UserCollectMapper;
import com.huhuhu.project.service.UserCollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
 * @since 2023-03-10 07:56:41
 */
@Service
@RequiredArgsConstructor
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements UserCollectService {

    private final QuestionMapper questionMapper;

    private final CategoryMapper categoryMapper;

    @Override
    public Boolean add(UserCollect userCollect) {
        Long userId = TokenUtil.currentLoginUserId();
        Integer collectType = userCollect.getCollectType();
        if (CollectTypeEnum.TYPE.getCode().equals(collectType)) {
            boolean exists = this.baseMapper.exists(Wrappers.lambdaQuery(UserCollect.class)
                    .eq(UserCollect::getUserId, userId)
                    .eq(UserCollect::getCollectType, CollectTypeEnum.TYPE.getCode())
                    .eq(UserCollect::getCategoryId, userCollect.getCategoryId()));
            if (exists) {
                return true;
            }
        } else if (CollectTypeEnum.QUESTION.getCode().equals(collectType)) {
            boolean exists = this.baseMapper.exists(Wrappers.lambdaQuery(UserCollect.class)
                    .eq(UserCollect::getUserId, userId)
                    .eq(UserCollect::getCollectType, CollectTypeEnum.QUESTION.getCode())
                    .eq(UserCollect::getQuestionId, userCollect.getQuestionId()));
            if (exists) {
                return true;
            }
            // 查询问题分类
            Question question = questionMapper.selectOne(Wrappers.lambdaQuery(Question.class)
                    .select(Question::getQuestionId, Question::getCategoryId)
                    .eq(Question::getQuestionId, userCollect.getQuestionId()));
            userCollect.setCategoryId(question.getCategoryId());
        }
        userCollect.setUserId(userId);
        return this.save(userCollect);
    }

    @Override
    public Boolean del(Long questionId) {
        Long userId = TokenUtil.currentLoginUserId();
        return this.remove(Wrappers.lambdaQuery(UserCollect.class)
                .eq(UserCollect::getUserId, userId)
                .eq(UserCollect::getQuestionId, questionId));
    }

    @Override
    public List<Map<String, Object>> statistic() {
        return this.baseMapper.statisticCategory(Long.parseLong(TokenUtil.currentLoginUserId().toString()));
    }

    @Override
    public Page<Question> questionList(QuestionPageParams params) {
        Page<Question> questionPage = this.baseMapper.questionList(Page.of(params.getPage(), params.getSize()), TokenUtil.currentLoginUserId(), params);
        List<Question> records = questionPage.getRecords();
        if (CollUtil.isNotEmpty(records)) {
            for (Question record : records) {
                record.setCollectFlag(Boolean.TRUE);
            }
        }
        return questionPage;
    }

    @Override
    public Boolean delCategory(Long categoryId) {
        Long userId = TokenUtil.currentLoginUserId();
        return this.remove(Wrappers.lambdaQuery(UserCollect.class)
                .eq(UserCollect::getUserId, userId)
                .eq(UserCollect::getCollectType, CollectTypeEnum.TYPE.getCode())
                .eq(UserCollect::getCategoryId, categoryId));
    }

    @Override
    public List<UserCollect> categoryList() {
        List<UserCollect> collects = this.list(Wrappers.lambdaQuery(UserCollect.class)
                .eq(UserCollect::getUserId, TokenUtil.currentLoginUserId())
                .eq(UserCollect::getCollectType,
                CollectTypeEnum.TYPE.getCode()));
        if (CollUtil.isEmpty(collects)) {
            return Collections.emptyList();
        }
        Set<Long> cIds = collects.stream().map(UserCollect::getCategoryId).collect(Collectors.toSet());
        Map<Long, Category> categoryMap = this.categoryMapper.selectList(Wrappers.lambdaQuery(Category.class)
                        .in(Category::getCategoryId, cIds)).stream()
                .collect(Collectors.toMap(Category::getCategoryId, Function.identity()));
        for (UserCollect collect : collects) {
            collect.setCategory(categoryMap.get(collect.getCategoryId()));
        }
        // 统计分类下题的数量
        List<Map<String, Long>> statisticResult = questionMapper.statisticQuestionNum(cIds);
        Map<Long, Long> collect = statisticResult.stream().collect(Collectors.toMap(item -> item.get("categoryId"), item -> item.get("num")));
        for (UserCollect userCollect : collects) {
            Long num = collect.get(userCollect.getCategoryId());
            userCollect.setCategoryQuestionNum(num);
        }
        return collects;
    }
}
