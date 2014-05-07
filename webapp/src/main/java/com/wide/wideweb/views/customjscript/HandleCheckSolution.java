package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.wide.domainmodel.Exercise;
import com.wide.wideweb.util.FeatureFactory;
import com.wide.wideweb.util.ViewUtils;

public class HandleCheckSolution implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = -3684428427509403235L;

    private CustomLayout layout;
    private boolean answerOnly;

    public HandleCheckSolution(CustomLayout layout, boolean answerOnly) {
        this.layout = layout;
        this.answerOnly = answerOnly;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        String answer = arguments.getString(0);
        Exercise currentEx = ViewUtils.getCurrentExercise();
        if (this.answerOnly) {
            String shortAnswer = ViewUtils.getFeatureValue(currentEx, FeatureFactory.SHORT_ANSWER);
            this.layout.addComponent(new Label(shortAnswer.equals(answer) ? "Good answer" : "Bad answer", ContentMode.HTML), "lessonDesc");
        } else {
            String solutionText = ViewUtils.getFeatureValue(currentEx, FeatureFactory.SOLUTION_TEXT);
            this.layout.addComponent(new Label(solutionText, ContentMode.HTML), "lessonDesc");
        }
    }
}
