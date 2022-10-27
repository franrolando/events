package com.transactionlogger.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

//@Entity
//@Table(name = "event")
public class Event {

    @Id
    private String id;
    private String app;
    private String type;
    private String action;
    private String schema;
    private String data;
    private Date createdAt;


}
