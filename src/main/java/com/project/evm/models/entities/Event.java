package com.project.evm.models.entities;

import java.sql.Date;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
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

    private Host hostedBy;
}
