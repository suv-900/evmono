package com.project.evm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.project.evm.exceptions.CredentialsDontMatchException;
import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.exceptions.TicketExistsException;
import com.project.evm.exceptions.UserExistsException;
import com.project.evm.exceptions.UserNotFoundException;
import com.project.evm.models.dto.UserDTO;
import com.project.evm.models.dto.UserLogin;
import com.project.evm.models.entities.Ticket;
import com.project.evm.models.entities.User;
import com.project.evm.services.TokenService;
import com.project.evm.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/create")
    public void createUser(@Valid @RequestBody User user,HttpServletResponse response)
        throws UserExistsException,JWTCreationException,Exception{
        
        if(userService.exists(user)){
            throw new UserExistsException("User already exists cannot create another.");
        }

        Long userID = userService.addUser(user);

        String token = tokenService.generateTokenWithID(userID,"user");
        response.addHeader("Token",token);

    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void loginUser(@Valid @RequestBody UserLogin user,HttpServletResponse response)
    throws Exception,CredentialsDontMatchException,UserNotFoundException,Exception{
        
        Long userID = userService.loginUser(user.getUsername(),user.getPassword());

        String token = tokenService.generateTokenWithID(userID,"user");
        response.addHeader("Token",token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/update")
    public void updateUser(@RequestHeader(value = "Token",required = true) String token,
            @RequestBody User userUpdates)throws Exception,TokenExpiredException,JWTVerificationException{
        
        String username = tokenService.extractUsername(token);

        userUpdates.setName(username);
            
        userService.updateUser(userUpdates);
    }
 
    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/getUser/{id}")
    public UserDTO getUser(@PathVariable("id")Long id)throws Exception,UserNotFoundException{
        return userService.getUserById(id);
    }

    @ResponseStatus(HttpStatus.FOUND)
    @PostMapping("/verifyTicket")
    public void verifyTicket(@RequestBody @NotBlank String ticket){

    }
    
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/registerForEvent/{eventID}")
    public Ticket registerForEvent(@RequestHeader(value="Token",required=true)String token,@PathVariable("eventID")@NotBlank Long eventID)
    throws TicketExistsException,UserNotFoundException,EventNotFoundException,Exception
    {
        Long userID = tokenService.extractUserID(token);

        return userService.buyTicket(userID,eventID);
    }
    // @ResponseStatus(HttpStatus.OK)
    // @DeleteMapping("/delete")
    // public void deleteUser(@RequestHeader(value = "Token",required = true)String token)
    //     throws TokenExpiredException,JWTVerificationException,Exception{

    //     String username = tokenService.extractUsername(token);
        
    //     userService.deleteUserByUsername(username);
    // }


}
