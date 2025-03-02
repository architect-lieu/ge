package com.huhuhu.project.common.config.swagger;

import com.huhuhu.project.common.config.entity.SwaggerProperties;


/**
 * Swagger基础配置
 *
 * @author macro
 * @date 2020/7/16
 */
public abstract class BaseSwaggerConfig {
    /**
     * 自定义Swagger配置
     * @return SwaggerProperties
     */
    public abstract SwaggerProperties swaggerProperties();
}
