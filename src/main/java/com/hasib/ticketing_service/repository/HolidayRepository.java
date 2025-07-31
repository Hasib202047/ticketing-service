package com.hasib.ticketing_service.repository;

import com.hasib.ticketing_service.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, String> {

    @Query(value = "select count(h)>0 from Holiday h where :date between h.startDate and h.endDate")
    boolean isHoliday(@Param("date") LocalDate date);
}
