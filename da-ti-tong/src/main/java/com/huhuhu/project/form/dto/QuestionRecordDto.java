package com.huhuhu.project.form.dto;

import com.huhuhu.project.entity.ErrorQuestionRecord;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class QuestionRecordDto {

    @Valid
    private List<ErrorQuestionRecord> questionRecordList;
}
