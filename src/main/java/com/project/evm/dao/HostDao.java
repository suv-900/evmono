package com.project.evm.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.evm.config.HibernateUtil;
import com.project.evm.models.entities.Host;
import com.project.evm.services.PasswordHasher;

import jakarta.transaction.Transactional;

@Repository
public class HostDao {
    @Autowired
    private PasswordHasher hasher;

    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public Long addHost(Host host)throws Exception{
        String hashedPassword = hasher.hashPassword(host.getPassword());
        host.setPassword(hashedPassword);

        Session session = sessionFactory.getCurrentSession();
        session.persist(host);
        session.flush();
        return host.getId();
    }
   
    @Transactional
    public Host retrieveHostById(Long hostID)throws Exception{
        Session session = sessionFactory.getCurrentSession();
        Host host = session.get(Host.class,hostID);
        return host;
    } 
    
    @Transactional
    public Host updateHost(Host host)throws Exception{
        Session session = sessionFactory.getCurrentSession();
        return session.merge(host);
    }
    
    @Transactional
    public void removeHost(Host host)throws Exception{
        Session session = sessionFactory.getCurrentSession();
        session.remove(host);
    }
    
    @Transactional
    public Optional<Object[]> loginHost(String name)throws Exception
    {
        String hql = "select u.id,u.password from User u where u.name = :name";
        Session session = sessionFactory.getCurrentSession();
        Query<Object[]> query = session.createQuery(hql,Object[].class);
        Optional<Object[]> resultOp = query.uniqueResultOptional();

        return resultOp;    
    }

    @Transactional
    public Host getHostFullDetails(Long hostID)throws Exception{
        Session session = sessionFactory.getCurrentSession();
        return session.get(Host.class,hostID);
    }

    @Transactional
    public List<Host> getHostFullDetailsList(List<Long> hostIDs)throws Exception{
        List<Host> hostList = new LinkedList<>();
        Session session = sessionFactory.getCurrentSession();
        hostIDs.forEach((hostId)->{
            Host host = session.get(Host.class,hostId);
            hostList.add(host);
        });
        return hostList;
    }

    @Transactional
    public boolean exists(String name,String email)throws Exception{
        String hql = "select count(h) from Host h where h.name = :name or h.email = :email";
        Session session = sessionFactory.getCurrentSession();
        Query<Integer> query = session.createQuery(hql,Integer.class);
        Integer count = query.uniqueResult();
        return count > 0;
    }
    
}
