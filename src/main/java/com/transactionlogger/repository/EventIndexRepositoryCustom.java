package com.transactionlogger.repository;

import com.transactionlogger.entity.EventIndex;

import java.util.List;

public interface EventIndexRepositoryCustom {
    List<EventIndex> findEventIndexByPredicates();
}
