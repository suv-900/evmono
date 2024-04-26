package com.project.evm.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.dao.UserDao;
import com.project.evm.exceptions.CredentialsDontMatchException;
import com.project.evm.exceptions.UserNotFoundException;
import com.project.evm.models.dto.UserDTO;
import com.project.evm.models.entities.User;

@Service
public class UserService {
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private PasswordHasher hasher;
    //persistent state
    //sessions

    public Long addUser(User user){
        String hashedPassword = hasher.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return userDao.addUser(user);
    }

    public Optional<Long> getUserID(String name){
       return userDao.getUserID(name); 
    }

    public User updateUser(User user){
       return userDao.updateUser(user); 
    }

    public void deleteUser(User user){
       userDao.deleteUser(user); 
    }

    public Long loginUser(String name,String password)throws 
        CredentialsDontMatchException,Exception,UserNotFoundException
    {
        Optional<Object[]> resultOp = userDao.loginUser(name);

        if(resultOp.isEmpty()){
            throw new UserNotFoundException("User doesnt exists try registering first.");
        }
        Object[] result = resultOp.get();
        String dbPassword = (String) result[0];
        Long userID = (Long) result[1];
        
        hasher.comparePassword(dbPassword,password);

        return userID;
    }
    
    public User getUserByID(Long userID)throws UserNotFoundException{
        return userDao.getUserByID(userID);
    }
    public UserDTO getUserByIdDTO(Long userID)throws UserNotFoundException{
        UserDTO user = new UserDTO();
        User dbUser = getUserByID(userID);
        user.setName(dbUser.getName());
        user.setEmail(dbUser.getEmail());
        user.setDescription(dbUser.getDescription());
        return user;
    }
    //needs to be protected 
    public List<User> getFullUserDetailsList(List<Long> userIDs){
        return userDao.getFullUserDetailsList(userIDs);
    }

    public boolean existsByUsername(String username){
        return userDao.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email){
       return userDao.existsByEmail(email); 
    }

    public Long getCount(){
       return userDao.getCount(); 
    }

    public boolean exists(String name,String email){
       return userDao.exists(name,email); 
    } 

    //TODO:idList must not contain null
    //for retriving users associated with a event
    public List<UserDTO> findAllById(List<Long> idList)throws Exception{
        return userDao.findAllById(idList);
    }

    
}
