package com.huhuhu.project.service;

import com.huhuhu.project.entity.ExamConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.dto.ExamConfigForm;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
public interface ExamConfigService extends IService<ExamConfig> {

    /**
     * 添加考试规则
     * @param examConfigForm
     * @return
     */
    Boolean add(ExamConfigForm examConfigForm);

    /**
     * 获取考试规则
     * @param categoryId
     * @return
     */
    ExamConfigForm detail(Long categoryId);
}
