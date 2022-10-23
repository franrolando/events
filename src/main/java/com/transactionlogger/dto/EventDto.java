package com.transactionlogger.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EventDto {

    private String app;
    private String type;
    private String action;
    private RecordSchemaDto schema;
    private String data;

}
