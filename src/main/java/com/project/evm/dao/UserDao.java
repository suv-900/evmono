package com.project.evm.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.project.evm.config.HibernateUtil;
import com.project.evm.exceptions.CredentialsDontMatchException;
import com.project.evm.exceptions.UserNotFoundException;
import com.project.evm.models.dto.UserDTO;
import com.project.evm.models.entities.User;
import jakarta.transaction.Transactional;

@Repository
public class UserDao{
    
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    //persistent state
    //sessions
    
    public Long addUser(User user){

        //session object = unit of work with database
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction(); 
        //attaches object to persistence state
        session.persist(user);
        session.getTransaction().commit(); 
        //syncs the objects in persistence state to the datastore
        return user.getId();
    }

    @Transactional(rollbackOn=Exception.class)
    public Optional<Long> getUserID(String name){
        String hql = "select id from User where name = :name";
        Session session = sessionFactory.getCurrentSession();

        return session.createQuery(hql,Long.class)
            .setParameter("name",name)
            .uniqueResultOptional();
    }

    @Transactional(rollbackOn=Exception.class)
    public User updateUser(User user){
        Session session = sessionFactory.getCurrentSession();
        return session.merge(user);
    }

    @Transactional(rollbackOn=Exception.class)
    public void deleteUser(User user){
        Session session = sessionFactory.getCurrentSession();
        session.remove(user);
    }

    @Transactional(rollbackOn=Exception.class)
    public Optional<Object[]> loginUser(String username)throws 
        CredentialsDontMatchException,Exception,UserNotFoundException
    {
        Session session = sessionFactory.getCurrentSession();
        String hql = "select u.password,u.id from User u where u.name = :name";
        
        Optional<Object[]> result = session.createQuery(hql,Object[].class)
            .setParameter("name",username)
            .uniqueResultOptional();
        
        return result;
    }
    
    
    @Transactional(rollbackOn=Exception.class)
    public User getUserByID(Long userID){
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class,userID);
    }

    @Transactional(rollbackOn=Exception.class)
    public List<User> getAllUsers(){
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User",User.class)
            .list();
    }

    //needs to be protected 
    @Transactional(rollbackOn=Exception.class)
    public List<User> getFullUserDetailsList(List<Long> userIDs){
        List<User> list = new LinkedList<User>();
        
        Session session = sessionFactory.getCurrentSession();
        userIDs.forEach((userID)->{
            User user = session.get(User.class,userID);
            list.add(user);
        }); 

        return list;
    }

    @Transactional(rollbackOn=Exception.class)
    public boolean existsByUsername(String username){
        String hql = "select count(u) from User u where u.name = :name";
        Session session = sessionFactory.getCurrentSession();
        Integer count = session.createQuery(hql,Integer.class)
            .setParameter("name",username)
            .uniqueResult();
        return count > 0;
    }
    
    public boolean existsByEmail(String email){
        String hql = "select count(u) from User u where u.email = :email";
        Session session = sessionFactory.getCurrentSession();
        Integer count = session.createQuery(hql,Integer.class)
            .setParameter("email",email)
            .uniqueResult();
        return count > 0;
    }

    public Long getCount(){
        String hql = "select count(u) from User u";
        Session session = sessionFactory.getCurrentSession();
        Long count = session.createQuery(hql,Long.class)
            .uniqueResult();
        return count;
    }

    public boolean exists(String name,String email){
        String hql = "select count(u) from User u where u.name = :name or u.email = :email";
        Session session = sessionFactory.getCurrentSession();
        Integer count = session.createQuery(hql,Integer.class)
            .setParameter("name",name)
            .setParameter("email",email)
            .uniqueResult();
        return count > 0;
    } 

    //TODO:idList must not contain null
    //for retriving users associated with a event
    public List<UserDTO> findAllById(List<Long> idList)throws Exception{

        List<User> userList = getFullUserDetailsList(idList);

        List<UserDTO> list = new LinkedList<>();
        
        userList.forEach((user)->{
            UserDTO newDTO = new UserDTO();
            newDTO.setName(user.getName());
            newDTO.setDescription(user.getDescription());
            newDTO.setEmail(user.getEmail());
            list.add(newDTO);
        });

        userList.clear();

        return list;

    }

}


