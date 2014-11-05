package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.wideweb.util.SpringSecurityHelper;
import com.wide.wideweb.util.ViewUtils;

public class HandleLogin implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = 6974549511895301825L;

    private Navigator navigator;

    public HandleLogin(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        String command = arguments.getString(0);
        if ("Log in".equals(command)) {
            this.navigator.navigateTo(ViewUtils.LOGIN);
        }
        if ("Log out".equals(command)) {
            SpringSecurityHelper.unauthenticate();
            ViewUtils.clearSession();
            this.navigator.navigateTo(ViewUtils.MAIN);
        }
        if ("Profile".equals(command)) {
            this.navigator.navigateTo(ViewUtils.PROFILE);
        }
    }
}
