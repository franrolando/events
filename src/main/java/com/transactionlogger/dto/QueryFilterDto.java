package com.transactionlogger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryFilterDto implements Serializable {

    private String fieldName;
    private String fieldValue;
}
