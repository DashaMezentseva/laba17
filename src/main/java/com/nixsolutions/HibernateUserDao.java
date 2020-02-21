package com.nixsolutions;

import com.nixsolutions.dao.UserDao;
import com.nixsolutions.entity.Role;
import com.nixsolutions.entity.User;
import com.nixsolutions.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

public class HibernateUserDao implements UserDao {

    private static final String FIND_ALL_USERS = "FROM User";
    private static final String FIND_USER_BY_LOGIN =
        "FROM User u WHERE u.login = :login";
    private static final String FIND_USER_BY_EMAIL =
        "FROM User u WHERE u.email = :email";
    private static final String FIND_USER_BY_ID =
        "FROM User u WHERE u.userId = :userId";


    @Override
    public void create(User user) {

        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void update(User user) {

        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public void remove(User user) {

        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> findAll() {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        List<User> users = null;

        try {
            transaction = session.beginTransaction();

            users = session.createQuery(FIND_ALL_USERS).list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return users;
    }

    @Override
    public User findByLogin(String login) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        User user = null;
        try {
            transaction = session.beginTransaction();

            javax.persistence.Query query = session.createQuery(FIND_USER_BY_LOGIN);
            query.setParameter("login", login);
            user = (User) query.getResultList().stream().findFirst().orElse(null);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        User user = null;
        try {
            transaction = session.beginTransaction();

            javax.persistence.Query query = session.createQuery(FIND_USER_BY_EMAIL);
            query.setParameter("email", email);
            user = (User) query.getResultList().stream().findFirst().orElse(null);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }

    public User findById(Long id) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        User user = null;

        try {
            transaction = session.beginTransaction();
            javax.persistence.Query query = session.createQuery(FIND_USER_BY_ID);
            query.setParameter("userId", id);
            user = (User) query.getResultList().stream().findFirst().orElse(null);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }
}
