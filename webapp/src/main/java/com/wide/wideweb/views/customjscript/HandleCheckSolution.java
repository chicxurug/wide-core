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
import com.wide.domainmodel.Exercise.SolutionType;
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
                                + (compareSolution(answerVars.get(i), submitVars.get(i), currentEx.getType()) ? "green" : "red") + "')");
                JavaScript.getCurrent().execute("window.$(\".solutionBar .yourSolution input[id='var" + (i + 1) + "']\").css('border-width', '2px')");
                JavaScript.getCurrent()
                        .execute(
                                "window.$(\".solutionBar .yourSolution input[id='var" + (i + 1) + "']\").val('" + submitVars.get(i) + " - "
                                        + (compareSolution(answerVars.get(i), submitVars.get(i), currentEx.getType()) ? "Correct" : "Wrong")
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
        for (String answ : answer.substring(1, answer.length() - 1).split("\",")) {
            vars.add(answ.replace("\"", "").replace(" - Correct", "").replace(" - Wrong", ""));
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

    private boolean compareSolution(String answer, String varval, SolutionType type) {
        boolean ret = false;

        if (type == SolutionType.SIMPLE) {
            ret = answer.equals(varval);
        } else if (type == SolutionType.MULTI_CHOICE) {
            String extract1 = answer.toUpperCase().replaceAll("[^A-Z]+", "");
            String extract2 = varval.toUpperCase().replaceAll("[^A-Z]+", "");
            for (int i = 0; i < extract1.length(); i++) {
                if (!extract2.contains(extract1.charAt(i) + "")) {
                    ret = false;
                    break;
                }
                extract2 = extract2.replaceFirst(extract1.charAt(i) + "", "");
            }
            ret = extract2.isEmpty();
        }
        return ret;
    }
}
