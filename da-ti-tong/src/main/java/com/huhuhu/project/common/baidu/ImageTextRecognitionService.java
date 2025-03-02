package com.huhuhu.project.common.baidu;

import com.huhuhu.project.common.baidu.response.AccurateBasicResponse;
import org.json.JSONObject;

/**
 * @author KangXin
 * @version 1.0
 * @desc TODO
 * @date 2023/3/17 14:43
 */
public interface ImageTextRecognitionService {

    /**
     * 请求百度图片文字识别接口  高精度版
     * @param bytes
     * @return
     */
    AccurateBasicResponse accurateBasicRequest(byte[] bytes);

    JSONObject basicGeneral(byte[] bytes);
}
