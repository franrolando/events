package com.transactionlogger.entity;

import com.transactionlogger.service.EventIndexService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "event_index")
@AllArgsConstructor
@NoArgsConstructor
public class EventIndex implements Serializable {

    @EmbeddedId
    private EventIndexId id;
    private LocalDateTime createdAt;

    public EventIndex(String app, String type, String action, String recordId, String fieldName, String fieldValue) {
        this.id = new EventIndexId(app, type, action, recordId, fieldName, fieldValue);
        this.createdAt = LocalDateTime.now();
    }

}
