package com.wide.wideweb.views;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Image;
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
@VaadinView(RoleAdminView.NAME)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RoleAdminView extends Panel implements View
{

    private static final long serialVersionUID = 7356196328500987090L;

    public static final String NAME = "role_admin";

    @PostConstruct
    public void PostConstruct()
    {
        LoggerFactory.getLogger(this.getClass()).debug("POST");
        setSizeFull();
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        layout.addComponent(new Label("ROLE_ADMIN"));
        Image image = new Image("felejthetetlen:)", new ExternalResource("http://www.taknyosbagoly.hu/uploads/images/ab9115b8b1afadf1ec51220abc28d66f.jpg"));
        layout.addComponent(image);
        layout.addComponent(new Link("Go back", new ExternalResource("#!" + ViewUtils.MAIN)));

        setContent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {
    }
}
