package com.project.evm.services;

import java.util.LinkedList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.project.evm.exceptions.CredentialsDontMatchException;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
@Service
public class PasswordHasher {
    // private final static Logger log = LoggerFactory.getLogger(PasswordHasher.class);
   
    public String hashPassword(String password){
       String salt = BCrypt.gensalt();
       
       String hashedPassword = BCrypt.hashpw(password,salt);

       return hashedPassword+":"+salt;
    }

    public void comparePassword(String dbPassword,String plainPassword)throws CredentialsDontMatchException,IllegalArgumentException{
        
        List<String> list = this.passwordBreaker(dbPassword);

        String dbHashedPassword = list.get(0);
        String dbSalt = list.get(1);
        
        String password2 = BCrypt.hashpw(plainPassword,dbSalt);

        boolean matches = this.compareStrings(dbHashedPassword,password2);

        if(!matches){
            throw new CredentialsDontMatchException("Credentials dont match");
        }
    }
    
    private boolean compareStrings(String s1,String s2){
        
        if(s1.length() != s2.length()) return false;

        for(int i=0;i<s1.length();i++){
            if(s1.charAt(i) != s2.charAt(i)){
                return false;
            }
        }
        return true;
    }
    
    private List<String> passwordBreaker(String dbPassword){
        List<String> list = new LinkedList<>();

        StringBuilder hashedPassword = new StringBuilder();
        
        int i = 0;
        for(i = 0;i<dbPassword.length();i++){
            if(dbPassword.charAt(i) == ':'){
                break;
            }
            hashedPassword.append(dbPassword.charAt(i));
        }
        if(i == dbPassword.length()){
            //no salt found
            throw new IllegalArgumentException("Salt not found in dbPassword.Reached the end of the string.");
        }
        String dbSalt = dbPassword.substring(i+1, dbPassword.length());

        list.add(hashedPassword.toString());
        list.add(dbSalt);

        return list;

   }
}
