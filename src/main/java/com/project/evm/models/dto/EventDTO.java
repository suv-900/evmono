package com.project.evm.models.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class EventDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private Date date;
    private Long totalTickets; 
}
