package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Question;
import com.huhuhu.project.entity.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.QuestionPageParams;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
public interface UserCollectService extends IService<UserCollect> {

    /**
     * 收藏
     * @param userCollect
     * @return
     */
    Boolean add(UserCollect userCollect);

    /**
     * 取消收藏
     * @param questionId
     * @return
     */
    Boolean del(Long questionId);

    /**
     * 统计
     * @return
     */
    List<Map<String, Object>> statistic();

    /**
     * 查询分类下，收藏的题列表
     * @param params
     * @return
     */
    Page<Question> questionList(QuestionPageParams params);

    /**
     * 删除收藏分类
     * @param categoryId
     * @return
     */
    Boolean delCategory(Long categoryId);

    List<UserCollect> categoryList();
}
