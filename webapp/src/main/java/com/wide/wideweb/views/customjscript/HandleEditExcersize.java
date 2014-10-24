package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.wideweb.util.ViewUtils;

public class HandleEditExcersize implements JavaScriptFunction {

    private static final long serialVersionUID = -1429465449278962069L;

    private Navigator navigator;

    public HandleEditExcersize(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        // TODO how to find out which exercise is currently on screen?
        if (ViewUtils.getCurrentExercise() == null) {
            return;
        }
        this.navigator.navigateTo(ViewUtils.EDIT_EXERCISE);
    }

}
