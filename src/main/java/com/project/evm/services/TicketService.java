package com.project.evm.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.dao.TicketDao;
import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.exceptions.TicketExistsException;
import com.project.evm.exceptions.TicketNotFoundException;
import com.project.evm.exceptions.UserNotFoundException;
import com.project.evm.models.entities.Event;
import com.project.evm.models.entities.Ticket;
import com.project.evm.models.entities.User;

@Service
public class TicketService {
     @Autowired
     private TicketDao ticketDao;
    
     @Autowired
     private UserService userService;

     @Autowired
     private EventService eventService;

     public Ticket buyTicket(Long userID,Long eventID)throws 
     UserNotFoundException,EventNotFoundException,TicketExistsException,Exception{

        if(ticketDao.ticketExists(userID,eventID)){
          throw new TicketExistsException();
        } 

        User user = userService.getUserByID(userID);
        Event event = eventService.retrieveEventById(eventID);

        Ticket ticket = new Ticket();
        ticket.setForEvent(event);
        ticket.setUnderName(user);
        ticket.setUserID(user.getId());
        ticket.setEventID(event.getId());

        Long ticketID = ticketDao.saveTicket(ticket);

        return ticketDao.getTicket(ticketID);
     }

     public void removeTicket(Ticket ticket)throws TicketNotFoundException{
         ticketDao.removeTicket(ticket); 
     }

     public List<Ticket> getTicketLists(List<Long> ticketIds){
          return ticketDao.getTicketsList(ticketIds);
     }
}
