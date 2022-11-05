package com.transactionlogger.dto;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jboss.jandex.Index;

import java.util.List;

@AllArgsConstructor
@Data
public class IndexRecordData {

    private String fieldName;
    private List<String> hashedValues;

}
