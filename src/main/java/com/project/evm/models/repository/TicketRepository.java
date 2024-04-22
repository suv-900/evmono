package com.project.evm.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.evm.models.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long>{

    @Query(value="SELECT EXISTS(SELECT 1 FROM tickets WHERE userID = :userID AND eventID = :eventID) AS ticket_exists",nativeQuery=true)
    public boolean ticketExists(@Param("userID")Long userID,@Param("eventID")Long eventID);
}
