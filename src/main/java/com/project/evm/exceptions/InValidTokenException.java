package com.project.evm.exceptions;

public class InValidTokenException extends RuntimeException {
   public InValidTokenException(){
    super();
   }
   public InValidTokenException(String args){
    super(args);
   }  
}
