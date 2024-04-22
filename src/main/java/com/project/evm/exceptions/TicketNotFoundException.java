package com.project.evm.exceptions;

public class TicketNotFoundException extends RuntimeException {
   public TicketNotFoundException(){
    super();
   } 
   public TicketNotFoundException(String args){
    super(args);
   }
}
