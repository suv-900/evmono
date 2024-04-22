package com.project.evm.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.exceptions.HostExistsException;
import com.project.evm.exceptions.HostNotFoundException;
import com.project.evm.exceptions.InValidTokenException;
import com.project.evm.exceptions.UnauthorizedAccessException;
import com.project.evm.exceptions.UserExistsException;
import com.project.evm.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
        InValidTokenException.class,
        UnauthorizedAccessException.class,
        TokenExpiredException.class,
        JWTVerificationException.class,
        MissingRequestHeaderException.class})
    public String handleUnauthorizedAccessException(InValidTokenException e){
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
        UserNotFoundException.class,
        HostNotFoundException.class,
        EventNotFoundException.class})
    public String handleUserNotFoundException(UserNotFoundException e){
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        log.error(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({UserExistsException.class,HostExistsException.class})
    public String handleUserExistsException(UserExistsException e){
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class,
        NullPointerException.class,
        JWTCreationException.class})
    public void handleException(Exception e){
        log.error(e.getMessage());
    }

    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    // @ExceptionHandler(JWTCreationException.class)
    // public void handleJWTCreationException(JWTCreationException e){
    //     log.error(e.getMessage());
    // }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String,String> sendValidationErrors(MethodArgumentNotValidException e){
        
        Map<String,String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        log.warn(errors.toString());
        return errors;
    }
    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // @ExceptionHandler(UnauthorizedAccessException.class)
    // public String handleUnauthorizedUserException(UnauthorizedAccessException e){
    //     log.error(e.getMessage());
    //     return e.getMessage();
    // }
    
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(JWTVerificationException.class)
    // public String handleJWTVerificationException(JWTVerificationException e){
    //     log.error(e.getMessage());
    //     return "Token malformed";
    // }


    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // @ExceptionHandler(TokenExpiredException.class)
    // public String handleTokenExpiredException(TokenExpiredException e){
    //     log.error(e.getMessage());
    //     return "Token expired";
    // }

    // @ResponseStatus(HttpStatus.UNAUTHORIZED)
    // @ExceptionHandler(MissingRequestHeaderException.class)
    // public String handleMissingHeaders(MissingRequestHeaderException e){
    //     return "Missing header: "+e.getHeaderName();
    // }

 
}
