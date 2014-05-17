package com.wide.wideweb.util;

import com.google.common.collect.ImmutableSet;
import com.wide.domainmodel.Feature;
import com.wide.domainmodel.Feature.FeatureType;

public class FeatureFactory {

    public static final String EXERCISE_TEXT = "Exercise";
    public static final String RELEATED_LINKS = "Releated links";
    public static final String SHORT_ANSWER = "Answer";
    public static final String SOLUTION_TEXT = "Solution";
    public static final String TAGS = "Tags";

    public static final ImmutableSet<String> FEATUREKINDS = ImmutableSet.of(EXERCISE_TEXT, RELEATED_LINKS, SHORT_ANSWER, SOLUTION_TEXT, TAGS);

    public static Feature createExerciseText(String value) {
        return new Feature(EXERCISE_TEXT, value, FeatureType.LONG_TEXT);
    }

    public static Feature createShortAnswer(String value) {
        return new Feature(SHORT_ANSWER, value, FeatureType.VERIFYABLE_STEP);
    }

    public static Feature createSolutionText(String value) {
        return new Feature(SOLUTION_TEXT, value, FeatureType.LONG_TEXT);
    }

    public static Feature createReleatedLinks(String value) {
        return new Feature(RELEATED_LINKS, value, FeatureType.LONG_TEXT);
    }

    public static Feature createTags(String value) {
        return new Feature(TAGS, value, FeatureType.TAG);
    }

}
