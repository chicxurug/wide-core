package com.wide.wideweb.util;

import com.wide.domainmodel.Feature;
import com.wide.domainmodel.Feature.FeatureType;

public class FeatureFactory {

    private static final String EXERCISE_TEXT = "Exercise";
    private static final String RELEATED_LINKS = "Releated links";
    private static final String SHORT_ANSWER = "Answer";
    private static final String SOLUTION_TEXT = "Solution";

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
        return new Feature("Tags", value, FeatureType.TAG);
    }

}
