package com.project.evm.models.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.evm.models.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long>{
}
