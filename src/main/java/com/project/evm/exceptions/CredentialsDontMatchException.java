package com.project.evm.exceptions;

public class CredentialsDontMatchException extends RuntimeException {
   public CredentialsDontMatchException(){
    super();
   }
   public CredentialsDontMatchException(String args){
    super(args);
   } 
}
