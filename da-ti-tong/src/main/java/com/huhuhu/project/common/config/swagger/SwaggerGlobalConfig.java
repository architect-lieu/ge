package com.huhuhu.project.common.config.swagger;

import com.huhuhu.project.common.config.entity.SwaggerProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author KangXin
 * @version 1.0
 * @date 2022/6/1 10:09
 */
@Configuration
public class SwaggerGlobalConfig extends BaseSwaggerConfig{

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.huhuhu.project")
                .contactEmail("kangxin@sygence.com")
                .description("刷题通API")
                .title("刷题通API")
                .version("1.0")
                .contactName("KangXin")
                .build();
    }
}
