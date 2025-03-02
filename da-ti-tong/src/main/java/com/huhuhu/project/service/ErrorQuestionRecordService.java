package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.ErrorQuestionRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.dto.QuestionRecordDto;

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
public interface ErrorQuestionRecordService extends IService<ErrorQuestionRecord> {

    /**
     * 添加错题记录
     * @param errorQuestionRecord
     * @return
     */
    Boolean add(ErrorQuestionRecord errorQuestionRecord);

    /**
     * 批量添加做题记录
     * @param questionRecord
     * @return
     */
    Boolean addBatch(QuestionRecordDto questionRecord);

    /**
     * 分页查询记录
     *
     * @param page       当前页
     * @param size       页大小
     * @param categoryId
     * @param i
     * @return
     */
    Page<ErrorQuestionRecord> pageList(int page, int size, Long categoryId, Integer status);

    /**
     * 查询错题记录  分类
     * @return
     */
    List<Map<String, Object>> errorQuestion();
}
