package com.wide.wideweb.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Exercise.DifficultyLevel;
import com.wide.domainmodel.Exercise.SchoolLevel;
import com.wide.domainmodel.Feature;
import com.wide.wideweb.util.FeatureFactory;

public class ExerciseBean implements Serializable {

    private static final long serialVersionUID = -3343445526753441596L;

    private String language;
    private String title;
    private Category category;
    private String uploader; // TODO ?
    private String author;
    private String book;
    private String publisher; // TODO ?
    private DifficultyLevel difficulty;
    private SchoolLevel schoolLevel;
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

    public ExerciseBean(Exercise currentExercise) {
        this.language = currentExercise.getLanguage();
        this.title = currentExercise.getTitle();
        this.category = currentExercise.getCategory();
        this.uploader = null;
        this.author = currentExercise.getAuthor();
        this.book = currentExercise.getBookTitle();
        this.publisher = currentExercise.getPublisher();
        this.difficulty = currentExercise.getDifficulty();
        this.schoolLevel = currentExercise.getLevel();
        for (Feature feature : currentExercise.getFeatures()) {
            switch (feature.getName()) {
                case FeatureFactory.TAGS:
                    this.tags = feature.getValue();
                    break;
                case FeatureFactory.EXERCISE_TEXT:
                    this.exerciseText = feature.getValue();
                    break;
                case FeatureFactory.RELEATED_LINKS:
                    this.relatedLinks = feature.getValue();
                    break;
                case FeatureFactory.SHORT_ANSWER:
                    this.shortAnswer = feature.getValue();
                    break;
                case FeatureFactory.SOLUTION_TEXT:
                    this.solutionText = feature.getValue();
                    break;
                default:
                    throw new IllegalStateException("Unrecognized feature name: " + feature.getName());
            }
        }
    }

    public Exercise convert() {
        // TODO maybe this can be replaced by -> return convertFromPrevious(new Exercise());
        Exercise ret = new Exercise(this.language, this.title, this.difficulty, null, this.author, this.schoolLevel, this.publisher, this.book,
                this.category, null);
        List<Feature> features = new ArrayList<Feature>();
        if (StringUtils.isNotBlank(this.tags)) {
            Feature tags = FeatureFactory.createTags(this.tags);
            features.add(tags);
        }
        Feature exerciseText = FeatureFactory.createExerciseText(this.exerciseText);
        features.add(exerciseText);
        if (StringUtils.isNotBlank(this.relatedLinks)) {
            Feature relatedLinks = FeatureFactory.createReleatedLinks(this.relatedLinks);
            features.add(relatedLinks);
        }
        Feature shortAnswer = FeatureFactory.createShortAnswer(this.shortAnswer);
        features.add(shortAnswer);
        if (StringUtils.isNotBlank(this.solutionText)) {
            Feature solutionText = FeatureFactory.createSolutionText(this.solutionText);
            features.add(solutionText);
        }
        ret.setFeatures(features);
        return ret;
    }

    public Exercise convertFromPrevious(Exercise prev) {
        final Exercise ret = new Exercise(prev);
        ret.setLanguage(this.language);
        ret.setTitle(this.title);
        ret.setDifficulty(this.difficulty);
        ret.setAuthor(this.author);
        ret.setLevel(this.schoolLevel);
        ret.setPublisher(this.publisher);
        ret.setBookTitle(this.book);
        ret.setCategory(this.category);
        final List<Feature> features = ret.getFeatures();
        // iterate through old exercise's features and remove those features which were cleared, or update them accordingly.
        Set<String> featurekinds = new HashSet<>(FeatureFactory.FEATUREKINDS);
        for (Iterator<Feature> iterator = features.iterator(); iterator.hasNext();) {
            final Feature feature = iterator.next();
            switch (feature.getName()) {
                case FeatureFactory.TAGS:
                    featurekinds.remove(FeatureFactory.TAGS);
                    if (StringUtils.isNotBlank(this.tags)) {
                        feature.setValue(this.tags);
                    } else {
                        iterator.remove();
                    }
                    break;
                case FeatureFactory.EXERCISE_TEXT:
                    featurekinds.remove(FeatureFactory.EXERCISE_TEXT);
                    feature.setValue(this.exerciseText);
                    break;
                case FeatureFactory.RELEATED_LINKS:
                    featurekinds.remove(FeatureFactory.RELEATED_LINKS);
                    if (StringUtils.isNotBlank(this.relatedLinks)) {
                        feature.setValue(this.relatedLinks);
                    } else {
                        iterator.remove();
                    }
                    break;
                case FeatureFactory.SHORT_ANSWER:
                    featurekinds.remove(FeatureFactory.SHORT_ANSWER);
                    feature.setValue(this.shortAnswer);
                    break;
                case FeatureFactory.SOLUTION_TEXT:
                    featurekinds.remove(FeatureFactory.SOLUTION_TEXT);
                    if (StringUtils.isNotBlank(this.solutionText)) {
                        feature.setValue(this.solutionText);
                    } else {
                        iterator.remove();
                    }
                    break;
                default:
                    throw new IllegalStateException("Unrecognized feature name: " + feature.getName());
            }
        }
        // check for new attributes, which are not set yet.
        if (featurekinds.contains(FeatureFactory.TAGS) && StringUtils.isNotBlank(this.tags)) {
            Feature tags = FeatureFactory.createTags(this.tags);
            features.add(tags);
        }
        if (featurekinds.contains(FeatureFactory.EXERCISE_TEXT)) {
            Feature exerciseText = FeatureFactory.createExerciseText(this.exerciseText);
            features.add(exerciseText);
        }
        if (featurekinds.contains(FeatureFactory.RELEATED_LINKS) && StringUtils.isNotBlank(this.relatedLinks)) {
            Feature relatedLinks = FeatureFactory.createReleatedLinks(this.relatedLinks);
            features.add(relatedLinks);
        }
        if (featurekinds.contains(FeatureFactory.SHORT_ANSWER)) {
            Feature shortAnswer = FeatureFactory.createShortAnswer(this.shortAnswer);
            features.add(shortAnswer);
        }
        if (featurekinds.contains(FeatureFactory.SOLUTION_TEXT) && StringUtils.isNotBlank(this.solutionText)) {
            Feature solutionText = FeatureFactory.createSolutionText(this.solutionText);
            features.add(solutionText);
        }
        return ret;
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

    public DifficultyLevel getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public SchoolLevel getSchoolLevel() {
        return this.schoolLevel;
    }

    public void setSchoolLevel(SchoolLevel schoolLevel) {
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
