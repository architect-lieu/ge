package com.huhuhu.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Admin;
import com.huhuhu.project.form.params.AdminLoginParams;
import com.huhuhu.project.mapper.AdminMapper;
import com.huhuhu.project.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.TokenUtil;
import com.huhuhu.project.vo.AdminVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-13 10:18:48
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Override
    public AdminVo login(AdminLoginParams adminLoginParam) {
        String username = adminLoginParam.getUsername();
        String password = adminLoginParam.getPassword();
        // 根据用户名查询用户
        Admin admin = this.getOne(Wrappers.lambdaQuery(Admin.class).eq(Admin::getAdminName, username));
        if (admin == null) {
            throw new BusinessException(ResultCode.USER_NOT_FIND);
        }
        String adminPassword = admin.getAdminPassword();
        if (!adminPassword.equals(password)) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        AdminVo adminVo = BeanUtil.copyProperties(admin, AdminVo.class);
        String token = TokenUtil.createToken(admin);
        adminVo.setToken(token);
        return adminVo;
    }
}
