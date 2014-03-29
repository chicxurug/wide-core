package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.wideweb.util.ViewUtils;

public class HandleCreateExcersize implements JavaScriptFunction {

    private static final long serialVersionUID = -1429465449278962069L;

    private Navigator navigator;

    public HandleCreateExcersize(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        this.navigator.navigateTo(ViewUtils.CREATE_EXERCISE);
    }

}
