package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.wideweb.util.ViewUtils;

public class HandleFilterWelcome implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = -5725299103519806624L;

    private final CustomLayout layout;

    public HandleFilterWelcome(CustomLayout layout) {
        this.layout = layout;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        String searchText = arguments.getString(0);
        this.layout.replaceComponent(this.layout.getComponent("secondaryLevel"), ViewUtils.searchAll(searchText));
        JavaScript.getCurrent().execute("window.$(\"body\").removeClass(\"welcome\");window.$( \".searchInput\" ).trigger(\"click\");");
        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
    }
}
