package com.project.evm.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.project.evm.exceptions.CredentialsDontMatchException;
import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.exceptions.HostExistsException;
import com.project.evm.exceptions.HostNotFoundException;
import com.project.evm.exceptions.UnauthorizedAccessException;
import com.project.evm.models.dto.HostLogin;
import com.project.evm.models.entities.Event;
import com.project.evm.models.entities.Host;
import com.project.evm.services.HostService;
import com.project.evm.services.TokenService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

@RestController
@RequestMapping("/host")
public class HostController {
    @Autowired
    private HostService hostService; 

    @Autowired
    private TokenService tokenService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/register")
    public void createHost(@Valid @RequestBody Host host,HttpServletResponse response)throws HostExistsException,Exception{

        if(hostService.hostExists(host.getUsername(),host.getEmail())){
            throw new HostExistsException();
        }

        Long hostID = hostService.addHost(host);

        String token = tokenService.generateTokenWithID(hostID,"host");
        response.addHeader("Token",token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void loginHost(@Valid @RequestBody HostLogin host,HttpServletResponse response)throws CredentialsDontMatchException,HostNotFoundException,Exception{
        
        Long hostID = hostService.loginHost(host.getUsername(),host.getPassword());

        String token = tokenService.generateTokenWithID(hostID,"host");
        response.addHeader("Token",token);
    }

    @PostMapping("/create-event")
    @ResponseStatus(HttpStatus.OK)
    public void createEvent(@Valid @RequestBody Event event,@RequestHeader(value="Token",required=true)String token)
    throws UnauthorizedAccessException,TokenExpiredException,JWTVerificationException,Exception,HostNotFoundException
    {
        
        Long hostID = tokenService.extractHostID(token);

        Optional<Host> host = hostService.getHostByIdFull(hostID);

        if(host.isEmpty()){
            throw new HostNotFoundException("Unknown state host not found.Try registering.");
        }

        event.setHostedBy(host.get());
        hostService.createEvent(event);
    }

    @PostMapping("/update-event")
    @ResponseStatus(HttpStatus.OK)
    public void updateEvent(@Valid @RequestBody Event event,@RequestHeader(value="Token",required=true)String token)
    throws UnauthorizedAccessException,TokenExpiredException,JWTVerificationException,Exception
    {
        
        tokenService.verifyHostToken(token);

        hostService.updateEvent(event);
    }

    @GetMapping("/get-event/{eventID}")
    @ResponseStatus(HttpStatus.FOUND)
    public Event getEvent(@RequestHeader(value="Token",required=true)String token,@PathVariable("eventID")Long eventID)
    throws UnauthorizedAccessException,TokenExpiredException,JWTVerificationException,Exception
    {
        tokenService.verifyHostToken(token);

        Optional<Event> event = hostService.getEvent(eventID);

        if(event.isEmpty()){
            throw new EventNotFoundException();
        }

        return event.get();
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete-event/{eventID}")
    public void deleteEvent(@RequestHeader(value="Token",required=true)String token,@PathVariable("eventID")Long eventID)
    throws UnauthorizedAccessException,TokenExpiredException,JWTVerificationException,Exception
    {
        tokenService.verifyHostToken(token);

        hostService.deleteEventById(eventID);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/increaseTicketCount/{eventID}/{amount}")
    public void updateEventTicketCount(@PathVariable("eventID")@NotNull(message="eventID cannot be null") Long eventID,
        @PathVariable("amount") @NotNull(message="increase amount cannot be null")Long amount,
        @RequestHeader(value="Token",required=true)String token)
    throws UnauthorizedAccessException,TokenExpiredException,JWTVerificationException,NullPointerException,Exception
    {
        tokenService.verifyToken(token);
        hostService.increaseTicketCount(eventID,amount);
    }

}
