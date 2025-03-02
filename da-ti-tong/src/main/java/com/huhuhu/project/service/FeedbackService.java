package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Feedback;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.FeedbackPageParams;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-04-17 10:16:23
 */
public interface FeedbackService extends IService<Feedback> {

    Boolean add(Feedback feedback);

    Boolean modify(Feedback feedback);

    Feedback detail(Long id);

    Boolean del(List<Long> ids);

    Page<Feedback> pageList(FeedbackPageParams feedbackPageParams);
}
