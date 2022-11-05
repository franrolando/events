package com.transactionlogger.repository;

import com.transactionlogger.entity.Event;
import com.transactionlogger.entity.EventIndex;
import com.transactionlogger.entity.EventIndexId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EventIndexRepository extends JpaRepository<EventIndex, EventIndexId>, JpaSpecificationExecutor<EventIndex> {
}
