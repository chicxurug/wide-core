package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.domainmodel.stat.LogEntry;
import com.wide.wideweb.util.ViewDataCache;
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
        ViewDataCache cache = ViewDataCache.getInstance();
        ViewUtils.setCurrentExercise(cache.getExerciseByCategory(ViewUtils.getCurrentCategory(), exId));
        if (ViewUtils.getCurrentCategory() == null) {
            ViewUtils.setCurrentCategory(ViewUtils.getCurrentExercise().getCategory());
        }
        this.navigator.navigateTo(ViewUtils.VIEW_EXERCISE);
        ViewUtils.logEntry(LogEntry.EntryType.VIEW_EXERCISE);
    }
}
