package com.project.evm.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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
        
        //attaches object to persistence state
        session.persist(user);
        
        //syncs the objects in persistence state to the datastore
        session.flush();
        return user.getId();
    }

    @Transactional
    public Optional<Long> getUserID(String name){
        Session session = sessionFactory.getCurrentSession();
        String hql = "select id from User where name = :name";
        Query<Long> query = session.createQuery(hql,Long.class);

        query.setParameter("name",name);

        return query.uniqueResultOptional();
    }

    @Transactional
    public User updateUser(User user){
        Session session = sessionFactory.getCurrentSession();
        return session.merge(user);
    }

    @Transactional
    public void deleteUser(User user){
        Session session = sessionFactory.getCurrentSession();
        session.remove(user);
    }

    public Optional<Object[]> loginUser(String username)throws 
        CredentialsDontMatchException,Exception,UserNotFoundException
    {
        
        Session session = sessionFactory.getCurrentSession();
        
        String hql = "select u.password,u.id from User u where u.name = :name";
        
        Query<Object[]> query = session.createQuery(hql,Object[].class);
        
        query.setParameter("name",username);
        
        Optional<Object[]> result = query.uniqueResultOptional();

        return result;
    }
    
    
    @Transactional
    public User getUserByID(Long userID)throws UserNotFoundException{
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class,userID);
    }

    //needs to be protected 
    @Transactional
    public List<User> getFullUserDetailsList(List<Long> userIDs){
        List<User> list = new LinkedList<User>();
        
        Session session = sessionFactory.getCurrentSession();
        userIDs.forEach((userID)->{
            User user = session.get(User.class,userID);
            list.add(user);
        }); 

        return list;
    }

    public boolean existsByUsername(String username){
        String hql = "select count(u) from User u where u.name = :name";
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery(hql,Integer.class);
        query.setParameter("name",username);
        Integer count = query.uniqueResult();
        return count > 0;
    }
    
    public boolean existsByEmail(String email){
        String hql = "select count(u) from User u where u.email = :email";
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery(hql,Integer.class);
        query.setParameter("email",email);
        Integer count = query.uniqueResult();
        return count > 0;
    }

    public Long getCount(){
        String hql = "select count(u) from User u";
        Session session = sessionFactory.getCurrentSession();
        Query<Long> query = session.createQuery(hql,Long.class);
        Long count = query.uniqueResult();
        return count;
    }

    public boolean exists(String name,String email){
        String hql = "select count(u) from User u where u.name = :name or u.email = :email";
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery(hql,Integer.class);
        query.setParameter("name",name);
        query.setParameter("email",email);
        Integer count = query.uniqueResult();
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


