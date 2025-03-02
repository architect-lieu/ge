package com.huhuhu.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.entity.QuestionType;
import com.huhuhu.project.mapper.QuestionTypeMapper;
import com.huhuhu.project.service.QuestionTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Service
public class QuestionTypeServiceImpl extends ServiceImpl<QuestionTypeMapper, QuestionType> implements QuestionTypeService {

    @Override
    public Boolean add(QuestionType questionType) {
        String questionTypeCode = questionType.getQuestionTypeCode();
        QuestionType type = this.getOne(Wrappers.lambdaQuery(QuestionType.class).eq(QuestionType::getQuestionTypeCode, questionTypeCode));
        if (type != null) {
            throw new BusinessException(3001, String.format("已存在编码为[ %s ]的题型", questionType.getQuestionTypeCode()));
        }
        return this.save(questionType);
    }

    @Override
    public Boolean modify(QuestionType questionType) {
        String questionTypeCode = questionType.getQuestionTypeCode();
        QuestionType type = this.getOne(Wrappers.lambdaQuery(QuestionType.class).eq(QuestionType::getQuestionTypeCode, questionTypeCode));
        if (type != null && !type.getQuestionTypeId().equals(questionType.getQuestionTypeId())) {
            throw new BusinessException(3001, String.format("已存在编码为[ %s ]的题型", questionType.getQuestionTypeCode()));
        }
        return this.updateById(questionType);
    }

    @Override
    public List<QuestionType> allList(String keyword) {
        return this.list(Wrappers.lambdaQuery(QuestionType.class)
                .like(StrUtil.isNotBlank(keyword), QuestionType::getQuestionTypeName, keyword));
    }
}
