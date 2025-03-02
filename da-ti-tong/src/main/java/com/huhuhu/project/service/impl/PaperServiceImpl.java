package com.huhuhu.project.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.Paper;
import com.huhuhu.project.form.params.PaperPageParams;
import com.huhuhu.project.mapper.PaperMapper;
import com.huhuhu.project.service.PaperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {

    @Override
    public Boolean add(Paper paper) {
        return this.save(paper);
    }

    @Override
    public Boolean modify(Paper paper) {
        return this.updateById(paper);
    }

    @Override
    public Page<Paper> pageList(PaperPageParams params) {
        String paperName = params.getPaperName();
        Long chapterId = params.getChapterId();
        LambdaQueryWrapper<Paper> queryWrapper = Wrappers.lambdaQuery(Paper.class).eq(chapterId != null, Paper::getChapterId, chapterId)
                .like(StrUtil.isNotBlank(paperName), Paper::getPaperName, paperName)
                .orderByDesc(Paper::getCreateTime);
        return this.page(Page.of(params.getPage(), params.getSize()), queryWrapper);
    }

    @Override
    public Paper detail(Long id) {
        Paper paper = this.getById(id);
        if (paper == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return paper;
    }
}
