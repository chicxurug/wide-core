package com.wide.wideweb.views.customjscript;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.wide.common.FeatureFactory;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.stat.LogEntry;
import com.wide.wideweb.beans.ExerciseBean;
import com.wide.wideweb.util.SpringSecurityHelper;
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
        Exercise currentEx = ViewUtils.getCurrentExercise();
        if (this.answerOnly) {
            String answer = arguments.getString(0);
            String shortAnswer = ViewUtils.getFeatureValue(currentEx, FeatureFactory.SHORT_ANSWER);
            List<String> answerVars = getVariables(shortAnswer);
            List<String> submitVars = splitAnswer(answer);
            for (int i = 0; i < answerVars.size(); i++) {
                JavaScript.getCurrent().execute(
                        "window.$(\".solutionBar .yourSolution input[id='var" + (i + 1) + "']\").css('border-color', '"
                                + (answerVars.get(i).equals(submitVars.get(i)) ? "green" : "red") + "')");
                JavaScript.getCurrent().execute("window.$(\".solutionBar .yourSolution input[id='var" + (i + 1) + "']\").css('border-width', '2px')");
                JavaScript.getCurrent()
                        .execute(
                                "window.$(\".solutionBar .yourSolution input[id='var" + (i + 1) + "']\").val('" + submitVars.get(i) + " - "
                                        + (answerVars.get(i).equals(submitVars.get(i)) ? "Correct" : "Wrong")
                                        + "');");
            }
            ViewUtils.logEntry(LogEntry.EntryType.SUBMIT_SOLUTION, answer);
        } else {
            if (!SpringSecurityHelper.hasRole("ROLE_USER")) {
                return;
            }
            String solutionText = ViewUtils.getFeatureValue(currentEx, FeatureFactory.SOLUTION_TEXT);
            this.layout.addComponent(new Label(solutionText, ContentMode.HTML), "lessonDesc");
            this.layout.addComponent(ViewUtils.getBreadCrumb(currentEx.getCategory(), currentEx), "crumb");
            ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
            JavaScript.getCurrent().execute("MathJax.Hub.Queue([\"Typeset\",MathJax.Hub]);");
            ViewUtils.logEntry(LogEntry.EntryType.CHECK_SOLUTION);
        }
    }

    private List<String> splitAnswer(String answer) {
        List<String> vars = new ArrayList<String>();
        for (String answ : answer.substring(1, answer.length() - 1).split(",")) {
            vars.add(answ.substring(1, answ.length() - 1).replace(" - Correct", "").replace(" - Wrong", ""));
        }
        return vars;
    }

    private List<String> getVariables(String shortAnswer) {
        List<String> vars = new ArrayList<String>();
        for (String var : shortAnswer.split(ExerciseBean.VAR_SEPARATOR)) {
            if (var.equals(":=")) {
                break;
            }
            vars.add(var.split(":=")[1]);
        }

        return vars;
    }
}
