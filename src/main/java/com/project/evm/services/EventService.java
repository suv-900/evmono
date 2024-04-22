package com.project.evm.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.models.entities.Event;
import com.project.evm.models.repository.EventRepository;

@Service
public class EventService {
   @Autowired
   private EventRepository eventRepository; 

    public Event getEventById(Long eventID)throws EventNotFoundException,Exception{
        Optional<Event> event = eventRepository.findById(eventID);

        if(event.isEmpty()){
            throw new EventNotFoundException();
        }

        return event.get();
    } 
}
