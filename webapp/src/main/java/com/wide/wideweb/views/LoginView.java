package com.wide.wideweb.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.wide.wideweb.util.SpringSecurityHelper;
import com.wide.wideweb.util.ViewUtils;

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

        setSizeUndefined();
        CustomLayout layout = new CustomLayout("widelogin");

        final TextField uname = new TextField("");
        final PasswordField pwd = new PasswordField("");
        final Button btn = new Button("Login");
        final Link reg_link = new Link("Register", new ExternalResource("#!" + ViewUtils.REGISTER));
        btn.setClickShortcut(KeyCode.ENTER);

        layout.addComponent(uname, "username");
        layout.addComponent(pwd, "password");
        layout.addComponent(reg_link, "reg_link");
        layout.addComponent(btn, "okbutton");

        uname.focus();
        getSession().setAttribute("unhandled.exception", new Exception("Registration is not supported yet."));

        btn.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = 910486486583904056L;

            @Override
            public void buttonClick(ClickEvent cEvent) {

                try {
                    LoginView.this.authHelper.authenticate(uname.getValue(), pwd.getValue());
                    event.getNavigator().navigateTo(ViewUtils.MAIN);
                } catch (AuthenticationException e) {
                    Notification.show("Error", "bad credential", Notification.Type.ERROR_MESSAGE);
                } catch (Exception e) {
                    ViewUtils.navigateToErrorView(getSession(), event, e);
                } finally {
                    pwd.setValue("");
                }
            }
        });

        setContent(layout);

    }
}
