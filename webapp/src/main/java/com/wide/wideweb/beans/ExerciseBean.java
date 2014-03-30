package com.wide.wideweb.beans;

import java.io.Serializable;

import com.wide.domainmodel.Category;

public class ExerciseBean implements Serializable {

    private static final long serialVersionUID = -3343445526753441596L;

    private String language;
    private String title;
    private Category category;
    private String uploader;
    private String author;
    private String book;
    private String publisher;
    private String difficulty;
    private String schoolLevel;
    private String tags;
    private String exerciseText;
    private String relatedLinks;
    private String shortAnswer;
    private String solutionText;

    public ExerciseBean() {
        this.language = null;
        this.title = null;
        this.category = null;
        this.uploader = null;
        this.author = null;
        this.book = null;
        this.publisher = null;
        this.difficulty = null;
        this.schoolLevel = null;
        this.tags = null;
        this.exerciseText = null;
        this.relatedLinks = null;
        this.shortAnswer = null;
        this.solutionText = null;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getUploader() {
        return this.uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBook() {
        return this.book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getSchoolLevel() {
        return this.schoolLevel;
    }

    public void setSchoolLevel(String schoolLevel) {
        this.schoolLevel = schoolLevel;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getExerciseText() {
        return this.exerciseText;
    }

    public void setExerciseText(String exerciseText) {
        this.exerciseText = exerciseText;
    }

    public String getRelatedLinks() {
        return this.relatedLinks;
    }

    public void setRelatedLinks(String relatedLinks) {
        this.relatedLinks = relatedLinks;
    }

    public String getShortAnswer() {
        return this.shortAnswer;
    }

    public void setShortAnswer(String shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    public String getSolutionText() {
        return this.solutionText;
    }

    public void setSolutionText(String solutionText) {
        this.solutionText = solutionText;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ExerciseBean [language=");
        builder.append(this.language);
        builder.append(", title=");
        builder.append(this.title);
        builder.append(", category=");
        builder.append(this.category);
        builder.append(", uploader=");
        builder.append(this.uploader);
        builder.append(", author=");
        builder.append(this.author);
        builder.append(", book=");
        builder.append(this.book);
        builder.append(", publisher=");
        builder.append(this.publisher);
        builder.append(", difficulty=");
        builder.append(this.difficulty);
        builder.append(", schoolLevel=");
        builder.append(this.schoolLevel);
        builder.append(", tags=");
        builder.append(this.tags);
        builder.append(", exerciseText=");
        builder.append(this.exerciseText);
        builder.append(", relatedLinks=");
        builder.append(this.relatedLinks);
        builder.append(", shortAnswer=");
        builder.append(this.shortAnswer);
        builder.append(", solutionText=");
        builder.append(this.solutionText);
        builder.append("]");
        return builder.toString();
    }

}
