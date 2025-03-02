package com.huhuhu.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.cache.JvmCache;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.VipConfig;
import com.huhuhu.project.form.params.VipConfigPageParams;
import com.huhuhu.project.mapper.VipConfigMapper;
import com.huhuhu.project.service.VipConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-05-02 03:24:30
 */
@Service
public class VipConfigServiceImpl extends ServiceImpl<VipConfigMapper, VipConfig> implements VipConfigService {

    @Override
    @Transactional
    public Boolean add(VipConfig vipConfig) {
        JvmCache.clearVipConfig();
        return this.save(vipConfig);
    }

    @Override
    @Transactional
    public Boolean modify(VipConfig vipConfig) {
        JvmCache.clearVipConfig();
        return this.updateById(vipConfig);
    }

    @Override
    public Page<VipConfig> pageList(VipConfigPageParams vipConfigPageParams) {
        String type = vipConfigPageParams.getType();
        LambdaQueryWrapper<VipConfig> queryWrapper = Wrappers.lambdaQuery(VipConfig.class).eq(StrUtil.isNotBlank(type), VipConfig::getVipType, type);
        return this.page(Page.of(vipConfigPageParams.getPage(), vipConfigPageParams.getSize()), queryWrapper);
    }

    @Override
    public VipConfig detail(Long id) {
        VipConfig vipConfig = this.getById(id);
        if (vipConfig == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return vipConfig;
    }
}
