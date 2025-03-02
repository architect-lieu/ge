package com.huhuhu.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.huhuhu.project.common.constant.SystemConstant;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import com.huhuhu.project.entity.*;
import com.huhuhu.project.enums.CollectTypeEnum;
import com.huhuhu.project.enums.QuestionStatus;
import com.huhuhu.project.enums.QuestionTypeEnum;
import com.huhuhu.project.mapper.*;
import com.huhuhu.project.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huhuhu.project.utils.TokenUtil;
import com.huhuhu.project.vo.CategoryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author KangXin
 * @since 2023-03-10 07:56:41
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final UserCollectMapper userCollectMapper;

    private final ErrorQuestionRecordMapper errorQuestionRecordMapper;

    private final QuestionMapper questionMapper;

    private final NotesMapper notesMapper;

    @Override
    @Transactional
    public Boolean add(Category category) {
        Long parentId = category.getParentId();
        // 设置父节点标识
        category.setParentFlag(category.getParentId() == null || category.getParentFlag());
        if (parentId == null || parentId == 0) {
            category.setParentId(0L);
            category.setLevel(1);
        } else {
            // 更新父节点
            Category parent = this.getOne(Wrappers.lambdaQuery(Category.class).eq(Category::getCategoryId, parentId));
            if (parent == null) {
                throw new BusinessException(3001, String.format("不存在ID为[ %d ]的父节点", parentId));
            }
            category.setLevel(parent.getLevel() + 1);
            if (!parent.getParentFlag()) {
                parent.setParentFlag(Boolean.TRUE);
            }
            this.updateById(parent);
        }
        return this.save(category);
    }

    @Override
    @Transactional
    public Boolean modify(Category category) {
        // 先查
        Long categoryId = category.getCategoryId();
        Category oldCategory = this.getById(categoryId);
        // 改变了父节点
        List<Category> updateList = new ArrayList<>();
        if (!category.getParentId().equals(oldCategory.getParentId())) {
            // 查询原先父节点下有没有子节点
            Long parentId = oldCategory.getParentId();
            long count = this.count(Wrappers.lambdaQuery(Category.class).eq(Category::getParentId, parentId));
            // 更新父节点标识
            if (count == 0) {
                this.update(Wrappers.lambdaUpdate(Category.class).eq(Category::getCategoryId, parentId).set(Category::getParentFlag, false));
            }
            // 查询新指定的父节点的级别
            Category newParent = this.getById(category.getParentId());
            if (!newParent.getParentFlag()) {
                newParent.setParentFlag(Boolean.TRUE);
                updateList.add(newParent);
            }
            Integer level = newParent.getLevel();
            if (level + 1 != oldCategory.getLevel()) {
                category.setLevel(level + 1);
                // 递归更新子节点级别
                updateHelper(category, updateList);
            }
        }
        // 批量更新
        updateList.add(category);
        return this.updateBatchById(updateList);
    }

    @Override
    public List<CategoryVo> tree(Long pid, String currentLoginUserType) {
        // 先查根节点
        List<Category> parents = this.list(Wrappers.lambdaQuery(Category.class).eq(Category::getParentId, pid));
        List<CategoryVo> result = new ArrayList<>();
        //
        List<UserCollect> userCollects = Collections.emptyList();
        if (TokenUtil.loginCheck()) {
            userCollects = userCollectMapper.selectList(Wrappers.lambdaQuery(UserCollect.class).eq(UserCollect::getUserId, TokenUtil.currentLoginUserId()).eq(UserCollect::getCollectType,
                    CollectTypeEnum.TYPE.getCode()));
        }
        Map<Long, Boolean> collected = new HashMap<>();
        if (CollUtil.isNotEmpty(userCollects)) {
            collected = userCollects.stream().collect(Collectors.toMap(UserCollect::getCategoryId, item -> Boolean.TRUE, (o1, o2) -> o2));
        }
        // 递归构造
        if (CollUtil.isNotEmpty(parents)) {
            for (Category parent : parents) {
                CategoryVo categoryVo = new CategoryVo();
                BeanUtil.copyProperties(parent, categoryVo);
                if (parent.getParentFlag().equals(Boolean.TRUE)) {
                    List<CategoryVo> children = treeHelper(parent, collected, currentLoginUserType);
                    categoryVo.setChildren(children);
                } else {
                    Long questionNum = questionMapper.selectCount(Wrappers.lambdaQuery(Question.class).eq(Question::getCategoryId, parent.getCategoryId()));
                    categoryVo.setQuestionNum(questionNum);
                    // 查询收藏
                    Boolean aBoolean = collected.get(parent.getCategoryId());
                    categoryVo.setUserCollectFlag(aBoolean == null ? Boolean.FALSE : aBoolean);
                }
                result.add(categoryVo);
            }
        }
        return result;
    }

    @Override
    public CategoryVo detailWithChildren(Long id) {
        Category category = this.getById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        List<CategoryVo> categoryVos = treeHelper(category, null, SystemConstant.ADMIN);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtil.copyProperties(category, categoryVo);
        categoryVo.setChildren(categoryVos);
        return categoryVo;
    }

    @Override
    public Category detail(Long id) {
        Category category = this.getById(id);
        if (category == null) {
            throw new BusinessException(ResultCode.CHAPTER_NOT_FIND);
        }
        return category;
    }

    @Override
    public List<CategoryVo> search(String keyword) {
        if (StrUtil.isBlank(keyword))
            return new ArrayList<>();
        List<Category> categories = this.list(Wrappers.lambdaQuery(Category.class)
                .like(Category::getCategoryName, keyword));
        List<CategoryVo> result = new ArrayList<>();
        String currentLoginUserType = TokenUtil.currentLoginUserType();
        if (CollUtil.isNotEmpty(categories)) {
            for (Category category : categories) {
                CategoryVo categoryVo = BeanUtil.copyProperties(category, CategoryVo.class);
                categoryVo.setChildren(treeHelper(category, null, currentLoginUserType));
                result.add(categoryVo);
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> indexStatistic(Long categoryId) {
        Long collectNum = userCollectMapper.selectCount(Wrappers.lambdaQuery(UserCollect.class)
                .eq(UserCollect::getUserId, TokenUtil.currentLoginUserId())
                .eq(UserCollect::getCollectType, CollectTypeEnum.QUESTION.getCode())
                .eq(UserCollect::getCategoryId, categoryId));
        Long errorNum = errorQuestionRecordMapper.selectCount(Wrappers.lambdaQuery(ErrorQuestionRecord.class)
                .eq(ErrorQuestionRecord::getUserId, TokenUtil.currentLoginUserId())
                .eq(ErrorQuestionRecord::getStatus, QuestionStatus.ERROR.getCode())
                .eq(ErrorQuestionRecord::getCategoryId, categoryId));
        Long questionNum = questionMapper.selectCount(Wrappers.lambdaQuery(Question.class).eq(Question::getCategoryId, categoryId));
        Long notesNum = notesMapper.selectCount(Wrappers.lambdaQuery(Notes.class)
                .eq(Notes::getCategoryId, categoryId)
                .eq(Notes::getUserId, TokenUtil.currentLoginUserId()));
        Map<String, Object> result = new HashMap<>();
        result.put("collectNum", collectNum == null ? 0 : collectNum);
        result.put("errorNum", errorNum == null ? 0 : errorNum);
        result.put("questionNum", questionNum == null ? 0 : questionNum);
        result.put("notesNum", notesNum == null ? 0 : notesNum);
        return result;
    }

    @Override
    public Map<String, Object> categoryStatistic(Long categoryId) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> questionStatisticMap = errorQuestionRecordMapper.categoryStatistic(categoryId, TokenUtil.currentLoginUserId(), QuestionTypeEnum.getNames());
        Long totalNum = questionMapper.selectCount(Wrappers.lambdaQuery(Question.class).eq(Question::getCategoryId, categoryId));
        questionStatisticMap.put("totalQuestionNum", totalNum == null ? 0 : totalNum);
        result.put("questionStatistic", questionStatisticMap);

        Map<String, Object> collectStatisticMap = userCollectMapper.categoryStatistic(categoryId, TokenUtil.currentLoginUserId(), QuestionTypeEnum.getNames());
        result.put("collectStatistic", collectStatisticMap);
        return result;
    }

    @Override
    public List<CategoryVo> childrenList(Long pid) {
        List<Category> categories = this.list(Wrappers.lambdaQuery(Category.class).eq(Category::getParentId, pid).eq(Category::getParentFlag, Boolean.FALSE));
        return BeanUtil.copyToList(categories, CategoryVo.class);
    }

    @Override
    @Transactional
    public Boolean del(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Boolean.TRUE;
        }
        List<Category> categories = this.list(Wrappers.<Category>lambdaQuery().in(Category::getCategoryId, ids));
        if (CollUtil.isEmpty(categories)) {
            // 递归删除
            return Boolean.TRUE;
        }
        List<Long> childrenIds = this.list(Wrappers.<Category>lambdaQuery().in(Category::getParentId, ids))
                .stream().map(Category::getCategoryId)
                .collect(Collectors.toList());
        del(childrenIds);
        userCollectMapper.delete(Wrappers.<UserCollect>lambdaQuery().in(UserCollect::getCategoryId, ids));
        return this.removeBatchByIds(ids);
    }

    private List<CategoryVo> treeHelper(Category parent, Map<Long, Boolean> collected, String currentLoginUserType) {
        if (!parent.getParentFlag()) {
            return new ArrayList<>();
        }
        // 查询子节点
        List<Category> categories;
        if (SystemConstant.CUSTOMER.equals(currentLoginUserType)) {
            categories = this.list(Wrappers.lambdaQuery(Category.class).eq(Category::getParentId, parent.getCategoryId()));
        }else {
            categories = this.list(Wrappers.lambdaQuery(Category.class).eq(Category::getParentId, parent.getCategoryId())
                    .eq(Category::getParentFlag, Boolean.TRUE));
        }
        List<CategoryVo> result = new ArrayList<>();
        if (CollUtil.isNotEmpty(categories)) {
            for (Category category : categories) {
                List<CategoryVo> categoryVos = treeHelper(category, collected, currentLoginUserType);
                CategoryVo categoryVo = new CategoryVo();
                BeanUtil.copyProperties(category, categoryVo);
                if (collected != null && categoryVos.size() == 0) {
                    categoryVo.setUserCollectFlag(collected.get(category.getCategoryId()) == null ? Boolean.FALSE : Boolean.TRUE);
                }
                categoryVo.setChildren(categoryVos);
                result.add(categoryVo);
            }
        }
        return result;
    }

    private void updateHelper(Category parentCategory, List<Category> updateList) {
        Integer level = parentCategory.getLevel();
        List<Category> categories = this.list(Wrappers.lambdaQuery(Category.class).eq(Category::getParentId, parentCategory.getCategoryId()));
        if (CollUtil.isNotEmpty(categories)) {
            categories.forEach(c -> {
                c.setLevel(level + 1);
                updateList.add(c);
                updateHelper(c, updateList);
            });
        }
    }


}
