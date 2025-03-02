package com.huhuhu.project.form.params;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/12 22:44
 */
@Data
public class CustomerPageParams extends BasePageParams{
    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "手机号")
    private String mobilephone;
    @Schema(description = "会员标识")
    private Integer vipFlag;
    @Schema(description = "用户来源: MIN_APP APP")
    private String source;
    
    private Date startTime;
    
    private Date endTime;
}
