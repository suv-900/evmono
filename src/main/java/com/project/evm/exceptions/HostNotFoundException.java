package com.project.evm.exceptions;

public class HostNotFoundException extends RuntimeException{
   public HostNotFoundException(){
    super();
   }
   
   public HostNotFoundException(String args){
    super(args);
   }
}
