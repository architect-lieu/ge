package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.QuestionPageParams;
import com.huhuhu.project.form.params.SearchByBase64Params;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
public interface QuestionService extends IService<Question> {

    /**
     * 添加问题
     * @param question
     * @return
     */
    Boolean add(Question question);

    /**
     * 修改问题
     * @param question
     * @return
     */
    Boolean modify(Question question);

    /**
     * 分页获取问题
     * @param questionPageParams
     * @return
     */
    Page<Question> pageList(QuestionPageParams questionPageParams);

    /**
     * 问题详情
     * @param id
     * @return
     */
    Question detail(Long id);

    /**
     * 题型统计
     * @return
     * @param categoryId
     */
    Map<String, Map<String, Object>> typeStatistic(Long categoryId);

    /**
     * 试题集合统计
     * @param categoryId
     * @Param trueQuestionChapterFlag
     * @return
     */
    Map<String, List<Map<String, Object>>> paperStatistic(Long categoryId, Boolean trueQuestionChapterFlag);

    /**
     * 拍照查题
     * @param file
     * @param page
     * @param size
     * @return
     */
    Page<Question> imageSearch(MultipartFile file, int page, int size) throws IOException;

    /**
     * 拍照查题
     * @param searchByBase64Params
     * @return
     */
    Page<Question> imageSearchByBase64(SearchByBase64Params searchByBase64Params);

    /**
     * 导出试题集
     *
     * @param questionPageParams
     * @param response
     */
    void exportQuestionExcel(QuestionPageParams questionPageParams, HttpServletResponse response);

    /**
     * 导入试题
     *
     * @param file
     * @param categoryId
     * @param subjectId
     * @param examId
     */
    void importQuestionExcel(MultipartFile file, Long categoryId, Long subjectId, Long examId);

    void exportQuestionTemplate(HttpServletResponse response);

}
