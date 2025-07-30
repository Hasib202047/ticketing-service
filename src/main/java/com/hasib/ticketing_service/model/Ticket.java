package com.hasib.ticketing_service.model;

import com.hasib.ticketing_service.enums.Priority;
import com.hasib.ticketing_service.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String subCategory;
    private String handlingDepartment;
    private String handlingUser;
    private String description;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Version
    private int version;
}
