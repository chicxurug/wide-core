package com.wide.wideweb.views;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

/**
 * 
 * @author Peter Hegedus
 * 
 */
@Component
@Scope("prototype")
@VaadinView(ViewUtils.PROFILE)
@PreAuthorize("hasRole('ROLE_USER')")
public class ProfileView extends Panel implements View {

    /**
     * 
     */
    private static final long serialVersionUID = 8385818412506619730L;

    @Override
    public void enter(ViewChangeEvent event) {
        ViewDataCache cache = ViewDataCache.getInstance();
        CustomLayout layout = new CustomLayout("profile");
        setContent(layout);

        layout.addComponent(new Label("<p style=\"padding-top:34px; color: white; text-align: center; font-family: education;\">" + cache.getUsername()
                + "</p>", ContentMode.HTML), "auth_user");

        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader.js");
    }

}
