package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.wideweb.util.ViewUtils;

public class HandleOpenExcersize implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = 6523586818390570392L;

    private Navigator navigator;

    public HandleOpenExcersize(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        String exId = arguments.getString(0);
        this.navigator.navigateTo(ViewUtils.VIEW_EXERCISE + "/" + exId);
    }
}
