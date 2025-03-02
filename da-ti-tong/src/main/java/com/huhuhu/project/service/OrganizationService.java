package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.Organization;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.OrganizationPageParams;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-06-18 03:27:43
 */
public interface OrganizationService extends IService<Organization> {

    /**
     * 添加机构
     * @param organization
     * @return
     */
    Boolean add(Organization organization);

    /**
     * 修改机构
     * @param organization
     * @return
     */
    Boolean modify(Organization organization);

    /**
     * 机构详情
     * @param id
     * @return
     */
    Organization detail(Integer id);

    /**
     * 分页查询
     * @param params
     * @return
     */
    Page<Organization> pageList(OrganizationPageParams params);
}
