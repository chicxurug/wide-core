package com.wide.wideweb.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Test;
import com.wide.persistence.PersistenceListener;
import com.wide.service.WideService;

public final class ViewDataCache implements Serializable {

    private static final long serialVersionUID = 1L;

    private static ViewDataCache instance;

    private WideService service = new WideService(PersistenceListener.getEntityManagerFactory());

    private ViewDataCache() {
    }

    public void doAllInit() {
        this.exercises.clear();
        initCategories();
        initTests();
    }

    private Category rootCategory;
    private Multimap<Category, Category> categories = ArrayListMultimap.create();
    private Multimap<Category, Exercise> exercises = ArrayListMultimap.create();
    private List<Test> tests = new ArrayList<Test>();

    private String username;

    public void initCategories() {
        this.categories.clear();
        Queue<Category> queue = new LinkedList<Category>();

        // creating category->subcategory mapping with
        // depth-first search algorithm, starting from the root category
        this.rootCategory = this.service.getOrCreateCategory("", null);
        // this.categories.put(null, this.rootCategory);
        queue.add(this.rootCategory);

        while (!queue.isEmpty()) {
            Category category = queue.poll();
            if (category.getParent() != null) {
                this.categories.put(category.getParent(), category);
                // category.setPath(category.getParent().getPath() + " / " + category.getName());
                // } else {
                // category.setPath(category.getName());
            }
            List<Category> subCategories = this.service.getCategoriesByParent(category);
            queue.addAll(subCategories);
        }

        // filling (transient) path values
        this.rootCategory.setPath("");
        StringBuilder categoryText = new StringBuilder();
        Category parent = null;
        for (final Category category : this.categories.values()) {
            categoryText.append(category.getName());
            parent = category.getParent();
            while (parent != null && !parent.equals(this.rootCategory)) {
                categoryText.insert(0, " / ");
                categoryText.insert(0, parent.getName());
                parent = parent.getParent();
            }
            category.setPath(categoryText.toString());
            categoryText.setLength(0);
        }
    }

    private void initTests() {
        this.tests.clear();
        this.tests.addAll(this.service.getTests());
    }

    public Category getRootCategory() {
        return this.rootCategory;
    }

    public Category getCategoryByName(String categoryName) {
        for (Category c : this.categories.values()) {
            if (categoryName.equals(c.getName())) {
                return c;
            }
        }
        return this.rootCategory;
    }

    public Category getCategoryById(Long id) {
        for (Category c : this.categories.values()) {
            if (id.equals(c.getId())) {
                return c;
            }
        }
        if (id.equals(this.rootCategory.getId())) {
            return this.rootCategory;
        } else {
            return null;
        }
    }

    public Multimap<Category, Category> getCategories() {
        return this.categories;
    }

    public Collection<Exercise> getExerciseByCategory(Category category) {
        if (this.exercises.get(category).isEmpty()) {
            this.exercises.putAll(category, this.service.getExercisesByCategory(category));
        }

        return this.exercises.get(category);
    }

    public Collection<Test> getTestsByCategory(Category category) {
        Collection<Exercise> exs = getExerciseByCategory(category);
        Set<Test> affectedTests = new HashSet<Test>();
        for (Exercise e : exs) {
            affectedTests.addAll(this.service.getTestsByExercise(e));
        }

        return affectedTests;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public static synchronized ViewDataCache getInstance() {
        if (instance == null) {
            instance = new ViewDataCache();
            instance.doAllInit();
        }
        return instance;
    }

    public Collection<Exercise> getExercisesByProperty(String propName, String propValue) {
        return this.service.getExercisesByProperty(propName, propValue);
    }

    public Collection<Exercise> getExercisesBySearchText(String searchText) {
        return this.service.getExercisesBySearchText(searchText);
    }

    public Collection<Test> getTestsBySearchText(String searchText) {
        return this.service.getTestsBySearchText(searchText);
    }

    public Exercise getExerciseByCategory(Category current, String exId) {
        Exercise found = null;
        Long id = Long.parseLong(exId);

        // The id is coming from a global search
        if (current == null) {
            found = this.service.getExerciseById(id);
        } else {
            Collection<Exercise> possibleExercises = this.exercises.get(current);
            for (Exercise e : possibleExercises) {
                if (id.equals(e.getId())) {
                    found = e;
                    break;
                }
            }
        }
        if (found == null) {
            found = this.service.getExerciseById(id);
        }

        return found;
    }

}
