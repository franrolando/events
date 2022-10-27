package com.transactionlogger.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.transactionlogger.dto.EventDto;
import com.transactionlogger.dto.IndexSchemaField;
import com.transactionlogger.dto.SearchQueryDto;
import com.transactionlogger.entity.EventIndex;
import com.transactionlogger.repository.EventIndexRepository;
import com.transactionlogger.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class EventService {

    private EventRepository eventRepository;
    private EventIndexRepository eventIndexRepository;

    public EventService(/*EventRepository eventRepository, EventIndexRepository eventIndexRepository*/) {
        this.eventRepository = eventRepository;
        this.eventIndexRepository = eventIndexRepository;
    }

    @Transactional
    public void createRecord(EventDto eventDto) {
        JsonObject dataJson = JsonParser.parseString(eventDto.getData().toString()).getAsJsonObject();
        sanitizeData(eventDto.getSchema().getOmits(), dataJson);
        //eventIndexRepository.save(createEventIndexRecordData(eventDto.getSchema().getFilterIndexes(), dataJson));
        //eventRepository.save(eventDto);
    }

    public List<EventDto> searchRecord(SearchQueryDto searchQueryDto) {
        return null;
    }

    private EventIndex createEventIndexRecordData(List<IndexSchemaField> indexSchemaFields, JsonObject data) {
        EventIndex eventIndex = new EventIndex();
        Map<String, JsonElement> values = new HashMap<>();
        indexSchemaFields.forEach(index -> {
            String name = index.getName();
            JsonElement field = existField(index.getField(), data);
            if (field == null){
                throw new RuntimeException(MessageFormat.format("Field {0} is not in the data", index.getField()));
            }
        });
        return eventIndex;
    }

    private void sanitizeData(List<String> omits, JsonObject data){
        omits.forEach( omitField -> {
           removeField(omitField, data);
        });
    }

    private boolean removeField(String field, JsonObject object){
        boolean fieldExist = false;
        if (object.has(field)){
            object.remove(field);
            fieldExist = true;
        } else {
            Iterator<String> it = object.keySet().iterator();
            while (it.hasNext() && !fieldExist){
                String attributeKey = it.next();
                JsonElement attribute = object.get(attributeKey);
                if (attribute.isJsonObject()){
                    fieldExist = removeField(field, attribute.getAsJsonObject());
                    if (fieldExist && attribute.getAsJsonObject().keySet().isEmpty()){
                        object.remove(attributeKey);
                    }
                }
            }
        }
        return fieldExist;
    }

    private JsonElement existField(String field, JsonObject object){
        JsonElement jsonField = JsonNull.INSTANCE;
        if (object.has(field)){
            jsonField = object.get(field);
        } else {
            Iterator<String> it = object.keySet().iterator();
            while (it.hasNext() && jsonField.isJsonNull()){
                String attributeKey = it.next();
                JsonElement attribute = object.get(attributeKey);
                if (attribute.isJsonObject()){
                    jsonField = existField(field, attribute.getAsJsonObject());
                }
            }
        }
        return jsonField;
    }

}
