package com.wide.wideweb.beans;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.wide.common.FeatureFactory;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Exercise.DifficultyLevel;
import com.wide.domainmodel.Exercise.SchoolLevel;
import com.wide.domainmodel.Feature;

public class ExerciseBean implements Serializable {

    private static final long serialVersionUID = -3343445526753441596L;

    public static final String VAR_SEPARATOR = "#";

    private String language;
    private String title;
    private Category category;
    private String uploader;
    private String author;
    private String book;
    private String publisher;
    private DifficultyLevel difficulty;
    private SchoolLevel schoolLevel;
    private String tags;
    private String exerciseText;
    private String relatedLinks;
    private String solutionText;

    // Short answer variables
    private String varName_1;
    private String varName_2;
    private String varName_3;
    private String varName_4;
    private String varVal_1;
    private String varVal_2;
    private String varVal_3;
    private String varVal_4;

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
        this.solutionText = null;
        this.varName_1 = "";
        this.varName_2 = "";
        this.varName_3 = "";
        this.varName_4 = "";
        this.varVal_1 = "";
        this.varVal_2 = "";
        this.varVal_3 = "";
        this.varVal_4 = "";
    }

    public ExerciseBean(Exercise currentExercise) {
        this.language = currentExercise.getLanguage();
        this.title = currentExercise.getTitle();
        this.category = currentExercise.getCategory();
        this.uploader = currentExercise.getUploader();
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
                    String variableString[] = feature.getValue().split(VAR_SEPARATOR);
                    for (int i = 0; i < variableString.length; i++) {
                        try {
                            Method setVarName = this.getClass().getMethod("setVarName_" + (i + 1), String.class);
                            Method setVarVal = this.getClass().getMethod("setVarVal_" + (i + 1), String.class);
                            setVarName.invoke(this, (variableString[i].split(":=").length < 2 ? "" : variableString[i].split(":=")[0]));
                            setVarVal.invoke(this, (variableString[i].split(":=").length < 2 ? "" : variableString[i].split(":=")[1]));
                        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                            this.varName_1 = this.varVal_1 = null;
                            this.varName_2 = this.varVal_2 = null;
                            this.varName_3 = this.varVal_3 = null;
                            this.varName_4 = this.varVal_4 = null;
                        }
                    }
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
        return convertFromPrevious(new Exercise());
    }

    public Exercise convertFromPrevious(Exercise prev) {
        final Exercise ret = new Exercise(prev);
        ret.setLanguage(this.language);
        ret.setTitle(this.title);
        ret.setUploader(this.uploader);
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
                    feature.setValue(this.varName_1 + ":=" + this.varVal_1 + VAR_SEPARATOR + this.varName_2 + ":=" + this.varVal_2 + VAR_SEPARATOR
                            + this.varName_3 + ":=" + this.varVal_3 + VAR_SEPARATOR + this.varName_4 + ":=" + this.varVal_4);
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
            Feature shortAnswer = FeatureFactory.createShortAnswer(this.varName_1 + ":=" + this.varVal_1 + VAR_SEPARATOR + this.varName_2 + ":="
                    + this.varVal_2 + VAR_SEPARATOR
                    + this.varName_3 + ":=" + this.varVal_3 + VAR_SEPARATOR + this.varName_4 + ":=" + this.varVal_4);
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

    public String getSolutionText() {
        return this.solutionText;
    }

    public void setSolutionText(String solutionText) {
        this.solutionText = solutionText;
    }

    public String getVarName_1() {
        return this.varName_1;
    }

    public void setVarName_1(String varName_1) {
        this.varName_1 = varName_1;
    }

    public String getVarName_2() {
        return this.varName_2;
    }

    public void setVarName_2(String varName_2) {
        this.varName_2 = varName_2;
    }

    public String getVarName_3() {
        return this.varName_3;
    }

    public void setVarName_3(String varName_3) {
        this.varName_3 = varName_3;
    }

    public String getVarName_4() {
        return this.varName_4;
    }

    public void setVarName_4(String varName_4) {
        this.varName_4 = varName_4;
    }

    public String getVarVal_1() {
        return this.varVal_1;
    }

    public void setVarVal_1(String varVal_1) {
        this.varVal_1 = varVal_1;
    }

    public String getVarVal_2() {
        return this.varVal_2;
    }

    public void setVarVal_2(String varVal_2) {
        this.varVal_2 = varVal_2;
    }

    public String getVarVal_3() {
        return this.varVal_3;
    }

    public void setVarVal_3(String varVal_3) {
        this.varVal_3 = varVal_3;
    }

    public String getVarVal_4() {
        return this.varVal_4;
    }

    public void setVarVal_4(String varVal_4) {
        this.varVal_4 = varVal_4;
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
        builder.append(this.varName_1 + ":=" + this.varVal_1 + VAR_SEPARATOR + this.varName_2 + ":="
                + this.varVal_2 + VAR_SEPARATOR + this.varName_3 + ":=" + this.varVal_3 + VAR_SEPARATOR
                + this.varName_4 + ":=" + this.varVal_4);
        builder.append(", solutionText=");
        builder.append(this.solutionText);
        builder.append("]");
        return builder.toString();
    }

}
