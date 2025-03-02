package com.huhuhu.project.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.entity.VipConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.VipConfigPageParams;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 03:24:30
 */
public interface VipConfigService extends IService<VipConfig> {

    /**
     * 添加vip配置
     * @param vipConfig
     * @return
     */
    Boolean add(VipConfig vipConfig);

    /**
     * 更新vip
     * @param vipConfig
     * @return
     */
    Boolean modify(VipConfig vipConfig);

    /**
     * 分页查询配置
     * @param vipConfigPageParams
     * @return
     */
    Page<VipConfig> pageList(VipConfigPageParams vipConfigPageParams);

    /**
     * 详情
     * @param id
     * @return
     */
    VipConfig detail(Long id);
}
