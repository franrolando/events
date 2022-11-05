package com.transactionlogger.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.transactionlogger.dto.*;
import com.transactionlogger.service.EventService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/createEvent")
    public ResponseEntity createEvent(@RequestBody EventDto eventDto) {
        eventService.createRecord(eventDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/search/{app}")
    public ResponseEntity<List<EventDto>> searchEvents(@PathVariable String app,
                                       @RequestParam(required = false, defaultValue = "" ) String type,
                                       @RequestParam(required = false, defaultValue = "" ) String action,
                                       @RequestParam(required = false, defaultValue = "0") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate, @RequestBody List<Map<String, Object>> filters) {
        SearchQueryDto searchQueryDto = SearchQueryDto.builder()
                .app(app)
                .type(type)
                .action(action)
                .page(page)
                .pageSize(pageSize)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        if (filters != null && !filters.isEmpty()){
            JsonArray dataJson = JsonParser.parseString(filters.toString()).getAsJsonArray();
            List<FilterDto> filterDto = new Gson().fromJson(dataJson,  new TypeToken<List<FilterDto>>(){}.getType());
            String fieldName = filterDto.stream().sorted().map(FilterDto::getFieldName).collect(Collectors.joining(":"));
            String fieldValue = filterDto.stream().sorted().map(filter -> filter.getFieldValue().getAsString()).collect(Collectors.joining(":"));
            searchQueryDto.setFilter(new QueryFilterDto(fieldName, DigestUtils.sha1Hex(fieldValue)));
        }
        return new ResponseEntity<>(eventService.searchRecord(searchQueryDto), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<EventDto> update(@RequestBody UpdateDto updateDto) {
        return new ResponseEntity<>(eventService.updateRecord(updateDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity delete(@RequestBody DeleteDto deleteDto) {
        eventService.deleteRecord(deleteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthcheck() {
        return new ResponseEntity<>("Working", HttpStatus.OK);
    }

}

