package com.project.evm.exceptions;

public class EventExistsException extends RuntimeException {
   public EventExistsException(){
    super();
   }
   public EventExistsException(String args){
    super(args);
   } 
}
