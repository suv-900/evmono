package com.project.evm.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.exceptions.CredentialsDontMatchException;
import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.exceptions.TicketExistsException;
import com.project.evm.exceptions.UserNotFoundException;
import com.project.evm.models.dto.UserDTO;
import com.project.evm.models.dto.UserLogin;
import com.project.evm.models.entities.Event;
import com.project.evm.models.entities.Ticket;
import com.project.evm.models.entities.User;
import com.project.evm.models.repository.EventRepository;
import com.project.evm.models.repository.TicketRepository;
import com.project.evm.models.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PasswordHasher hasher;

    public Long addUser(User user)throws Exception{
        String hashedPassword = hasher.hashPassword(user.getPassword());
        
        user.setPassword(hashedPassword);
        
        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }
    
    public Long loginUser(String username,String password)throws 
        CredentialsDontMatchException,Exception,UserNotFoundException
    {
        
        User user = userRepository.getPasswordByUsernameWithID(username);
        
        if(user == null){
            throw new UserNotFoundException("User doesnt exists try registering first.");
        }

        hasher.comparePassword(user.getPassword(),password);

        return user.getId();
    }
    
    public UserDTO getUserById(Long userID)throws UserNotFoundException,Exception{
        Optional<User> userEntity = userRepository.getUserById(userID);
        
        if(userEntity.isEmpty()){
            throw new UserNotFoundException();
        }

        User user = userEntity.get(); 
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setDescription(user.getDescription());

        return userDTO;
    }

    //needs to be protected 
    public User getUserFullDetails(Long userID)throws Exception{
        return userRepository.getReferenceById(userID);
    }

    public void updateUser(User user)throws Exception{
        userRepository.save(user);
    }

    public void deleteUser(User user)throws Exception{
       userRepository.delete(user); 
    }

    // public void deleteUserByUsername(String username)throws Exception{
    //     userRepository.deleteUserByUsername(username);
    // }

    public boolean existsByUsername(String username)throws Exception{
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email)throws Exception{
        return userRepository.existsByEmail(email);
    }

    public long getCount()throws Exception{
        return userRepository.getCount();
    }
    public boolean exists(User user)throws Exception{
        return userRepository.userExists(user.getName(),user.getEmail());
    }

    //TODO:idList must not contain null
    //for retriving users associated with a event
    public List<UserDTO> findAllById(Iterable<Long> idList)throws Exception{
        List<UserDTO> list = new LinkedList<>();

        List<User> entityList = userRepository.findAllById(idList);

        entityList.forEach((userEntity)->{
            UserDTO newDTO = new UserDTO();
            newDTO.setName(userEntity.getName());
            newDTO.setDescription(userEntity.getDescription());
            newDTO.setEmail(userEntity.getEmail());
            list.add(newDTO);
        });

        entityList.clear();

        return list;

    }
    
    public User findUserById(Long userId)throws UserNotFoundException,Exception{
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new UserNotFoundException();
        }

        return user.get();
    }

    public Ticket buyTicket(Long userID,Long eventID)throws UserNotFoundException,EventNotFoundException,TicketExistsException,Exception{

        if(ticketService.ticketExists(userID,eventID)){
            throw new TicketExistsException();
        } 

        User user = findUserById(userID);
        Event event = eventService.getEventById(eventID);

        Ticket ticket = new Ticket();
        ticket.setForEvent(event);
        ticket.setUnderName(user);
        ticket.setUserID(user.getId());
        ticket.setEventID(event.getId());

        return ticketService.addTicket(ticket);
    }
}
