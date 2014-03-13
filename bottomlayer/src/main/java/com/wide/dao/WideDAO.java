package com.wide.dao;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Feature;

public class WideDAO extends BaseDAO {

    public WideDAO(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public Exercise saveOrUpdateExercise(Exercise e) {
        return save(e);
    }

    public List<Exercise> getExercises() {
        return findAll(Exercise.class);
    }

    public List<Exercise> getExercisesByCategory(Category category) {
        return findByQuery("from Exercise where category = ?1", category);
    }

    public List<Category> getCategories() {
        return findAll(Category.class);
    }

    public List<Category> getCategoriesByParent(Category parent) {
        return findByQuery("from Category where parent = ?1", parent);
    }

    public Category getOrCreateCategory(String name, Category parent) {
        Category result;
        try {
            result = findUniqueByQuery("from Category where name = ?1", name);
        } catch (NoResultException e) {
            result = new Category(name, parent);
            create(result);
        }
        return result;
    }

    public void createCategory(Category c) {
        create(c);
    }

    public Category saveOrUpdateCategory(Category c) {
        return save(c);
    }

    public Feature saveOrUpdateFeature(Feature f) {
        return save(f);
    }

}
