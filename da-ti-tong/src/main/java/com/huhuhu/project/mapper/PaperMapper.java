package com.huhuhu.project.mapper;

import com.huhuhu.project.entity.Paper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Mapper
public interface PaperMapper extends BaseMapper<Paper> {

    List<Map<String, Long>> statisticPaperQuestionNum(@Param("paperIds") Set<Long> paperIds);
}
