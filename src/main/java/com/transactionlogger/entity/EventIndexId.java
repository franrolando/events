package com.transactionlogger.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventIndexId implements Serializable {
    private String app;
    private String type;
    private String action;
    private String recordId;
    private String fieldName;
    private String fieldValue;
}
