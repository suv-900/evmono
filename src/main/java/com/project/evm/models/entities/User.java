package com.project.evm.models.entities;

import java.util.Set;

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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
@Cacheable
@Cache(region="users",usage=CacheConcurrencyStrategy.READ_ONLY)
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    private long id;

    @NotBlank(message="user name cannot be blank.")
    @Column(nullable=false,unique=true)
    private String name;

    @NotBlank(message="email cannot be blank.")
    @Email(message = "email is not valid.")
    @Column(nullable = false,unique = true)
    private String email;

    @NotBlank(message="user password cannot be blank.")
    @Column(nullable=false)
    private String password;

    private String description;

    private boolean online;

    @OneToMany
    private Set<Ticket> tickets;

}
