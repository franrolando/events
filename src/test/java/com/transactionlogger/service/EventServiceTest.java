package com.transactionlogger.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.transactionlogger.dto.EventDto;
import com.transactionlogger.dto.SearchQueryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.Type;

@WebMvcTest
public class EventServiceTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {
        @Bean
        public EventService employeeService() {
            return new EventService(null, null) {
            };
        }
    }

    @Autowired
    private EventService eventService;

    @Test
    public void testCreateRecord() throws IOException {
        Resource resource = new ClassPathResource("event.json");
        BufferedReader br = new BufferedReader(new FileReader(resource.getFile().getAbsolutePath()));
        JsonObject object = JsonParser.parseReader(br).getAsJsonObject();
        EventDto eventDto = new Gson().fromJson(object, EventDto.class);
        eventService.createRecord(eventDto);
    }

    @Test
    public void testSearchRecords(){
      /*  SearchQueryDto searchQueryDto = SearchQueryDto.builder().build();
        eventService.searchRecord(searchQueryDto);*/
    }

}
