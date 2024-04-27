package com.project.evm.models.entities;

import java.sql.Date;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Cacheable;
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
@Table
@Cacheable
@Cache(region="tickets",usage=CacheConcurrencyStrategy.READ_ONLY)
public class Ticket {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;
    
    private Long userID;

    private Long eventID;
    
    @OneToOne
    private Event forEvent;

    @OneToOne
    private User underName;

    private Date issuedAt;

}
