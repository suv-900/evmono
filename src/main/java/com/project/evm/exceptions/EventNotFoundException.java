package com.project.evm.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(){
        super();
    }
    
    public EventNotFoundException(String args){
        super(args);
    }
}
