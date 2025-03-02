package com.huhuhu.project.common.sms.impl;

import com.alibaba.fastjson.JSON;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.sms.SmsService;
import darabonba.core.client.ClientOverrideConfiguration;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/10 19:27
 */
@Service
@Slf4j
public class AliSmsServiceImpl implements SmsService {

    @Value("${aliyun.sms.accessKey}")
    private String accessKey;

    @Value("${aliyun.sms.accessSecret}")
    private String accessSecret;

    @Value("${aliyun.sms.tmpCode}")
    private String tmpCode;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Resource
    private AliyunSmsCodeMapping aliyunSmsCodeMapping;

    @Override
    public boolean sendMessage(String phone, Map<String, Object> templateParam) {
        // Configure Credentials authentication information, including ak, secret, token
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKey)
                .accessKeySecret(accessSecret)
                //.securityToken("<your-token>") // use STS token
                .build());
        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou") // Region ID
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                        .setConnectTimeout(Duration.ofSeconds(60))
                )
                .build();

        // Parameter settings for API request
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .phoneNumbers(phone)
                .signName(signName)
                .templateCode(tmpCode)
                .templateParam(JSON.toJSONString(templateParam))
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        // Synchronously get the return value of the API request
        try {
            SendSmsResponse resp = response.get();
            SendSmsResponseBody body = resp.getBody();
            log.info(JSON.toJSONString(body));
            if (body.getCode().equals("OK")) {
                return true;
            }
            throw new BusinessException(aliyunSmsCodeMapping.apply(body.getCode()));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        client.close();
        return false;
    }
}
