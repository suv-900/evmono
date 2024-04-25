package com.project.evm.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.spi.CurrentSessionContext;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.evm.config.HibernateUtil;
import com.project.evm.exceptions.CredentialsDontMatchException;
import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.exceptions.TicketExistsException;
import com.project.evm.exceptions.UserNotFoundException;
import com.project.evm.models.dto.UserDTO;
import com.project.evm.models.entities.Event;
import com.project.evm.models.entities.Ticket;
import com.project.evm.models.entities.User;
import com.project.evm.models.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EventService eventService;

    @Autowired
    private TicketService ticketService;

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Autowired
    private PasswordHasher hasher;
    //persistent state
    //sessions

    

    @Transactional
    public void addUser(User user){

        String hashedPassword = hasher.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        
        //session object = unit of work with database
        Session session = sessionFactory.getCurrentSession();
        
        //attaches object to persistence state
        session.persist(user);
        
        //syncs the objects in persistence state to the datastore
        //session.flush();
    }

    @Transactional
    public Long getUserID(User user){
        Session session = sessionFactory.getCurrentSession();
        String hql = "select id from User where name = :name";
        Query<Long> query = session.createQuery(hql,Long.class);

        query.setParameter("name",user.getName());

        Long userID = query.uniqueResult();
        return userID;
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

    // public Long addUser(User user)throws Exception{
    //     String hashedPassword = hasher.hashPassword(user.getPassword());
        
    //     user.setPassword(hashedPassword);
        
    //     User savedUser = userRepository.save(user);

    //     return savedUser.getId();
    // }
    
    public Long loginUser(String username,String password)throws 
        CredentialsDontMatchException,Exception,UserNotFoundException
    {
        
        Session session = sessionFactory.getCurrentSession();
        
        String hql = "select u.password,u.id from User u where u.name = :name";
        
        Query<Object[]> query = session.createQuery(hql,Object[].class);
        
        query.setParameter("name",username);
        
        Object[] result = query.uniqueResult();

        if(result.length == 0){
            throw new UserNotFoundException("User doesnt exists try registering first.");
        }
        String dbPassword = (String) result[0];
        Long userID = (Long) result[1];
        
        hasher.comparePassword(dbPassword,password);

        return userID;
    }
    
    
    @Transactional
    public User getUserByID(Long userID)throws UserNotFoundException{
        Session session = sessionFactory.getCurrentSession();

        String hql = "select u.name,u.email,u.description from User u where u.id = :id";

        Query<String[]> query = session.createQuery(hql,String[].class);

        query.setParameter("id",userID);

        Optional<String[]> resultOp = query.uniqueResultOptional();

        if(resultOp.isEmpty()){
            throw new UserNotFoundException("User doesnt exists");
        }

        String[] result = resultOp.get();
        User newUser = new User();
        newUser.setName(result[0]);
        newUser.setEmail(result[1]);
        newUser.setDescription(result[2]);

        return newUser;
    }

    public UserDTO getUserById(Long userID)throws UserNotFoundException,Exception{
        Optional<User> userEntity = userRepository.getUserById(userID);
        
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
    @Transactional
    public List<User> getFullUserDetailsList(Long[] userIDs){
        List<User> list = new LinkedList<User>();
        
        String hql = "from User u where u.id = :id";
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery(hql,User.class);
        
        for(Long userID : userIDs){
            query.setParameter("id",userID);
            User user = query.uniqueResult();
            list.add(user); 
        }

        return list;
    }
    
    @Transactional
    public List<User> getFullUserDetailsList(List<Long> userIDs){
        List<User> list = new LinkedList<User>();
        
        String hql = "from User u where u.id = :id";
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery(hql,User.class);
        
        userIDs.forEach((userID)->{
            query.setParameter("id",userID);
            User user = query.uniqueResult();
            list.add(user); 
        });
                
        return list;
    } 

    public Optional<User> getFullUserDetails(Long userID){
        String hql = "from User u where u.id = :id";
        Session session = sessionFactory.getCurrentSession();
        Query<User> query = session.createQuery(hql,User.class);
        query.setParameter("id",userID);
        Optional<User> user = query.uniqueResultOptional();
        return user;
    }

    // public void deleteUserByUsername(String username)throws Exception{
    //     userRepository.deleteUserByUsername(username);
    // }

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

    public Ticket buyTicket(Long userID,Long eventID)throws UserNotFoundException,EventNotFoundException,TicketExistsException,Exception{

        if(ticketService.ticketExists(userID,eventID)){
            throw new TicketExistsException();
        } 

        User user = getUserByID(userID);
        Event event = eventService.getEventById(eventID);

        Ticket ticket = new Ticket();
        ticket.setForEvent(event);
        ticket.setUnderName(user);
        ticket.setUserID(user.getId());
        ticket.setEventID(event.getId());

        return ticketService.addTicket(ticket);
    }
}
