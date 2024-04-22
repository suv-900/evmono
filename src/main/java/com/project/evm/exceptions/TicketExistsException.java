package com.project.evm.exceptions;

public class TicketExistsException extends RuntimeException{
    public TicketExistsException(){
        super();
    }   
    public TicketExistsException(String args){
        super(args);
    } 
}
