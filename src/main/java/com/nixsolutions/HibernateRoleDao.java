package com.nixsolutions;

import com.nixsolutions.dao.RoleDao;
import com.nixsolutions.entity.Role;
import com.nixsolutions.entity.User;
import com.nixsolutions.util.HibernateUtil;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateRoleDao implements RoleDao {

    private static final String FIND_ROLE_BY_NAME =
        "FROM Role r WHERE r.name = :name";


    @Override
    public void create(Role role) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(role);
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
    public void update(Role role) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(role);
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
    public void remove(Role role) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.remove(role);
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
    public Role findByName(String name) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Role role = null;
        try {
            transaction = session.beginTransaction();

            Query query = session.createQuery(FIND_ROLE_BY_NAME);
            query.setParameter("name", name);
            role = (Role) query.getResultList().stream().findFirst().orElse(null);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return role;
    }
}
