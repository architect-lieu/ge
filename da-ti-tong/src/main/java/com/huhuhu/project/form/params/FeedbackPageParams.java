package com.huhuhu.project.form.params;

import lombok.Data;

/**
 * @author KangXin
 * @version 1.0
 * @desc TODO
 * @date 2023/4/17 22:45
 */
@Data
public class FeedbackPageParams extends BasePageParams {
    private String type;
    private Integer useFlag;
    private String content;
}
