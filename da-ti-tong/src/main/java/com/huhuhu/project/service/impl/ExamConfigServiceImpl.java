package com.huhuhu.project.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huhuhu.project.entity.ExamConfig;
import com.huhuhu.project.form.dto.ExamConfigForm;
import com.huhuhu.project.mapper.ExamConfigMapper;
import com.huhuhu.project.service.ExamConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Service
public class ExamConfigServiceImpl extends ServiceImpl<ExamConfigMapper, ExamConfig> implements ExamConfigService {

    @Override
    @Transactional
    public Boolean add(ExamConfigForm examConfigForm) {
        List<ExamConfig> examConfigs = examConfigForm.getExamConfigs();
        return this.saveOrUpdateBatch(examConfigs);
    }

    @Override
    public ExamConfigForm detail(Long categoryId) {
        List<ExamConfig> examConfigs = this.list(Wrappers.lambdaQuery(ExamConfig.class).eq(ExamConfig::getCategoryId, categoryId));
        ExamConfigForm examConfigForm = new ExamConfigForm();
        examConfigForm.setExamConfigs(examConfigs);
        return examConfigForm;
    }
}
