package com.transactionlogger.repository;

import com.transactionlogger.entity.EventIndex;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventIndexRepositoryImpl implements EventIndexRepositoryCustom {
    @Override
    public List<EventIndex> findEventIndexByPredicates() {
        return null;
    }

}
