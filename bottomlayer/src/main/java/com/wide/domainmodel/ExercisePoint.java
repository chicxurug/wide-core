package com.wide.domainmodel;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ExercisePoint")
public class ExercisePoint {

    private Long id;
    private Exercise exercise;
    private Long point;

    public ExercisePoint() {

    }

    public ExercisePoint(Exercise exercise, Long point) {
        this.exercise = exercise;
        this.point = point;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    public Exercise getExercise() {
        return this.exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public Long getPoint() {
        return this.point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }
}
