package com.huhuhu.project.utils;

import cn.hutool.core.bean.BeanUtil;
import com.huhuhu.project.common.exception.BusinessException;

/**
 * @author KangXin
 * @version 1.0
 * @date 2022/6/1 11:28
 */
public class ConvertVoUtil {

    public static<T> T convert(Object source,Class<T> clazz) {
        try {
            T t = clazz.newInstance();
            BeanUtil.copyProperties(source,t);
            return t;
        } catch (Exception e) {
           throw new BusinessException(3001,"系统异常，类型转换失败！");
        }
    }

}
