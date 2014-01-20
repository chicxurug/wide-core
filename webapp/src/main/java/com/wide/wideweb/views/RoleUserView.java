package com.wide.wideweb.views;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.wide.wideweb.util.ViewUtils;

/**
 * @author Attila Cs.
 */
@Component
@Scope("prototype")
@VaadinView(RoleUserView.NAME)
@PreAuthorize("hasRole('ROLE_USER')")
public class RoleUserView extends Panel implements View
{

    private static final long serialVersionUID = -6596447346940555387L;

    public static final String NAME = "role_user";

    @PostConstruct
    public void PostConstruct()
    {
        setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        layout.addComponent(new Label("ROLE_USER"));
        layout.addComponent(new Link("Go back", new ExternalResource("#!" + ViewUtils.MAIN)));

        setContent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {
    }
}
