package com.transactionlogger.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RecordSchemaDto {

    private List<IndexSchemaField> filterIndexes;
    private List<String> omits;

}
