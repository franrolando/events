package com.transactionlogger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.*;
import com.transactionlogger.dto.*;
import com.transactionlogger.entity.EventIndex;
import com.transactionlogger.entity.EventIndexId;
import com.transactionlogger.repository.EventIndexRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.Opt;
import org.hibernate.sql.Delete;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Slf4j
public class EventIndexService {

    private EventIndexRepository eventIndexRepository;
    private ModelMapper mapper;

    public EventIndexService(EventIndexRepository eventIndexRepository) {
        this.eventIndexRepository = eventIndexRepository;
        mapper = new ModelMapper();
    }

    public void saveIndexes(EventDto eventDto, String eventId) {
        JsonObject dataJson = JsonParser.parseString(eventDto.getData().toString()).getAsJsonObject();
        List<IndexRecordData> indexRecordsData = createEventIndexRecordData(dataJson, eventDto.getSchema().getFilterIndexes());
        indexRecordsData.forEach(indexRecordData -> {
            indexRecordData.getHashedValues().forEach(hashedValue -> {
                eventIndexRepository.save(new EventIndex(eventDto.getApp(), eventDto.getType(), eventDto.getAction(), eventId, indexRecordData.getFieldName(), hashedValue));
            });
        });
    }

    private List<IndexRecordData> createEventIndexRecordData(JsonObject data, List<IndexSchemaField> indexSchemaFields) {
        List<IndexRecordData> indexRecordsData = new ArrayList<>();
        Map<String, List<JsonElement>> indexValuesByName = createIndexValuesByName(data, indexSchemaFields);
        Set<String> indexNames = indexValuesByName.keySet();
        List<String> indexKeys = createIndexKeys(new ArrayList<>(indexNames.stream().sorted().collect(Collectors.toList())));
        indexKeys.forEach(indexKey -> {
            List<List<JsonElement>> listOfValues = List.of(indexKey.split(":")).stream().map(indexKey2 -> indexValuesByName.get(indexKey2)).collect(Collectors.toList());
            List<List<JsonElement>> cartesianProduct = cartesianProduct(0, listOfValues);
            List<String> values = new ArrayList<>();
            cartesianProduct.forEach(product -> {
                values.add(product.stream().sorted(Comparator.comparing(JsonElement::getAsString)).map(JsonElement::getAsString).collect(Collectors.joining(":")));
                indexRecordsData.add(new IndexRecordData(indexKey, hashArray(values)));
            });
        });
        return indexRecordsData;
    }

    public List<String> hashArray(List<String> arrayUnhashed) {
        List<String> hashedArray = new ArrayList<>();
        arrayUnhashed.forEach(unhashed -> {
            hashedArray.add(createHash(unhashed));
        });
        return hashedArray;
    }

    private String createHash(String value) {
        return DigestUtils.sha1Hex(value.getBytes());
    }

    public List<List<JsonElement>> cartesianProduct(int index, List<List<JsonElement>> values) {
        return Lists.cartesianProduct(values);
    }

    private List<String> createIndexKeys(List<String> indexNames) {
        List<String> indexKeys = new ArrayList<>();
        int i = 1;
        for (String indexName : indexNames) {
            createsTails(indexName, new ArrayList<>(indexNames.subList(i++, indexNames.size())), indexKeys);
        }
        return indexKeys;
    }

    private void createsTails(String parent, List<String> tail, List<String> indexKeys) {
        indexKeys.add(parent);
        int i = 1;
        for (String indexName : tail) {
            String nextParent = StringUtils.join(new String[]{parent, indexName}, ":");
            createsTails(nextParent, new ArrayList<>(tail.subList(i++, tail.size())), indexKeys);
        }
    }


    private Map<String, List<JsonElement>> createIndexValuesByName(JsonObject data, List<IndexSchemaField> indexSchemaFields) {
        Map<String, List<JsonElement>> indexes = new HashMap<>();
        indexSchemaFields.forEach(indexSchemaField -> {
            String name = indexSchemaField.getName();
            String fieldName = indexSchemaField.getField();
            JsonElement field = existField(indexSchemaField.getField(), data);
            if (field == null) {
                throw new RuntimeException(MessageFormat.format("Field {0} is not in the data", fieldName));
            }
            List<JsonElement> jsonArray = new ArrayList<>();
            if (field.isJsonPrimitive()) {
                jsonArray.add(field);
            } else {
                field.getAsJsonArray().forEach(element -> {
                    jsonArray.add(element);
                });
            }
            indexes.put(name, jsonArray);
        });
        return indexes;
    }

