package com.huhuhu.project.common.login;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/12 9:46
 */
public interface ThirdPartyLoginService<T> {
    T login(String code);
}
