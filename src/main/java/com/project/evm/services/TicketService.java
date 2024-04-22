package com.project.evm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.models.entities.Ticket;
import com.project.evm.models.repository.TicketRepository;

@Service
public class TicketService {
   @Autowired
   private TicketRepository ticketRepository;
   
   public boolean ticketExists(Long userID,Long eventID)throws Exception{
        return ticketRepository.ticketExists(userID,eventID);
   }

   public Ticket addTicket(Ticket ticket)throws Exception{
        return ticketRepository.save(ticket);
   }
}
