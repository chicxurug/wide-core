package com.wide.domainmodel;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Test")
public class Test extends Solvable {

    private String description;
    // Exercise point map
    private List<ExercisePoint> exercises;

    public Test() {

    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "TestExercises",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "execisepoint_id")
            )
            public List<ExercisePoint> getExercises() {
        return this.exercises;
    }

    public void setExercises(List<ExercisePoint> exercises) {
        this.exercises = exercises;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ExercisePoint> generateExerciseList() {
        return this.exercises;
    }
}
