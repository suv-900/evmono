package com.project.evm.exceptions;

public class UnauthorizedAccessException extends RuntimeException{
   public UnauthorizedAccessException(){
    super();
   }
   
   public UnauthorizedAccessException(String args){
        super(args);
   }
}
