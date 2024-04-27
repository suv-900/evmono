package com.project.evm.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.project.evm.config.HibernateUtil;
import com.project.evm.exceptions.TicketNotFoundException;
import com.project.evm.models.entities.Ticket;

import jakarta.transaction.Transactional;

@Repository
public class TicketDao {
     private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
     
     @Transactional
     public boolean ticketExists(Long userID,Long eventID){
          String hql = "select count(t) from Ticket t where t.userID = :userID and t.eventID = :eventID";
          Session session = sessionFactory.getCurrentSession();
          Query<Integer> query = session.createQuery(hql,Integer.class);
          query.setParameter("userID",userID);
          query.setParameter("eventID",eventID);
          Integer count = query.uniqueResult();
          return count > 0;
     }
     
     public Long saveTicket(Ticket ticket){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(ticket);
        session.getTransaction().commit();
        return ticket.getId();
     } 
     
     @Transactional
     public Ticket getTicket(Long ticketID){
          Session session = sessionFactory.getCurrentSession();
          return session.get(Ticket.class,ticketID);
     }

    @Transactional
    public void removeTicket(Long ticketID)throws TicketNotFoundException{
        Session session = sessionFactory.getCurrentSession();
        Ticket ticket = session.get(Ticket.class,ticketID);
        if(ticket == null){
            throw new TicketNotFoundException();
        }
        session.remove(ticket);
    }
    
    @Transactional
    public void removeTicket(Ticket ticket){
        Session session = sessionFactory.getCurrentSession();
        session.remove(ticket);
    }

     @Transactional
     public List<Ticket> getTicketsList(List<Long> ticketIDs){
          List<Ticket> ticketsList = new LinkedList<>();
          Session session = sessionFactory.getCurrentSession();

          ticketIDs.forEach((ticketID)->{
               Ticket ticket = session.get(Ticket.class,ticketID);
               ticketsList.add(ticket);
          });

          return ticketsList;
     }    
}
