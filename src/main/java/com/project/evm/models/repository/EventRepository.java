package com.project.evm.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.evm.models.entities.Event;

public interface EventRepository extends JpaRepository<Event,Long>{
    @Query(value="UPDATE Event SET totalTickets = totalTickets + :amount WHERE id = :eventID",nativeQuery=true)
    public void increaseTicketCount(@Param("eventID")Long eventID,@Param("amount")Long amount);

}
