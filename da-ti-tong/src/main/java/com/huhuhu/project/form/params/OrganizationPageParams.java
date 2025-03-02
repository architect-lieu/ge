package com.huhuhu.project.form.params;

import lombok.Data;

@Data
public class OrganizationPageParams extends BasePageParams{
    private String name;
    private String code;
    private Integer vipFlag;
}
