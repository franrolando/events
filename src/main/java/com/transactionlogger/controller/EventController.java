package com.transactionlogger.controller;

import java.util.List;

import com.transactionlogger.dto.SearchQueryDto;
import com.transactionlogger.service.EventService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
