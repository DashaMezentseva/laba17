package com.nixsolutions.service;

import com.nixsolutions.service.RoleDao;
import com.nixsolutions.domain.Role;
import com.nixsolutions.util.HibernateUtil;
import javax.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateRoleDao implements RoleDao {

    private static final Logger LOG = LoggerFactory
            .getLogger(HibernateRoleDao.class);

    private static final String FIND_ROLE_BY_NAME =
            "FROM Role r WHERE r.name = :name";


    @Override
    public void create(Role role) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        if (role == null){
            LOG.error("Role = null", new NullPointerException());
            throw new NullPointerException();
        }

        try {
            transaction = session.beginTransaction();
            session.save(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("Cannot create a role", e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }

    }

    @Override
    public void update(Role role) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        if (role == null){
            LOG.error("Role = null", new NullPointerException());
            throw new NullPointerException();
        }

        try {
            transaction = session.beginTransaction();
            session.update(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("Cannot update a role", e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }

    }

    @Override
    public void remove(Role role) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        if (role == null){
            LOG.error("Role = null", new NullPointerException());
            throw new NullPointerException();
        }

        try {
            transaction = session.beginTransaction();
            session.remove(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOG.error("Cannot remove a role", e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }

    }

    @Override
    public Role findByName(String name) {
        Session session= HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        if (name == null || name.isEmpty()){
            LOG.error("name = null or is empty", new NullPointerException());
            throw new NullPointerException();
        }
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
            LOG.error("Cannot find by name a role", e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return role;
    }
}
