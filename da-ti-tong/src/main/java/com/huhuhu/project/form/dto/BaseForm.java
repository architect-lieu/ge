package com.huhuhu.project.form.dto;

/**
 * @author KangXin
 * @version 1.0
 * @desc TODO
 * @date 2023/3/12 9:12
 */
public abstract class BaseForm<T> {
    protected abstract T build();
}
