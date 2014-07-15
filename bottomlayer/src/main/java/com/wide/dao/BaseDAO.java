package com.wide.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public class BaseDAO {

    protected EntityManagerFactory entityManagerFactory;

    public BaseDAO(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    protected <T> void create(final T entity) {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(entity);
        et.commit();
        em.close();
    }

    protected <T> void delete(final T entity) throws NoResultException {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.remove(em.merge(entity));
        et.commit();
        em.close();
    }

    protected <T> T deleteById(final Class<T> type, final Long id) throws NoResultException {
        T object = findById(type, id);
        delete(object);
        return object;
    }

    @SuppressWarnings("unchecked")
    protected <T> T findById(final Class<T> type, final Long id) throws NoResultException {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Class<?> clazz = getObjectClass(type);
        T result = (T) em.find(clazz, id);
        if (result == null) {
            throw new NoResultException("No object of type: " + type + " with ID: " + id);
        }
        et.commit();
        em.close();
        return result;
    }

    protected <T> T save(final T entity) {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        T result = em.merge(entity);
        et.commit();
        em.close();
        return result;
    }

    protected <T> void refresh(final T entity) {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        em.refresh(entity);
        et.commit();
        em.close();
    }

    protected Class<?> getObjectClass(final Object type) throws IllegalArgumentException {
        Class<?> clazz = null;
        if (type == null) {
            throw new IllegalArgumentException("Null has no type. You must pass an Object");
        }
        else if (type instanceof Class<?>) {
            clazz = (Class<?>) type;
        }
        else {
            clazz = type.getClass();
        }
        return clazz;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> findByNamedQuery(final String namedQueryName) {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        List<T> result = em.createNamedQuery(namedQueryName).getResultList();
        et.commit();
        em.close();
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> findByNamedQuery(final String namedQueryName, final Object... params) {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Query query = em.createNamedQuery(namedQueryName);
        int i = 1;
        for (Object p : params) {
            query.setParameter(i++, p);
        }
        List<T> result = query.getResultList();
        et.commit();
        em.close();
        return result;
    }

    protected <T> List<T> findAll(final Class<T> type) {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(type);
        query.from(type);
        List<T> result = em.createQuery(query).getResultList();
        et.commit();
        em.close();
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <T> T findUniqueByNamedQuery(final String namedQueryName) throws NoResultException {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        T result = (T) em.createNamedQuery(namedQueryName).getSingleResult();
        et.commit();
        em.close();
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <T> T findUniqueByNamedQuery(final String namedQueryName, final Object... params) throws NoResultException {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Query query = em.createNamedQuery(namedQueryName);
        int i = 1;
        for (Object p : params) {
            query.setParameter(i++, p);
        }
        T result = (T) query.getSingleResult();
        et.commit();
        em.close();
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> findByQuery(final String namedQueryName, final Object... params) {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Query query = em.createQuery(namedQueryName);
        int i = 1;
        for (Object p : params) {
            query.setParameter(i++, p);
        }
        List<T> result = query.getResultList();
        et.commit();
        em.close();
        return result;
    }

    @SuppressWarnings("unchecked")
    protected <T> T findUniqueByQuery(final String namedQueryName, final Object... params) throws NoResultException {
        EntityManager em = this.entityManagerFactory.createEntityManager();
        EntityTransaction et = em.getTransaction();
        et.begin();
        Query query = em.createQuery(namedQueryName);
        int i = 1;
        for (Object p : params) {
            query.setParameter(i++, p);
        }
        T result = (T) query.getSingleResult();
        et.commit();
        em.close();
        return result;
    }

}
