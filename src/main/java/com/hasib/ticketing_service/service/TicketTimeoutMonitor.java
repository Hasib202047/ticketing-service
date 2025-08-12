package com.hasib.ticketing_service.service;

import com.hasib.ticketing_service.enums.Status;
import com.hasib.ticketing_service.model.Ticket;
import com.hasib.ticketing_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TicketTimeoutMonitor {
//    private final RedisTicketScheduler redisScheduler;
    private final TicketRepository ticketRepository;
    private final DatabaseTicketScheduler databaseTicketScheduler;
    private final EmailService emailService;

//    @Scheduled(fixedRate = 30000) // every 30 seconds
//    public void checkExpiredTickets() {
//        Set<String> expiredIds = redisScheduler.popExpiredTickets(Priority.URGENT);
//        expiredIds.addAll(redisScheduler.popExpiredTickets(Priority.HIGH));
//        expiredIds.addAll(redisScheduler.popExpiredTickets(Priority.MEDIUM));
//        expiredIds.addAll(redisScheduler.popExpiredTickets(Priority.LOW));
//
//        if (expiredIds.isEmpty()) return;
//
//        expiredIds.forEach(idStr -> {
//            Long ticketId = Long.parseLong(idStr);
//            ticketRepository.findById(ticketId).ifPresent(ticket -> {
//                if (ticket.getStatus() == Status.PENDING || ticket.getStatus() == Status.IN_PROGRESS) {
//                    ticket.setStatus(Status.ESCALATED); // mark as missed
//                    ticket.setUpdatedAt(LocalDateTime.now());
//                    ticketRepository.save(ticket);
//                    notifyDepartmentHead(ticket);
//                }
//            });
//        });
//    }

    //@Scheduled(fixedRate = 60000)
    @Scheduled(cron = "0 * 10-16 * * SUN-THU")
    public void checkExpiredTicketsInDatabase() {
        List<Long> expiredTicketIds = databaseTicketScheduler.popExpiredTickets();
        if (expiredTicketIds.isEmpty()) return;

        List<Ticket> ticketList = ticketRepository.findAllById(expiredTicketIds);
        ticketList.forEach(ticket -> {
            if (ticket.getStatus() == Status.PENDING || ticket.getStatus() == Status.IN_PROGRESS) {
                ticket.setStatus(Status.ESCALATED); // mark as missed
                ticket.setUpdatedAt(LocalDateTime.now());
                notifyDepartmentHead(ticket);
            }
        });
        ticketRepository.saveAll(ticketList);
    }

    private void notifyDepartmentHead(Ticket ticket) {
        // TODO: Email/SMS/Push notification logic here
        emailService.sendEmail("hasibul.hoque129971@gmail.com","Alarming Mail","Dear sir,Please check your delulu!");
        System.out.println("🔔 Ticket " + ticket.getId() + " missed SLA and was escalated.");
    }
}
