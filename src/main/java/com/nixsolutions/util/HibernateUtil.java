package com.nixsolutions.util;

import com.nixsolutions.entity.Role;
import com.nixsolutions.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static SessionFactory sessionFactory=buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try{
            return new Configuration().configure().buildSessionFactory();
        }catch (Exception e){
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}


//    Configuration configuration = new Configuration().configure().addAnnotatedClass(User.class).addAnnotatedClass(Role.class);
//    ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//    SessionFactory sessionFactory = configuration.buildSessionFactory(registry);