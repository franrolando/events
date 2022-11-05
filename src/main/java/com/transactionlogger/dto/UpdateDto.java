package com.transactionlogger.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
public class UpdateDto {
    private String app;
    private String type;
    private String action;
    private String recordId;
    private String fieldName;
    private String fieldValue;

    private RecordSchemaDto schema;
    @Setter
    private Map<String,Object> data;
}
