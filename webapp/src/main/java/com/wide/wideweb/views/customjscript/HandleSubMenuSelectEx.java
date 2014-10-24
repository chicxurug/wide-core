package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.navigator.Navigator;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.wide.common.FeatureFactory;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Feature;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

public class HandleSubMenuSelectEx implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = 4530928329315949330L;

    private final ViewDataCache cache = ViewDataCache.getInstance();

    private Navigator navigator;
    private CustomLayout layout;

    public HandleSubMenuSelectEx(Navigator navigator, CustomLayout layout) {
        this.navigator = navigator;
        this.layout = layout;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        try {
            Thread.sleep(arguments.getInt(1));
            Category selectedCategory = this.cache.getCategoryByName(arguments.getString(0));
            if (selectedCategory.equals(this.cache.getRootCategory())) {
                // The exercise has been selected from the bread crumb, reset the exercise view and do nothing else
                Exercise currentEx = ViewUtils.getCurrentExercise();
                if (currentEx == null) {
                    return;
                }
                this.layout.addComponent(ViewUtils.getBreadCrumb(currentEx.getCategory()), "crumb");
                String exDesc = currentEx.getTitle();
                for (Feature f : currentEx.getFeatures()) {
                    if (FeatureFactory.EXERCISE_TEXT.equals(f.getName())) {
                        exDesc = f.getValue();
                    }
                }
                this.layout.addComponent(new Label(exDesc, ContentMode.HTML), "lessonDesc");
                JavaScript.getCurrent().execute("$solutionDefaultText='Your solution comes here'");
                JavaScript
                        .getCurrent()
                        .execute(
                                "window.$(\".solutionBar .yourSolution > input\").removeAttr(\"style\"); window.$(\".solutionBar .yourSolution > input\").val($solutionDefaultText); window.$(\".solutionBar .yourSolution > input\").addClass(\"default\");");
            } else {
                ViewUtils.setCurrentCategory(selectedCategory);
                this.navigator.navigateTo(ViewUtils.MAIN);
            }
            ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
            JavaScript.getCurrent().execute("MathJax.Hub.Queue([\"Typeset\",MathJax.Hub]);");
        } catch (InterruptedException e) {
            ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
        }

    }

}
