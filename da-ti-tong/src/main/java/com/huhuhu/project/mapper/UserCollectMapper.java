package com.huhuhu.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Question;
import com.huhuhu.project.entity.UserCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huhuhu.project.form.params.QuestionPageParams;
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
public interface UserCollectMapper extends BaseMapper<UserCollect> {

    List<Map<String, Object>> statisticCategory(@Param("currentLoginUserId") Long currentLoginUserId);

    Page<Question> questionList(@Param("page") Page<Question> page,@Param("userId") Long userId,@Param("params") QuestionPageParams params);

    Map<String, Object> categoryStatistic(@Param("categoryId") Long categoryId,@Param("userId") Long userId,@Param("names") List<String> names);
}
