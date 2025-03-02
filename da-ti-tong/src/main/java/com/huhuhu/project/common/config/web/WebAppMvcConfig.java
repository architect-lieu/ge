package com.huhuhu.project.common.config.web;

import com.huhuhu.project.common.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @Classname Knife4jWebMvcConfig
 * @Date 2021/3/2 15:27
 */
@Configuration
public class WebAppMvcConfig extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 注册自定义的显示 ResponseResult 注解的拦截器
         */
        registry.addInterceptor(new LoginCheckInterceptor())
                // 拦截配置
                .addPathPatterns("/**")
                // 排除配置
                .excludePathPatterns(
                        "/**/error",
                        "/**/wx/callback/**",
                        "/**/customer/api/project/customer/send/sm",
                        "/**/customer/api/project/vip-config/list",
                        "/**/customer/api/project/document/list",
                        "/**/customer/api/project/category/tree",
                        "/**/customer/api/project/customer/min-app/login",
                        "/**/customer/api/project/customer/sm/login",
                        "/**/customer/api/project/customer/min-app/phone",
                        "/**/admin/api/project/admin/login",
                        "/**/doc.html",
                        "/**/webjars/**",
                        "/**/favicon.ico")
                .excludePathPatterns("/**/swagger-ui/**","/**/v3/api-docs/**");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOriginPatterns("*").allowCredentials(true)
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH").maxAge(3600);
    }
}