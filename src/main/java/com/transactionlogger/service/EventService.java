package com.transactionlogger.service;

import com.transactionlogger.dto.EventDto;
import com.transactionlogger.dto.SearchQueryDto;
import com.transactionlogger.entity.EventIndex;
import com.transactionlogger.repository.EventIndexRepository;
import com.transactionlogger.repository.EventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventService {

    @Bean
    private EventRepository eventRepository;
    private EventIndexRepository eventIndexRepository;

    public EventService(EventRepository eventRepository, EventIndexRepository eventIndexRepository) {
        this.eventRepository = eventRepository;
        this.eventIndexRepository = eventIndexRepository;
    }

    @Transactional
    public void createRecord(EventDto eventDto) {
        eventIndexRepository.save(createEventIndex(eventDto));
        //eventRepository.save(eventDto);
    }

    public List<EventDto> searchRecord(SearchQueryDto searchQueryDto) {
        return null;
    }

    private EventIndex createEventIndex(EventDto event) {
        EventIndex eventIndex = new EventIndex();
        return eventIndex;
    }
}
