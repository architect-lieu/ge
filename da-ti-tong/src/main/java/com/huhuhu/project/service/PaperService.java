package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Paper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.PaperPageParams;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
public interface PaperService extends IService<Paper> {

    /**
     * 添加试题集
     * @param paper
     * @return
     */
    Boolean add(Paper paper);

    /**
     * 修改试题集
     * @param paper
     * @return
     */
    Boolean modify(Paper paper);

    /**
     * 分页查询试题集
     * @param params
     * @return
     */
    Page<Paper> pageList(PaperPageParams params);

    /**
     * 试题集详情
     * @param id
     * @return
     */
    Paper detail(Long id);
}
