package com.huhuhu.project.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author konghao
 */
public class WxBizDataCrypt {
    public static Integer CODE_OK = 0;
    public static Integer CODE_IllegalAesKey = -41001;
    public static Integer CODE_IllegalIv = -41002;
    public static Integer CODE_IllegalBuffer = -41003;
    public static Integer CODE_DecodeBase64Error = -41004;
    private String appid;
    private String sessionKey;

    public WxBizDataCrypt(String appid, String sessionKey) {
        this.sessionKey = sessionKey;
        this.appid = appid;
    }

    public Map<String, Object> decryptData(String encryptedData, String iv) {
        Map<String, Object> map = new HashMap();
        String result = AesCbcUtils.decrypt(encryptedData, this.sessionKey, iv, "UTF-8");
        if (null != result && result.length() > 0) {
            JSONObject userInfoJson = JSONObject.parseObject(result);
            Map<String, Object> userInfo = new HashMap();
            userInfo.put("openId", userInfoJson.get("openId"));
            userInfo.put("nickName", userInfoJson.get("nickName"));
            userInfo.put("gender", userInfoJson.get("gender"));
            userInfo.put("city", userInfoJson.get("city"));
            userInfo.put("province", userInfoJson.get("province"));
            userInfo.put("country", userInfoJson.get("country"));
            userInfo.put("avatarUrl", userInfoJson.get("avatarUrl"));
            userInfo.put("unionId", userInfoJson.get("unionId"));
            map.put("userInfo", userInfo);
            map.put("status", 1);
            map.put("msg", "success");
        } else {
            map.put("status", 0);
            map.put("msg", "解密失败");
        }

        return map;
    }

    public JSONObject decryptDataPhone(String encryptedData, String iv) {
        JSONObject userInfoJson = null;
        String result = AesCbcUtils.decrypt(encryptedData, this.sessionKey, iv, "UTF-8");
        if (null != result && result.length() > 0) {
            userInfoJson = JSONObject.parseObject(result);
        }

        return userInfoJson;
    }
}
