package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.ExamRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.dto.ExamRecordPageParams;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
public interface ExamRecordService extends IService<ExamRecord> {

    /**
     * 添加考试记录
     * @param examRecord
     * @return
     */
    Boolean addRecord(ExamRecord examRecord);

    /**
     * 分页查询考试记录
     * @param examRecordPageParams
     * @return
     */
    Page<ExamRecord> pageList(ExamRecordPageParams examRecordPageParams);

    /**
     * 记录详情
     * @param id
     * @return
     */
    ExamRecord detail(Long id);
}