    private JsonElement existField(String field, JsonObject object) {
        JsonElement jsonField = JsonNull.INSTANCE;
        if (field.contains(".")) {
            List<String> path = new ArrayList<>(List.of(field.split(Pattern.quote("."))));
            String principal = path.get(0);
            path.remove(0);
            StringBuilder subPath = new StringBuilder(StringUtils.join(path, "."));
            if (object.has(principal)) {
                jsonField = existField(subPath.toString(), object.getAsJsonObject(principal));
            }
        } else {
            if (object.has(field)) {
                jsonField = object.get(field);
            } else {
                Iterator<String> it = object.keySet().iterator();
                while (it.hasNext() && jsonField.isJsonNull()) {
                    String attributeKey = it.next();
                    JsonElement attribute = object.get(attributeKey);
                    if (attribute.isJsonObject()) {
                        jsonField = existField(field, attribute.getAsJsonObject());
                    }
                }
            }
        }
        return jsonField;
    }

    public List<EventIndex> searchEventsIndexes(SearchQueryDto searchQueryDto) {
        Specification<EventIndex> specification = appEquals(searchQueryDto.getApp());
        if (!searchQueryDto.getType().isEmpty()){
            specification = specification.and(typeEquals(searchQueryDto.getType()));
        }
        if (!searchQueryDto.getAction().isEmpty()){
            specification = specification.and(actionEquals(searchQueryDto.getAction()));
        }
        if (!searchQueryDto.getFilter().getFieldName().isEmpty()){
            specification = specification.and(fieldName(searchQueryDto.getFilter().getFieldName()));
            specification = specification.and(fieldValue(searchQueryDto.getFilter().getFieldValue()));
        }
        if (searchQueryDto.getStartDate() != null){
            specification = specification.and(startDate(Instant.ofEpochMilli(searchQueryDto.getStartDate().getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()));
        }
        if (searchQueryDto.getEndDate() != null){
            specification = specification.and(endDate(Instant.ofEpochMilli(searchQueryDto.getEndDate().getTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()));
        }
        return eventIndexRepository.findAll(where(specification));
    }

    static Specification<EventIndex> appEquals(String app) {
        return (eventIndex, cq, cb) -> cb.equal(eventIndex.get("id").get("app"), app);
    }
    static Specification<EventIndex> fieldName(String fieldName) {
        return (eventIndex, cq, cb) -> cb.equal(eventIndex.get("id").get("fieldName"), fieldName);
    }
    static Specification<EventIndex> fieldValue(String fieldValue) {
        return (eventIndex, cq, cb) -> cb.equal(eventIndex.get("id").get("fieldValue"), fieldValue);
    }
    static Specification<EventIndex> typeEquals(String type) {
        return (eventIndex, cq, cb) -> cb.equal(eventIndex.get("id").get("type"), type);
    }
    static Specification<EventIndex> actionEquals(String action) {
        return (eventIndex, cq, cb) -> cb.equal(eventIndex.get("id").get("action"), action);
    }
    static Specification<EventIndex> startDate(LocalDateTime startDate) {
        return (eventIndex, cq, cb) -> cb.greaterThan(eventIndex.get("createdAt"), startDate);
    }
    static Specification<EventIndex> endDate(LocalDateTime endDate) {
        return (eventIndex, cq, cb) -> cb.lessThan(eventIndex.get("createdAt"), endDate);
    }

    public void deleteById(DeleteDto id) {
        EventIndexId eventIndexId = mapper.map(id, EventIndexId.class);
        this.eventIndexRepository.deleteById(eventIndexId);
    }

    public void updateIndex(UpdateDto updateDto) {
        EventIndexId eventIndexId = mapper.map(updateDto, EventIndexId.class);
        Optional<EventIndex> optEventIndexId = this.eventIndexRepository.findById(eventIndexId);
        if (optEventIndexId.isPresent()){

        } else {

        }
    }

    public boolean existsEvent(UpdateDto updateDto) {
        EventIndexId eventIndexId = mapper.map(updateDto, EventIndexId.class);
        return this.eventIndexRepository.existsById(eventIndexId);
    }
}
