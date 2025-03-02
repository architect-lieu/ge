
package com.huhuhu.project.common.baidu.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccurateBasicResponse {
    @JSONField(name = "log_id")
    private Long logId;
    @JSONField(name = "words_result")
    private List<WordsResult> wordsResult;
    @JSONField(name = "words_result_num")
    private Long wordsResultNum;
    @Data
    public static class WordsResult {
        private String words;
        private Probability probability;
    }

    @Data
    public static class Probability{
        private BigDecimal average;
        private BigDecimal min;
        private BigDecimal variance;
    }
}
