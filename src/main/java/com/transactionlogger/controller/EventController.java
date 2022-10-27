package com.transactionlogger.controller;

import java.util.List;

import com.transactionlogger.dto.SearchQueryDto;
import com.transactionlogger.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.transactionlogger.dto.EventDto;

@RestController
@RequestMapping
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/createEvent")
    public void createEvent(@RequestBody EventDto eventDto) {
        eventService.createRecord(eventDto);
    }

    @PostMapping("/search")
    public List<EventDto> searchEvents(SearchQueryDto searchQueryDto) {
        return eventService.searchRecord(searchQueryDto);
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthcheck(){
        return new ResponseEntity<>("Working", HttpStatus.ACCEPTED);
    }

}
