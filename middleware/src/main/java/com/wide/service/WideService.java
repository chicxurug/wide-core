package com.wide.service;

import javax.persistence.EntityManagerFactory;

import com.wide.dao.WideDAO;

public class WideService extends WideDAO {

    public WideService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

}
