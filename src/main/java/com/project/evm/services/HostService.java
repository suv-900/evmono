package com.project.evm.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.exceptions.CredentialsDontMatchException;
import com.project.evm.exceptions.HostNotFoundException;
import com.project.evm.models.dto.HostDTO;
import com.project.evm.models.entities.Event;
import com.project.evm.models.entities.Host;
import com.project.evm.models.repository.EventRepository;
import com.project.evm.models.repository.HostRepository;

import lombok.NonNull;

@Service
public class HostService {
   
    @Autowired
    private HostRepository hostRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PasswordHasher hasher;

    public Long addHost(Host host)throws Exception{
        String hashedPassword = hasher.hashPassword(host.getPassword());
        
        host.setPassword(hashedPassword);

        Host savedHost = hostRepository.save(host);

        return savedHost.getId();
    }

    public Long loginHost(String username,String password)throws 
    CredentialsDontMatchException,
    HostNotFoundException,Exception{
        Host host= hostRepository.getPasswordByUsernameWithID(username);

        if(host == null){
            throw new HostNotFoundException("Host not registered.");
        }

        hasher.comparePassword(host.getPassword(),password);

        return host.getId();
    }

    public HostDTO getHostById(Long hostID)throws Exception{
        return hostRepository.getHostById(hostID);
    }

    public Optional<Host> getHostByIdFull(@NonNull Long hostID)throws Exception{
        return hostRepository.findById(hostID);
    }
    public void deleteHost(@NonNull Host host)throws Exception{
        hostRepository.delete(host);
    }

    public void createEvent(@NonNull Event event)throws Exception{
        eventRepository.save(event);
    }

    public Event updateEvent(@NonNull Event event)throws Exception{
        return eventRepository.save(event);
    }

    public void deleteEvent(@NonNull Event event)throws Exception{
        eventRepository.delete(event);
    }

    public void deleteEventById(@NonNull Long eventID)throws Exception{
        eventRepository.deleteById(eventID);
    }

    public boolean hostExists(String username,String email)throws Exception{
        return hostRepository.hostExists(username,email);
    }

    public void increaseTotalTickets(Long eventID,Long amount)throws Exception{
        eventRepository.increaseTotalTickets(eventID,amount);
    }

    public Optional<Event> getEvent(@NonNull Long eventID)throws Exception{
        return eventRepository.findById(eventID);
    } 
}
