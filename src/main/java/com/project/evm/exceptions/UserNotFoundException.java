package com.project.evm.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String args){
        super(args);
    }

    public UserNotFoundException(){
        super("User not found.");
    }
}