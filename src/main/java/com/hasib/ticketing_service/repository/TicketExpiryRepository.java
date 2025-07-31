package com.hasib.ticketing_service.repository;

import com.hasib.ticketing_service.model.TicketExpiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketExpiryRepository extends JpaRepository<TicketExpiry,Long> {
    @Query(value = "select t.ticketId from TicketExpiry t where t.expiryTime < :expiryTime")
    List<Long>  getAllExpiredTicketIds(LocalDateTime expiryTime);
}
