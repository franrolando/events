package com.transactionlogger.dto;

import lombok.Getter;

@Getter
public class DeleteDto {
    private String app;
    private String type;
    private String action;
    private String recordId;
    private String fieldName;
    private String fieldValue;
}
