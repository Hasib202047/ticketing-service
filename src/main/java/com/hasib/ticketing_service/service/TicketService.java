package com.hasib.ticketing_service.service;

import com.hasib.ticketing_service.dto.TicketDto;
import com.hasib.ticketing_service.enums.Status;
import com.hasib.ticketing_service.model.Ticket;
import com.hasib.ticketing_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;
//    private final RedisTicketScheduler redisScheduler;
    private final DatabaseTicketScheduler databaseTicketScheduler;

    public ResponseEntity<?> save(TicketDto ticketDto) {
        Ticket ticket = modelMapper.map(ticketDto, Ticket.class);
        ticket.setCreatedAt(LocalDateTime.now());
        ticket = ticketRepository.save(ticket);

        if(ticketDto.getStatus() == Status.PENDING) {
//            redisScheduler.scheduleTicket(ticket.getId(),ticket.getPriority());
            databaseTicketScheduler.scheduleTicket(ticket.getId(),ticket.getPriority());
        }
        return new ResponseEntity<>(ticket, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateStatus(Long id, Status status) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        assert ticket != null;
        ticket.setStatus(status);
        ticket.setUpdatedAt(LocalDateTime.now());
        ticket = ticketRepository.save(ticket);
        if(status == Status.PENDING) {
            /*redisScheduler.removeTicket(ticket.getId(),ticket.getPriority());
            redisScheduler.scheduleTicket(ticket.getId(),ticket.getPriority());*/
            databaseTicketScheduler.updateTicket(ticket.getId(),ticket.getPriority());
        }
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    public ResponseEntity<?> getAll()
    {
        return new ResponseEntity<>(ticketRepository.findAll(Sort.by(Sort.Direction.ASC,"id")), HttpStatus.OK);
    }

}
