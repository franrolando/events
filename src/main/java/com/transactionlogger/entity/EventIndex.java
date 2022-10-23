package com.transactionlogger.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "event_index")
public class EventIndex {

    @Id
    private String app;
    private String type;
    private String action;
    private String recordId;
    private String fieldName;
    private String fieldValue;
    private Date createdAt;

}
