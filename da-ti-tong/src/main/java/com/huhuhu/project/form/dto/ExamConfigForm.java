package com.huhuhu.project.form.dto;

import com.huhuhu.project.entity.ExamConfig;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

/**
 * @author KangXin
 * @version 1.0
 * @date 2023/3/14 11:29
 */
@Data
public class ExamConfigForm {
    @Valid
    private List<ExamConfig> examConfigs;
}
