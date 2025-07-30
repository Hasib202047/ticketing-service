package com.hasib.ticketing_service.service;

import com.hasib.ticketing_service.enums.Priority;
import com.hasib.ticketing_service.enums.Status;
import com.hasib.ticketing_service.model.Ticket;
import com.hasib.ticketing_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class TicketTimeoutMonitor {
    private final RedisTicketScheduler redisScheduler;
    private final TicketRepository ticketRepository;

    @Scheduled(fixedRate = 30000) // every 30 seconds
    public void checkExpiredTickets() {
        Set<String> expiredIds = redisScheduler.popExpiredTickets(Priority.URGENT);
        expiredIds.addAll(redisScheduler.popExpiredTickets(Priority.HIGH));
        expiredIds.addAll(redisScheduler.popExpiredTickets(Priority.MEDIUM));
        expiredIds.addAll(redisScheduler.popExpiredTickets(Priority.LOW));

        if (expiredIds.isEmpty()) return;

        expiredIds.forEach(idStr -> {
            Long ticketId = Long.parseLong(idStr);
            ticketRepository.findById(ticketId).ifPresent(ticket -> {
                if (ticket.getStatus() == Status.PENDING || ticket.getStatus() == Status.IN_PROGRESS) {
                    ticket.setStatus(Status.ESCALATED); // mark as missed
                    ticket.setUpdatedAt(LocalDateTime.now());
                    ticketRepository.save(ticket);
                    notifyDepartmentHead(ticket);
                }
            });
        });
    }

    private void notifyDepartmentHead(Ticket ticket) {
        // TODO: Email/SMS/Push notification logic here
        System.out.println("🔔 Ticket " + ticket.getId() + " missed SLA and was escalated.");
    }
}
