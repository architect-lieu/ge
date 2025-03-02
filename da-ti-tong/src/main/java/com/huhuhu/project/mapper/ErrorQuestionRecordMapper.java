package com.huhuhu.project.mapper;

import com.huhuhu.project.entity.ErrorQuestionRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Mapper
public interface ErrorQuestionRecordMapper extends BaseMapper<ErrorQuestionRecord> {

    Map<String, Object> categoryStatistic(@Param("categoryId") Long categoryId, @Param("userId") Long userId,@Param("names") List<String> names);
}
