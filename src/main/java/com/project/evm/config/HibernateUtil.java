package com.project.evm.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HibernateUtil {
   private static SessionFactory sessionFactory = buildSessionFactory();
   
   private static SessionFactory buildSessionFactory(){
    log.info("Buidling Session Factory");
    try{
        return new Configuration().configure().buildSessionFactory();
    }catch(Exception ex){
        log.error("CRITICAL:SessionFactory couldnt be started."+ex.getMessage());
        throw ex;
    }
   }

   public static SessionFactory getSessionFactory(){
    return sessionFactory;
   }
}
