package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.ExamInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.ExamInfoPageParams;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-07-01 08:07:05
 */
public interface ExamInfoService extends IService<ExamInfo> {

    Boolean add(ExamInfo examInfo);

    Boolean modify(ExamInfo examInfo);

    Page<ExamInfo> pageList(ExamInfoPageParams param);

    ExamInfo detail(Long id);

    ExamInfo detailBySubject(Long categoryId);
}
