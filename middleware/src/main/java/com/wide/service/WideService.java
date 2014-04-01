package com.wide.service;

import java.io.Serializable;

import javax.persistence.EntityManagerFactory;

import com.wide.dao.WideDAO;

public class WideService extends WideDAO implements Serializable {

    private static final long serialVersionUID = -3490692884618730013L;

    public WideService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

}
