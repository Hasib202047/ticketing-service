package com.hasib.ticketing_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "expiryTime")
})
public class TicketExpiry {
    @Id
    private Long ticketId;
    private LocalDateTime expiryTime;
}
