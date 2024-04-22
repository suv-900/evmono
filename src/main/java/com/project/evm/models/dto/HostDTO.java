package com.project.evm.models.dto;

import java.util.Set;

import com.project.evm.models.entities.Event;

import lombok.Data;

@Data
public class HostDTO {
    private String username;
    private String email;
    private Set<Event> eventsCreated;
}
