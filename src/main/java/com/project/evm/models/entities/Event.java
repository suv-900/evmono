package com.project.evm.models.entities;

import java.sql.Date;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
@Cacheable
@Cache(region="events",usage=CacheConcurrencyStrategy.READ_ONLY)
public class Event{
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private Long id;

    @NotBlank(message="Event title cannot be blank.")
    private String title;

    @NotBlank(message="Event description cannot be blank.")
    private String description;

    @NotNull(message="Event Date cannot be blank.")
    private Date date;

    @NotBlank(message="Location cannot be blank.")
    private String location;

    @NotNull(message="number of tickets cannot be null")
    private Long totalTickets;

    private Long ticketsSold;
    
    private Long ticketsAvailable;

    @OneToMany(mappedBy="forEvent")
    private Set<Ticket> ticketsSoldID;

    @OneToOne
    private Host hostedBy;
}
