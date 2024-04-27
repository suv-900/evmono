package com.project.evm.exceptions;

public class HostExistsException extends RuntimeException {
   public HostExistsException(){
     super();
   }

   public HostExistsException(String args){
        super(args);
   }
}
