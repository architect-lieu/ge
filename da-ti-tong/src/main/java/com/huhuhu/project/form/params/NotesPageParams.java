package com.huhuhu.project.form.params;

import lombok.Data;

/**
 * @author KangXin
 * @version 1.0
 * @desc TODO
 * @date 2023/4/17 22:30
 */
@Data
public class NotesPageParams extends BasePageParams {
    private String content;
    private Long categoryId;
}
