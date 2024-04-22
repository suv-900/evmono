package com.project.evm.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.evm.models.dto.UserDTO;
import com.project.evm.models.entities.User;

import lombok.extern.slf4j.Slf4j;
//count,exists,findAllById,deleteAllById,findById
@Slf4j
@Repository
@Transactional
public class UserDAO {
    

    @Autowired
    private SessionFactory sessionFactory;

    public User addUser(User user)throws Exception{
        Session session = this.sessionFactory.getCurrentSession();
        
        session.persist("users",user);

        log.info("User added: "+user.getName());
        return user;
    }

    public boolean exists(User user)throws Exception{
        Session session = this.sessionFactory.getCurrentSession();
        
        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE name = :username OR email = :email) AS user_exists";

        Boolean exists = session.createQuery(query,Boolean.class)
            .setParameter("username",user.getName())
            .setParameter("email",user.getEmail()) 
            .getSingleResult();
        
        return exists;
    }

    /**
     * @param userID
     * @return
     * @throws Exception
     */
    public UserDTO findById(int userID)throws Exception{
        Session session = this.sessionFactory.getCurrentSession();
        
        return session.get(UserDTO.class,userID);
       
    }
    
    // public Optional<UserDTO> findById2(long userID){
    //     return repository.findByID(userID);
    // }
    // public Optional<UserEntity> findById2(long userID){
    //     return userRepository.findById(userID);
    // }

    public List<User> findAllById(Iterable<Integer> idList)throws Exception{
        List<User> userList = new LinkedList<>();

        Session session = this.sessionFactory.getCurrentSession();
        
        String query = "SELECT name,email,description FROM users WHERE id = :id";

        idList.forEach((id) -> {
            User user = session.createQuery(query,User.class)
                .setParameter("id",id)    
                .getSingleResultOrNull();

            if(user != null){
                userList.add(user);
            }
        });

        return userList;
    }

    public String getPasswordByUsername(String name)throws Exception{
        Session session = this.sessionFactory.getCurrentSession();
        String query = "SELECT password FROM users WHERE name = :name";
        
        String password = session.createQuery(query,String.class)
                .setParameter("name",name)
                .uniqueResult();
        
        return password;
    }
    //notify other services [cascade] 
    public void deleteUser(User user)throws Exception{
        Session session = this.sessionFactory.getCurrentSession();
        session.remove(user);
        log.info("User removed: "+user.getName());
    }

    public User updateUser(User user)throws Exception{
        Session session = this.sessionFactory.getCurrentSession();
        
        User updatedUser = session.merge("users",user);

        log.info("User updated: "+updatedUser.toString());

        return updatedUser;
    }
    
    public long getCount()throws Exception{
        Session session = this.sessionFactory.getCurrentSession();
        String query = "SELECT COUNT(*) FROM users AS user_count";

        Long count = session.createQuery(query,Long.class).uniqueResult();

        return count;
    }

    public boolean existsByUsername(String username)throws Exception{
        Session session = this.sessionFactory.getCurrentSession();
        String query ="SELECT EXISTS(SELECT 1 FROM users WHERE name = :username) AS user_exists";
        
        boolean exists = session.createQuery(query,Boolean.class)
            .setParameter("username",username)
            .uniqueResult();
        
        return exists;
    }
    public boolean existsByEmail(String email)throws Exception{
        Session session = this.sessionFactory.getCurrentSession();
        String query ="SELECT EXISTS(SELECT 1 FROM users WHERE email = :email) AS user_exists";
        
        boolean exists = session.createQuery(query,Boolean.class)
            .setParameter("email",email)
            .uniqueResult();
        
        return exists;
    }

}
