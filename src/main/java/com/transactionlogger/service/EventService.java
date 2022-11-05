package com.transactionlogger.service;

import com.google.gson.*;
import com.transactionlogger.dto.*;
import com.transactionlogger.entity.Event;
import com.transactionlogger.entity.EventIndex;
import com.transactionlogger.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventService {

    private EventRepository eventRepository;
    private EventIndexService eventIndexService;

    public EventService(EventRepository eventRepository, EventIndexService eventIndexService) {
        this.eventRepository = eventRepository;
        this.eventIndexService = eventIndexService;
    }

    @Transactional
    public void createRecord(EventDto eventDto) {
        log.info("Creating new record");
        JsonObject dataJson = JsonParser.parseString(eventDto.getData().toString()).getAsJsonObject();
        eventDto.setData(new Gson().fromJson(sanitizeData(eventDto.getSchema().getOmits(), dataJson), HashMap.class));
        Event event = new Event(eventDto);
        eventRepository.save(event);
        eventIndexService.saveIndexes(eventDto,event.getId());
        log.info("Record saved successfully");
    }

    public List<EventDto> searchRecord(SearchQueryDto searchQueryDto) {
        List<EventDto> eventsDto = new ArrayList<>();
        List<EventIndex> events = eventIndexService.searchEventsIndexes(searchQueryDto);
        List<String> ids = events.stream().map( eventIndex -> eventIndex.getId().getRecordId()).collect(Collectors.toList());
        this.eventRepository.findAllById(ids).forEach( event -> {
            eventsDto.add(EventDto.builder()
                    .id(event.getId())
                    .app(event.getApp())
                    .type(event.getType())
                    .action(event.getAction())
                    .schema(new Gson().fromJson(event.getSchema(), RecordSchemaDto.class))
                    .data(new Gson().fromJson(event.getData(), HashMap.class))
                    .build()
            );
        });
        return eventsDto;
    }

    private JsonObject sanitizeData(List<String> omits, JsonObject data) {
        JsonObject deepCopy = data.deepCopy();
        omits.forEach(omitField -> {
            removeField(omitField, deepCopy);
        });
        return deepCopy;
    }

    private boolean removeField(String field, JsonObject object) {
        boolean fieldExist = false;
        if (object.has(field)) {
            object.remove(field);
            fieldExist = true;
        } else {
            Iterator<String> it = object.keySet().iterator();
            while (it.hasNext() && !fieldExist) {
                String attributeKey = it.next();
                JsonElement attribute = object.get(attributeKey);
                if (attribute.isJsonObject()) {
                    fieldExist = removeField(field, attribute.getAsJsonObject());
                    if (fieldExist && attribute.getAsJsonObject().keySet().isEmpty()) {
                        object.remove(attributeKey);
                    }
                }
            }
        }
        return fieldExist;
    }

    @Transactional
    public void deleteRecord(DeleteDto deleteDto) {
        eventRepository.deleteById(deleteDto.getRecordId());
        eventIndexService.deleteById(deleteDto);
    }

    public EventDto updateRecord(UpdateDto updateDto) {
        log.info("Updating record");
        if (eventIndexService.existsEvent(updateDto)){
            log.info("Record Exists. Updating ...");
            //eventRepository.deleteById(updateDto.getRecordId());
            //eventIndexService.updateIndex(updateDto);
        }
        return null;
    }
}
