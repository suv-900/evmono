package com.project.evm.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.exceptions.CredentialsDontMatchException;
import com.project.evm.exceptions.UserNotFoundException;
import com.project.evm.models.dto.UserDTO;
import com.project.evm.models.dto.UserLogin;
import com.project.evm.models.entities.User;
import com.project.evm.models.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository repository;
   
    @Autowired
    private PasswordHasher hasher;

    public Long addUser(User user)throws Exception{
        String hashedPassword = hasher.hashPassword(user.getPassword());
        
        user.setPassword(hashedPassword);
        
        User savedUser = repository.save(user);

        return savedUser.getId();
    }
    
    public Long loginUser(String username,String password)throws 
        CredentialsDontMatchException,Exception,UserNotFoundException
    {
        
        User user = repository.getPasswordByUsernameWithID(username);
        
        if(user == null){
            throw new UserNotFoundException("User doesnt exists try registering first.");
        }

        hasher.comparePassword(user.getPassword(),password);

        return user.getId();
    }
    
    public UserDTO getUserById(Long userID)throws UserNotFoundException,Exception{
        Optional<User> userEntity = repository.getUserById(userID);
        
        if(userEntity.isEmpty()){
            throw new UserNotFoundException();
        }

        User user = userEntity.get(); 
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setDescription(user.getDescription());

        return userDTO;
    }

    //needs to be protected 
    public User getUserFullDetails(Long userID)throws Exception{
        return repository.getReferenceById(userID);
    }

    public void updateUser(User user)throws Exception{
        repository.save(user);
    }

    public void deleteUser(User user)throws Exception{
       repository.delete(user); 
    }

    // public void deleteUserByUsername(String username)throws Exception{
    //     userRepository.deleteUserByUsername(username);
    // }

    public boolean existsByUsername(String username)throws Exception{
        return repository.existsByUsername(username);
    }

    public boolean existsByEmail(String email)throws Exception{
        return repository.existsByEmail(email);
    }

    public long getCount()throws Exception{
        return repository.getCount();
    }
    public boolean exists(User user)throws Exception{
        return repository.userExists(user.getName(),user.getEmail());
    }

    //TODO:idList must not contain null
    //for retriving users associated with a event
    public List<UserDTO> findAllById(Iterable<Long> idList)throws Exception{
        List<UserDTO> list = new LinkedList<>();

        List<User> entityList = repository.findAllById(idList);

        entityList.forEach((userEntity)->{
            UserDTO newDTO = new UserDTO();
            newDTO.setName(userEntity.getName());
            newDTO.setDescription(userEntity.getDescription());
            newDTO.setEmail(userEntity.getEmail());
            list.add(newDTO);
        });

        entityList.clear();

        return list;

    }
    
}
