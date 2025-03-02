package com.huhuhu.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Organization;
import com.huhuhu.project.form.params.OrganizationPageParams;
import com.huhuhu.project.mapper.OrganizationMapper;
import com.huhuhu.project.service.OrganizationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-06-18 03:27:43
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    @Override
    public Boolean add(Organization organization) {
        Organization org = this.getOne(Wrappers.lambdaQuery(Organization.class).eq(Organization::getOrgCode, organization.getOrgCode()));
        if (org != null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED.getCode(), "已存在此机构编码～！");
        }
        // 保存
        this.save(organization);
        // 生成激活码
        organization.setActiveCode(StrUtil.fillBefore(organization.getId().toString(),'0', 6));
        return this.updateById(organization);
    }

    @Override
    public Boolean modify(Organization organization) {
        Organization org = this.getOne(Wrappers.lambdaQuery(Organization.class)
                .eq(Organization::getId, organization.getId()));
        if (org == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED.getCode(), "不存在此机构编～！");
        }
        // 生成激活码
        organization.setActiveCode(StrUtil.fillBefore(organization.getId().toString(),'0', 6));
        return this.updateById(organization);
    }

    @Override
    public Organization detail(Integer id) {
        Organization organization = this.getById(id);
        if (organization == null) {
            throw new BusinessException(ResultCode.VALIDATE_FAILED.getCode(), "不存在此机构编～！");
        }
        return organization;
    }

    @Override
    public Page<Organization> pageList(OrganizationPageParams params) {
        String name = params.getName();
        Integer vipFlag = params.getVipFlag();
        String code = params.getCode();
        LambdaQueryWrapper<Organization> queryWrapper = Wrappers.lambdaQuery(Organization.class)
                .like(StrUtil.isNotBlank(name), Organization::getOrgName, name)
                .eq(StrUtil.isNotBlank(code), Organization::getOrgCode, code)
                .eq(vipFlag != null, Organization::getVipFlag, vipFlag);
        return this.page(Page.of(params.getPage(), params.getSize()), queryWrapper);
    }
}
