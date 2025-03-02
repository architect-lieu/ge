package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.SubjectPageParams;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-07-01 06:02:37
 */
public interface SubjectService extends IService<Subject> {

    /**
     * 添加科目
     * @param subject
     * @return
     */
    Boolean add(Subject subject);

    /**
     * 修改科目
     * @param subject
     * @return
     */
    Boolean modify(Subject subject);

    /**
     * 科目分页
     * @param param
     * @return
     */
    Page<Subject> pageList(SubjectPageParams param);

    /**
     * 详情
     * @param id
     * @return
     */
    Subject detail(Long id);

    /**
     * 分类下的所有科目
     * @param categoryId
     * @return
     */
    List<Subject> allList(Long categoryId);
}
