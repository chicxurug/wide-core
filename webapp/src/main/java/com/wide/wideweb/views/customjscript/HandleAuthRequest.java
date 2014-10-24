package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.security.core.AuthenticationException;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.wide.wideweb.util.SpringSecurityHelper;
import com.wide.wideweb.util.ViewUtils;

public class HandleAuthRequest implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = -2538530866881731533L;

    SpringSecurityHelper authHelper;
    private ViewChangeEvent event;

    public HandleAuthRequest(ViewChangeEvent event, SpringSecurityHelper authHelper) {
        this.event = event;
        this.authHelper = authHelper;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        String uname = arguments.getString(0);
        String pwd = arguments.getString(1);

        try {
            this.authHelper.authenticate(uname, pwd);
            this.event.getNavigator().navigateTo(ViewUtils.MAIN);
        } catch (AuthenticationException e) {
            Notification.show("Error", "bad credential", Notification.Type.ERROR_MESSAGE);
        } catch (Exception e) {
            ViewUtils.navigateToErrorView(UI.getCurrent().getSession(), this.event, e);
        } finally {
            JavaScript.getCurrent().execute("window.$(passwd).val('')");
        }

        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
    }

}
