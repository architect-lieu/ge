package com.huhuhu.project.common.baidu.config;

import com.baidu.aip.ocr.AipOcr;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/17 15:10
 */
@ConfigurationProperties(prefix = "baidu")
@Getter
public class BaiDuAipOcr extends AipOcr {
    private final String appId;
    private final String appKey;
    private final String secretKey;

    @ConstructorBinding
    public BaiDuAipOcr(String appId,
                       String appKey,
                       String secretKey) {

        super(appId, appKey, secretKey);
        this.appId = appId;
        this.appKey = appKey;
        this.secretKey = secretKey;

        this.setConnectionTimeoutInMillis(2000);
        this.setSocketTimeoutInMillis(60000);
    }
}
