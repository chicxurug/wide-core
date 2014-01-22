package com.wide.persistence;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class PersistenceListener implements ServletContextListener {

    private static EntityManagerFactory entityManagerFactory;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        entityManagerFactory = Persistence.createEntityManagerFactory("com.wide.jpa");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}