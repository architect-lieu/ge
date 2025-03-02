package com.huhuhu.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.ExamRecord;
import com.huhuhu.project.form.dto.ExamRecordPageParams;
import com.huhuhu.project.mapper.ExamRecordMapper;
import com.huhuhu.project.service.ExamRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.TokenUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Service
public class ExamRecordServiceImpl extends ServiceImpl<ExamRecordMapper, ExamRecord> implements ExamRecordService {

    @Override
    public Boolean addRecord(ExamRecord examRecord) {
        return this.save(examRecord);
    }

    @Override
    public Page<ExamRecord> pageList(ExamRecordPageParams examRecordPageParams) {
        Date startTime = examRecordPageParams.getStartTime();
        Date endTime = examRecordPageParams.getEndTime();
        LambdaQueryWrapper<ExamRecord> queryWrapper = Wrappers.lambdaQuery(ExamRecord.class)
                .select(col -> !col.getColumn().equals("exam_data")) // 这里不查询考试记录的数据
                .eq(ExamRecord::getUserId, TokenUtil.currentLoginUserId())
                .between(startTime != null && endTime != null, ExamRecord::getCreateTime, startTime, endTime)
                .orderByDesc(ExamRecord::getCreateTime);
        return this.page(Page.of(examRecordPageParams.getPage(), examRecordPageParams.getSize()), queryWrapper);
    }

    @Override
    public ExamRecord detail(Long id) {
        ExamRecord examRecord = this.getById(id);
        if (examRecord == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return examRecord;
    }
}
