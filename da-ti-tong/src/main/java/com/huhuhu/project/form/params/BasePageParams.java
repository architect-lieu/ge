package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;

/**
 * @author KangXin
 * @version 1.0
 * @desc TODO
 * @date 2023/3/12 21:35
 */
@Setter
public abstract class BasePageParams {
    @Schema(description = "当前页")
    protected int page;
    @Schema(description = "页大小")
    protected int size;

    public int getPage() {
        return page <= 0 ? 1 : page;
    }

    public int getSize() {
        return size <=0 ? 10 : size;
    }
}
