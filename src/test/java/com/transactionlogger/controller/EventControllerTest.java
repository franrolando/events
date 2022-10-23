package com.transactionlogger.controller;

import com.transactionlogger.dto.EventDto;
import com.transactionlogger.dto.SearchQueryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

public class EventControllerTest {

	private EventController eventController;
	
	@Test
	public void testCreateRecord() {
		/*EventDto eventDto = EventDto.builder().build();
		eventController.createEvent(eventDto);*/
	}

	@Test
	public void testSearchRecords() {
		/*SearchQueryDto searchQueryDto = SearchQueryDto.builder().build();
		eventController.searchEvents(searchQueryDto);*/
	}
	
}
