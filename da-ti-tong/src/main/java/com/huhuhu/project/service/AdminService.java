package com.huhuhu.project.service;

import com.huhuhu.project.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.form.params.AdminLoginParams;
import com.huhuhu.project.vo.AdminVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-13 10:18:48
 */
public interface AdminService extends IService<Admin> {

    /**
     * 用户登陆
     * @param adminLoginParam
     * @return
     */
    AdminVo login(AdminLoginParams adminLoginParam);
}
