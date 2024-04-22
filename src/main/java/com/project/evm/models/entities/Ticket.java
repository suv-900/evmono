package com.project.evm.models.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long ticketId;
    
    @OneToOne
    @Column(name="forEvent",nullable=false)
    private Event forEvent;

    @OneToOne
    @Column(name="underName",nullable=false)
    private User underName;

    @Column(name="issuedAt",nullable=false)
    private Date issuedAt;

}
