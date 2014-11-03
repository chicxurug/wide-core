package com.wide.wideweb.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.wide.wideweb.util.SpringSecurityHelper;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;
import com.wide.wideweb.views.customjscript.HandleAuthRequest;

/**
 * 
 * @author Attila Cs.
 * 
 */
@Component
@Scope("prototype")
@VaadinView(ViewUtils.LOGIN)
public class LoginView extends Panel implements View {

    private static final long serialVersionUID = -2114781199528566532L;

    @Autowired
    SpringSecurityHelper authHelper;

    @Override
    public void enter(final ViewChangeEvent event) {

        CustomLayout layout = new CustomLayout("login");
        ViewDataCache cache = ViewDataCache.getInstance();
        JavaScript.getCurrent().removeFunction("com_wide_wideweb_handleauth");

        layout.addComponent(new Label("<p style=\"padding-top:34px; color: white; text-align: center; font-family: education;\">" + cache.getUsername()
                + "</p>", ContentMode.HTML), "auth_user");

        JavaScript.getCurrent().addFunction("com_wide_wideweb_handleauth", new HandleAuthRequest(event, this.authHelper));
        JavaScript.getCurrent().execute("window.$(\"input[id='uname']\").focus();");

        setContent(layout);

    }
}
