package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.ui.JavaScriptFunction;
import com.wide.wideweb.util.ViewUtils;

public class HandleAddToCart implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = 3884526814360926675L;

    @Override
    public void call(JSONArray arguments) throws JSONException {
        ViewUtils.addToCart();
    }

}
