package com.wide.domainmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Test")
public class Test extends Solvable {

    private String description;
    // Exercise point map
    Map<Exercise, Point> exercises;

    public Test() {

    }

    @ManyToMany
    @JoinTable(name = "ExercisePoints")
    public Map<Exercise, Point> getExercises() {
        return this.exercises;
    }

    public void setExercises(Map<Exercise, Point> exercises) {
        this.exercises = exercises;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Exercise> generateExerciseList() {
        return new ArrayList<Exercise>(this.exercises.keySet());
    }
}
