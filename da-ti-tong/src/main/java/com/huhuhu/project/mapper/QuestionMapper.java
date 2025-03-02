package com.huhuhu.project.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huhuhu.project.form.params.QuestionPageParams;
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
public interface QuestionMapper extends BaseMapper<Question> {

    Page<Question> pageListByPaperId(@Param("page") Page<Question> page,@Param("params") QuestionPageParams params);

    Page<Question> searchByKeyWordList(@Param("page") Page<Object> page,@Param("keywords") List<String> keywords);

    Map<String, Object> typeStatistic(@Param("categoryId") Long categoryId,@Param("types") List<String> types);

    Map<String, Object> difficultyStatistic(@Param("categoryId") Long categoryId,@Param("difficulty") List<String> difficulty);

    List<Map<String, Long>> statisticQuestionNum(@Param("cIds") Set<Long> cIds);

    Map<String, Object> selectQuestionCountBySubjectIds(@Param("subjectIds") List<Long> subjectIds);
}
