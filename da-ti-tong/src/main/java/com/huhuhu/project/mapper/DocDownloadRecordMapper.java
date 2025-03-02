package com.huhuhu.project.mapper;

import cn.hutool.core.date.DateTime;
import com.huhuhu.project.entity.DocDownloadSearchRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 02:56:45
 */
@Mapper
public interface DocDownloadRecordMapper extends BaseMapper<DocDownloadSearchRecord> {

    Map<String, BigDecimal> statisticNumByType(@Param("lastTime") DateTime lastTime, @Param("currentTime") DateTime currentTime, @Param("userId") Long userId);
}
