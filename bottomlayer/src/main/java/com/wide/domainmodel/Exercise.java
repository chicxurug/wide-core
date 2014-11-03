package com.wide.domainmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class Exercise extends Solvable implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum DifficultyLevel {
        D1_EASY(1, "Easy to solve"),
        D2_AVERAGE(2, "Average"),
        D3_FAIR(3, "Fair"),
        D4_CHALLENGING(4, "Challenging"),
        D5_HARD(5, "Hard to solve");

        private final int difficulty;
        private final String description;

        DifficultyLevel(int difficulty, String description) {
            this.difficulty = difficulty;
            this.description = description;
        }

        public int getDifficulty() {
            return this.difficulty;
        }

        public String getDescription() {
            return this.description;
        }

        @Override
        public String toString() {
            return this.difficulty + " - " + this.description;
        }

        public static DifficultyLevel randomDifficulty() {
            return values()[new Random().nextInt(values().length)];
        }

    }

    public enum SchoolLevel {
        ELEMENTARY("Elementary"),
        HIGHSCHOOL("High School"),
        COLLEGE("University"),
        OTHER("Other");

        private final String description;

        SchoolLevel(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }

    public enum SolutionType {
        SIMPLE("Simple"),
        MULTI_CHOICE("Multi choice");

        private final String description;

        SolutionType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }

    private String language;
    private String title;
    private String uploader;
    private DifficultyLevel difficulty;
    private Integer score;
    private String author;
    private SchoolLevel level;
    private String publisher;
    private String bookTitle;
    private Boolean testMember;
    private Integer numberOfSubmits;
    private Category category;
    private List<Feature> features;
    private SolutionType type;

    public Exercise() {

    }

    public Exercise(String language, String title, String uploader, DifficultyLevel difficulty, Integer score, String author, SchoolLevel level,
            String publisher,
            String bookTitle, Category category, List<Feature> features, SolutionType type) {
        this.language = language;
        this.title = title;
        this.uploader = uploader;
        this.difficulty = difficulty;
        this.score = score;
        this.author = author;
        this.level = level;
        this.publisher = publisher;
        this.bookTitle = bookTitle;
        this.testMember = false;
        this.numberOfSubmits = 0;
        this.category = category;
        this.features = features;
        this.type = type;
    }

    // copy constructor
    public Exercise(Exercise prev) {
        // BeanUtils.copyProperties(prev, this, new String[] { "features" });
        this.id = prev.id;
        this.language = prev.language;
        this.title = prev.title;
        this.uploader = prev.uploader;
        this.difficulty = prev.difficulty;
        this.score = prev.score;
        this.author = prev.author;
        this.level = prev.level;
        this.publisher = prev.publisher;
        this.bookTitle = prev.bookTitle;
        this.testMember = prev.testMember;
        this.numberOfSubmits = prev.numberOfSubmits;
        this.category = prev.category;
        this.features = new ArrayList<>();
        if (prev.features != null) {
            for (Feature feature : prev.features) {
                this.features.add(new Feature(feature));
            }
        }
        this.type = prev.type;
    }

    @Column(nullable = false)
    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Column(nullable = false)
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(nullable = false)
    public DifficultyLevel getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
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

    // @Column(nullable = false)
    public Integer getScore() {
        return this.score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    // @Column(nullable = false)
    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // @Column(nullable = false)
    public SchoolLevel getLevel() {
        return this.level;
    }

    public void setLevel(SchoolLevel level) {
        this.level = level;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    // @Column(nullable = false)
    public String getBookTitle() {
        return this.bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Boolean getTestMember() {
        return this.testMember;
    }

    public void setTestMember(Boolean testMember) {
        this.testMember = testMember;
    }

    public Integer getNumberOfSubmits() {
        return this.numberOfSubmits;
    }

    public void setNumberOfSubmits(Integer numberOfSubmits) {
        this.numberOfSubmits = numberOfSubmits;
    }

    @Column(nullable = false)
    public String getUploader() {
        return this.uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public SolutionType getType() {
        return this.type;
    }

    public void setType(SolutionType type) {
        this.type = type;
    }
}
