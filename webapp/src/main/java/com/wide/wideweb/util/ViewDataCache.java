package com.wide.wideweb.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.persistence.PersistenceListener;
import com.wide.service.WideService;

public final class ViewDataCache implements Serializable {

    private static final long serialVersionUID = 1L;

    private static ViewDataCache instance;

    private WideService service = new WideService(PersistenceListener.getEntityManagerFactory());

    private ViewDataCache() {
    }

    public void doAllInit() {
        initCategories();
    }

    private Category rootCategory;
    private Multimap<Category, Category> categories = ArrayListMultimap.create();
    private Multimap<Category, Exercise> exercises = ArrayListMultimap.create();

    public void initCategories() {
        this.categories.clear();
        Queue<Category> queue = new LinkedList<Category>();

        this.rootCategory = this.service.getOrCreateCategory("", null);
        // this.categories.put(null, this.rootCategory);
        queue.add(this.rootCategory);

        while (!queue.isEmpty()) {
            Category category = queue.poll();
            if (category.getParent() != null) {
                this.categories.put(category.getParent(), category);
            }
            List<Category> subCategories = this.service.getCategoriesByParent(category);
            queue.addAll(subCategories);
        }
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

    public Multimap<Category, Category> getCategories() {
        return this.categories;
    }

    public Collection<Exercise> getExerciseByCategory(Category category) {
        if (this.exercises.get(category).isEmpty()) {
            this.exercises.putAll(category, this.service.getExercisesByCategory(category));
        }

        return this.exercises.get(category);
    }

    public static synchronized ViewDataCache getInstance() {
        if (instance == null) {
            instance = new ViewDataCache();
            instance.doAllInit();
        }
        return instance;
    }

}
