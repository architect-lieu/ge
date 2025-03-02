package com.huhuhu.project.common.baidu.impl;

import com.alibaba.fastjson.JSON;
import com.huhuhu.project.common.baidu.ImageTextRecognitionService;
import com.huhuhu.project.common.baidu.config.BaiDuAipOcr;
import com.huhuhu.project.common.baidu.response.AccurateBasicResponse;
import com.huhuhu.project.common.exception.BusinessException;
import com.huhuhu.project.common.exception.enums.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/17 14:44
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ImageTextRecognitionServiceImpl implements ImageTextRecognitionService {

    private final BaiDuAipOcr baiDuAipOcr;

    @Override
    public AccurateBasicResponse accurateBasicRequest(byte[] bytes) {
        try {
            HashMap<String, String> options = new HashMap<>();
            options.put("detect_direction", "true");
            options.put("probability", "true");
            JSONObject resultJson = baiDuAipOcr.basicAccurateGeneral(bytes, options);
            String jsonString = resultJson.toString();
            log.info("识别结果:{}", jsonString);
            return JSON.parseObject(jsonString, AccurateBasicResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("百度识别图片失败:{}", e.getMessage());
        }
        throw new BusinessException(ResultCode.IMAGE_PARSE_ERROR);
    }

    @Override
    public JSONObject basicGeneral(byte[] bytes) {
        HashMap<String, String> options = new HashMap<>();
        options.put("detect_direction", "true");
        options.put("probability", "true");
        return baiDuAipOcr.basicGeneral(bytes, options);
    }
}
