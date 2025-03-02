package com.huhuhu.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Feedback;
import com.huhuhu.project.form.params.FeedbackPageParams;
import com.huhuhu.project.mapper.FeedbackMapper;
import com.huhuhu.project.mapper.QuestionMapper;
import com.huhuhu.project.service.FeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    private final QuestionMapper questionMapper;

    @Override
    public Boolean add(Feedback feedback) {
        return this.save(feedback);
    }

    @Override
    public Boolean modify(Feedback feedback) {
        return this.updateById(feedback);
    }

    @Override
    public Feedback detail(Long id) {
        Feedback feedback = this.getById(id);
        if (feedback == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return feedback;
    }

    @Override
    public Boolean del(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    public Page<Feedback> pageList(FeedbackPageParams feedbackPageParams) {
        String content = feedbackPageParams.getContent();
        String type = feedbackPageParams.getType();
        Integer useFlag = feedbackPageParams.getUseFlag();
        Page<Feedback> page = this.page(Page.of(feedbackPageParams.getPage(), feedbackPageParams.getSize()), Wrappers.lambdaQuery(Feedback.class)
                .like(StrUtil.isNotBlank(content), Feedback::getFeedbackContent, content)
                .eq(StrUtil.isNotBlank(type), Feedback::getType, type)
                .eq(useFlag != null, Feedback::getUseFlag, useFlag));
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<Feedback> records = page.getRecords();
            for (Feedback record : records) {
                if (record.getQuestionId() != null) {
                    record.setQuestion(questionMapper.selectById(record.getQuestionId()));
                }
            }
        }
        return page;
    }
}
