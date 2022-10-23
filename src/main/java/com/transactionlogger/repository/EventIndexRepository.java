package com.transactionlogger.repository;

import com.transactionlogger.entity.EventIndex;
import com.transactionlogger.entity.EventIndexId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventIndexRepository extends JpaRepository<EventIndex, EventIndexId> {
}
