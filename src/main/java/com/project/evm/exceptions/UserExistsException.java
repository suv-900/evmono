package com.project.evm.exceptions;

public class UserExistsException extends RuntimeException {
   public UserExistsException(){
        super();
   }

   public UserExistsException(String args){
        super(args);
   }
}
