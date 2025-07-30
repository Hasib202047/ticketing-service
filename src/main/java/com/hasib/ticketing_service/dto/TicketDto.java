package com.hasib.ticketing_service.dto;

import com.hasib.ticketing_service.enums.Priority;
import com.hasib.ticketing_service.enums.Status;
import lombok.Data;

@Data
public class TicketDto {
    private String category;
    private String subCategory;
    private String handlingDepartment;
    private String handlingUser;
    private String description;
    private Priority priority;
    private Status status;
}
