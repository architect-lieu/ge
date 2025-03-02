package com.huhuhu.project.common.config;

import cn.felord.payment.autoconfigure.EnableMobilePay;
import com.aliyun.oss.OSSClientBuilder;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author KangXin
 * @version 1.0
 */
@Configuration
@ConfigurationPropertiesScan(basePackages = "com.huhuhu.project.common")
//@EnableMobilePay
@MapperScan(basePackages = "com.huhuhu.project.mapper")
@OpenAPIDefinition(info = @Info(title = "答题通文档", version = "openapi: 3.0.1", description = "答题通文档"))
public class GlobalScanConfig {
    @Bean
    public OSSClientBuilder clientBuilder() {
        return new OSSClientBuilder();
    }
}
