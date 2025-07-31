package com.hasib.ticketing_service.service;

import com.hasib.ticketing_service.enums.Priority;
import com.hasib.ticketing_service.model.TicketExpiry;
import com.hasib.ticketing_service.repository.TicketExpiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DatabaseTicketScheduler {
    private final TicketExpiryRepository ticketExpiryRepository;

    private static final Set<DayOfWeek> WORKING_DAYS = EnumSet.of(
            DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY
    );

    private static final LocalTime OFFICE_START = LocalTime.of(10, 0,0);
    private static final LocalTime OFFICE_END = LocalTime.of(15, 5,0);

    private LocalDateTime getExpiryTime(Priority priority) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime proposed = switch (priority) {
            case URGENT -> now.plusMinutes(3);
            case HIGH -> now.plusMinutes(5);
            case MEDIUM -> now.plusMinutes(7);
            case LOW -> now.plusDays(2);
        };

        if (proposed.toLocalTime().isBefore(OFFICE_END)) {
            return proposed;
        }
        Duration diff = Duration.between(LocalDate.now().atTime(OFFICE_END),proposed).abs();

        System.out.println("diff: " + diff);
        // Find next working day
        LocalDate nextWorkingDay = now.toLocalDate().plusDays(1);
        while (!WORKING_DAYS.contains(nextWorkingDay.getDayOfWeek())) {
            nextWorkingDay = nextWorkingDay.plusDays(1);
        }
        return LocalDateTime.of(nextWorkingDay, OFFICE_START).plus(diff);
    }
    public void scheduleTicket(Long ticketId, Priority priority){
        LocalDateTime delayTime = getExpiryTime(priority);

        ticketExpiryRepository.save(new TicketExpiry(ticketId, delayTime));
    }

    public List<Long> popExpiredTickets()
    {
        List<Long> ids = ticketExpiryRepository.getAllExpiredTicketIds(LocalDateTime.now());
        ticketExpiryRepository.deleteAllById(ids);
        return ids;
    }
    public void updateTicket(Long ticketId,Priority priority)
    {
        Optional<TicketExpiry> ticketExpiry = ticketExpiryRepository.findById(ticketId);
        if(ticketExpiry.isPresent()) {
            ticketExpiry.get().setExpiryTime(getExpiryTime(priority));
            ticketExpiryRepository.save(ticketExpiry.get());
        }else {
            ticketExpiryRepository.save(new TicketExpiry(ticketId, getExpiryTime(priority)));
        }
    }
}
