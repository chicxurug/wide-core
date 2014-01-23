package com.wide.domainmodel;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Exercise")
public class Exercise extends Solvable {

    public enum SCHOOL_LEVELS {
        ELEMENTARY,
        HIGHSCHOOL,
        COLLEGE
    }

    private String title;
    private Integer difficulty;
    private Integer score;
    private String author;
    private SCHOOL_LEVELS level;
    private String publisher;
    private String book_title;
    private Boolean test_member;
    private Integer numberOfSubmits;
    private Category category;
    private List<Feature> features;

    public Exercise() {

    }

    public Exercise(String title, Integer difficulty, Integer score, String author, SCHOOL_LEVELS level, String book_title, Category category,
            List<Feature> features) {
        this.title = title;
        this.difficulty = difficulty;
        this.score = score;
        this.author = author;
        this.level = level;
        this.book_title = book_title;
        this.test_member = false;
        this.numberOfSubmits = 0;
        this.category = category;
        this.features = features;
    }

    @Column(nullable = false)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = false)
    public Integer getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "ExerciseFeatures",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
            )
            public List<Feature> getFeatures() {
        return this.features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    @Column(nullable = false)
    public Integer getScore() {
        return this.score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Column(nullable = false)
    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Column(nullable = false)
    public SCHOOL_LEVELS getLevel() {
        return this.level;
    }

    public void setLevel(SCHOOL_LEVELS level) {
        this.level = level;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Column(nullable = false)
    public String getBook_title() {
        return this.book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public Boolean getTest_member() {
        return this.test_member;
    }

    public void setTest_member(Boolean test_member) {
        this.test_member = test_member;
    }

    public Integer getNumberOfSubmits() {
        return this.numberOfSubmits;
    }

    public void setNumberOfSubmits(Integer numberOfSubmits) {
        this.numberOfSubmits = numberOfSubmits;
    }
}
