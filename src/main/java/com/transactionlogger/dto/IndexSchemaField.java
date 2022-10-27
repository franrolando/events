package com.transactionlogger.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IndexSchemaField {

    private String name;
    private String field;

}
