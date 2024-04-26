package com.project.evm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.dao.EventDao;
import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.models.entities.Event;

@Service
public class EventService {
    
    @Autowired
    private EventDao eventDao;

    public Long saveEvent(Event event)throws Exception{
        return eventDao.saveEvent(event);
    }

    public Event retrieveEventById(Long eventID)throws Exception,EventNotFoundException{
        Event event = eventDao.retrieveEventById(eventID);
        if(event == null){
            throw new EventNotFoundException();
        }
        return event;
    }

    public Event updateEvent(Event event)throws Exception{
        return eventDao.updateEvent(event);
    }

    public void removeEvent(Event event)throws Exception{
        eventDao.removeEvent(event);
    }

    public void removeEvent(Long eventID)throws Exception{
        Event event = eventDao.retrieveEventById(eventID);
        if(event == null){
            throw new EventNotFoundException();
        }
        eventDao.removeEvent(event);
    }

    public List<Event> getEventsList(List<Long> eventIDs)throws Exception{
        return eventDao.getEventsList(eventIDs);
    }
}
