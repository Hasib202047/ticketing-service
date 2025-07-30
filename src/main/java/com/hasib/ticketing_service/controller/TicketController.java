package com.hasib.ticketing_service.controller;

import com.hasib.ticketing_service.dto.TicketDto;
import com.hasib.ticketing_service.enums.Status;
import com.hasib.ticketing_service.model.Ticket;
import com.hasib.ticketing_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TicketDto ticketDto) {
        return ticketService.save(ticketDto);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestParam Status status) {
        return ticketService.updateStatus(id, status);
    }
}
