package com.transactionlogger.integration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.transactionlogger.controller.EventController;
import com.transactionlogger.dto.EventDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SpringBootTest
public class EventsIntegrationTest {

    @Autowired
    private EventController eventController;

    @Test
    public void testCreateRecord() throws IOException {

        Resource resource = new ClassPathResource("event.json");
        BufferedReader br = new BufferedReader(new FileReader(resource.getFile().getAbsolutePath()));
        JsonObject object = JsonParser.parseReader(br).getAsJsonObject();
        EventDto eventDto = new Gson().fromJson(object, EventDto.class);
        eventController.createEvent(eventDto);
    }

}
