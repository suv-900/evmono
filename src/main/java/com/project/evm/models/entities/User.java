package com.project.evm.models.entities;

import java.io.Serializable;
import java.util.Set;

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
@Entity(name="users")
@Table(name="users")
public class User implements Serializable{
   
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
