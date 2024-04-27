package com.project.evm.models.entities;

import java.util.List;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
@Cacheable
@Cache(region="events",usage=CacheConcurrencyStrategy.READ_ONLY)
public class Host {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,unique=true)
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Column(nullable=false)
    @NotBlank(message = "First Name cannot be blank")
    private String firstName;

    @Column(nullable=false)
    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;

    @Column(nullable=false,unique=true)
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Column(nullable=false)
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @OneToMany(mappedBy="hostedBy")
    private List<Event> eventsCreated;

}
