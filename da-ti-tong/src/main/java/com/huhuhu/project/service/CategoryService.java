package com.huhuhu.project.service;

import com.huhuhu.project.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huhuhu.project.vo.CategoryVo;

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
public interface CategoryService extends IService<Category> {

    /**
     * 添加分类
     * @param category
     * @return
     */
    Boolean add(Category category);

    /**
     * 更新分类
     * @param category
     * @return
     */
    Boolean modify(Category category);

    /**
     * 获取分类树
     * @return
     */
    List<CategoryVo> tree(Long pid, String currentLoginUserType);

    /**
     * 查询分类 连同其下的分类
     * @param id
     * @return
     */
    CategoryVo detailWithChildren(Long id);

    /**
     * 分类详情
     * @param id
     * @return
     */
    Category detail(Long id);

    /**
     * 查询分类以及下面的子分类
     * @param keyword
     * @return
     */
    List<CategoryVo> search(String keyword);

    /**
     * 错题 收藏 笔记 题目总数
     * @param categoryId
     * @return
     */
    Map<String, Object> indexStatistic(Long categoryId);

    /**
     * 当前分类下登陆用户的相关统计
     * @param categoryId
     * @return
     */
    Map<String, Object> categoryStatistic(Long categoryId);

    /**
     * 获取分类下的叶子节点
     * @param pid
     * @return
     */
    List<CategoryVo> childrenList(Long pid);

    Boolean del(List<Long> ids);
}
