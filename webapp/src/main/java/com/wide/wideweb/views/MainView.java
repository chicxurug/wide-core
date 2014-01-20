package com.wide.wideweb.views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.wide.wideweb.util.SpringSecurityHelper;
import com.wide.wideweb.util.ViewUtils;

/**
 * @author Attila Cs.
 */
@Component
@Scope("prototype")
@VaadinView(ViewUtils.MAIN)
public class MainView extends Panel implements View
{

    private static final long serialVersionUID = 749302556423345956L;

    private Label usernameLabel = new Label();
    private Label rolesLabel = new Label();

    @PostConstruct
    public void PostConstruct()
    {
        setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        HorizontalLayout usernameLayout = new HorizontalLayout();
        usernameLayout.setSpacing(true);
        usernameLayout.addComponent(new Label("Username:"));
        usernameLayout.addComponent(this.usernameLabel);

        HorizontalLayout userRolesLayout = new HorizontalLayout();
        userRolesLayout.setSpacing(true);
        userRolesLayout.addComponent(new Label("Roles:"));
        userRolesLayout.addComponent(this.rolesLabel);

        layout.addComponent(usernameLayout);
        layout.addComponent(userRolesLayout);

        Link userView = new Link("ROLE_USER View (disabled, if user doesn't have access)", new ExternalResource("#!" + RoleUserView.NAME));
        Link roleView = new Link("ROLE_ADMIN View (disabled, if user doesn't have access)", new ExternalResource("#!" + RoleAdminView.NAME));

        userView.setEnabled(SpringSecurityHelper.hasRole("ROLE_USER"));
        roleView.setEnabled(SpringSecurityHelper.hasRole("ROLE_ADMIN"));

        layout.addComponent(userView);
        layout.addComponent(roleView);
        layout.addComponent(new Link("ROLE_ADMIN View (throw exception, if user doesn't have access)", new ExternalResource("#!" + RoleAdminView.NAME)));

        HorizontalLayout loginLayout = new HorizontalLayout();
        loginLayout.setSpacing(true);
        loginLayout.addComponent(new Link("Login", new ExternalResource("#!" + ViewUtils.LOGIN)));
        loginLayout.addComponent(new Link("Logout", new ExternalResource("j_spring_security_logout")));
        layout.addComponent(loginLayout);

        setContent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {
        if (SpringSecurityHelper.isAuthenticated()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<String> roles = new ArrayList<String>();
            for (GrantedAuthority grantedAuthority : user.getAuthorities())
            {
                roles.add(grantedAuthority.getAuthority());
            }

            this.usernameLabel.setValue(user.getUsername());
            this.rolesLabel.setValue(StringUtils.join(roles, ","));
        }
    }
}
