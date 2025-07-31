package com.hasib.ticketing_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Holiday {
    @Id
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
