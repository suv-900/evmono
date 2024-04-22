package com.project.evm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.models.entities.Event;
import com.project.evm.services.EventService;

import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/event")
public class EventController {
   @Autowired
   private EventService eventService;
    
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/get-event/{eventID}")
    public Event getEvent(@PathVariable("eventID")@NotBlank Long eventID)throws EventNotFoundException,Exception{
        return eventService.getEventById(eventID);
    }
}
