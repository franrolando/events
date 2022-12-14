package com.transactionlogger.dto;

import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
public class EventDto {

    private String id;
    private String app;
    private String type;
    private String action;
    private RecordSchemaDto schema;
    @Setter
    private Map<String,Object> data;

}
