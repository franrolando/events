package com.transactionlogger.entity;

import com.github.ksuid.Ksuid;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.transactionlogger.dto.EventDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@Entity
@Table(name = "event")
@NoArgsConstructor
@Data
public class Event {

    @Id
    private String id;
    private String app;
    private String type;
    private String action;
    private String schema;
    private String data;
    private LocalDateTime createdAt;

    public Event(EventDto eventDto){
        this.id = Ksuid.newKsuid().toString();
        this.app = eventDto.getApp();
        this.type = eventDto.getType();
        this.action = eventDto.getAction();
        this.schema = new Gson().toJson(eventDto.getSchema());
        this.data = JsonParser.parseString(eventDto.getData().toString()).getAsJsonObject().toString();
        this.createdAt = LocalDateTime.now();
    }

}
