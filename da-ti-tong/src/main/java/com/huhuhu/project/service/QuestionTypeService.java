package com.huhuhu.project.service;

import com.huhuhu.project.entity.QuestionType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
public interface QuestionTypeService extends IService<QuestionType> {

    /**
     * 添加题型
     * @param questionType
     * @return
     */
    Boolean add(QuestionType questionType);

    /**
     * 修改题型
     * @param questionType
     * @return
     */
    Boolean modify(QuestionType questionType);

    /**
     * 查询问题类型
     * @param keyword
     * @return
     */
    List<QuestionType> allList(String keyword);
}
