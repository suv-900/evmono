package com.project.evm.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.project.evm.config.HibernateUtil;
import com.project.evm.exceptions.EventNotFoundException;
import com.project.evm.models.entities.Event;

import jakarta.transaction.Transactional;

@Repository
public class EventDao {
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Long saveEvent(Event event)throws Exception{
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(event);
        session.getTransaction().commit();
        return event.getId();
    }

    @Transactional
    public Event retrieveEventById(Long eventID)throws Exception{
        Session session = sessionFactory.getCurrentSession();
        Event event = session.get(Event.class,eventID);
        if(event == null){
            throw new EventNotFoundException();
        }
        return event;
    }

    @Transactional
    public Event updateEvent(Event event)throws Exception{
        Session session = sessionFactory.getCurrentSession();
        return session.merge(event);
    }

    @Transactional
    public void removeEvent(Event event)throws Exception{
        Session session = sessionFactory.getCurrentSession();
        session.remove(event);
    }

    public List<Event> getEventsList(List<Long> eventIDs)throws Exception{
        List<Event> eventsList = new LinkedList<>();
        
        Session session = sessionFactory.getCurrentSession();

        eventIDs.forEach((eventID)->{
            Event event = session.get(Event.class,eventID);
            eventsList.add(event);
        });

        return eventsList;
    }    
}
