package com.wide.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.ExercisePoint;
import com.wide.domainmodel.Feature;
import com.wide.domainmodel.Test;

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

    public List<Test> getTests() {
        return findAll(Test.class);
    }

    public List<Exercise> getExercisesByCategory(Category category) {
        return findByQuery("from Exercise where category = ?1", category);
    }

    public Exercise getExerciseById(long id) {
        return findUniqueByQuery("from Exercise where id = ?1", id);
    }

    public List<Exercise> getExercisesBySearchText(String searchText) {
        return findByQuery("from Exercise where author like ?1 or title like ?1", "%" + searchText + "%");
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

    public List<Test> getTestsByExercise(Exercise e) {
        List<ExercisePoint> ep = findByQuery("from ExercisePoint where exercise = ?1", e);
        if (ep.isEmpty()) {
            return new ArrayList<Test>();
        }
        List<Object> resObjs = findByQuery("from Test t join t.exercises e where ?1 in e", ep);
        List<Test> tests = new ArrayList<Test>();
        for (Object obj : resObjs) {
            Object[] results = (Object[]) obj;
            tests.add((Test) results[0]);
        }
        return tests;
    }

    public List<Test> getTestsBySearchText(String searchText) {
        return findByQuery("from Test where description like ?1", "%" + searchText + "%");
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

    public Test saveOrUpdateTest(Test t) {
        return save(t);
    }

    public void removeExercise(Exercise e) {
        delete(e);
    }

}
