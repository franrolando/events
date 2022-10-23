package com.transactionlogger.repository;

import com.transactionlogger.dto.EventDto;
import com.transactionlogger.dto.SearchQueryDto;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepositoryTest {

    private EventRepository eventRepository;

    @Test
    public void testCreateRecord(){
        EventDto record = EventDto.builder().build();
    }

    @Test
    public void testSearchRecords(){
        SearchQueryDto searchQueryDto = SearchQueryDto.builder().build();
    }

}
