package com.transactionlogger.dto;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Data;

@Data
public class FilterDto implements Comparable {
    private String fieldName;
    private JsonPrimitive fieldValue;

    @Override
    public int compareTo(Object o) {
        FilterDto filter = (FilterDto) o;
        return this.fieldName.compareTo(filter.getFieldName());
    }

}
