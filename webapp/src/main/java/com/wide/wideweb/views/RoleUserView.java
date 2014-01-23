package com.wide.wideweb.views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Feature;
import com.wide.persistence.PersistenceListener;
import com.wide.service.WideService;
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
        final VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setMargin(true);

        layout.addComponent(new Label("ROLE_USER"));
        layout.addComponent(new Link("Go back", new ExternalResource("#!" + ViewUtils.MAIN)));

        setContent(layout);

        Button button = new Button("List exercises");
        button.addClickListener(new Button.ClickListener() {

            /**
             * 
             */
            private static final long serialVersionUID = -1875094823281557158L;

            @Override
            public void buttonClick(ClickEvent event) {
                WideService service = new WideService(PersistenceListener.getEntityManagerFactory());
                List<Exercise> exercises = service.getExercises();
                for (Exercise e : exercises) {
                    layout.addComponent(new Label("Exercise: " + e.getTitle() + ", diff: " + e.getDifficulty() + ", category: " + e.getCategory().getName()));
                    for (Feature f : e.getFeatures()) {
                        layout.addComponent(new Label(f.getName() + " => " + f.getValue()));
                    }
                }
            }
        });
        layout.addComponent(button);
        final TextField title = new TextField();
        final TextField diff = new TextField();
        Button button2 = new Button("Add exercises");
        button2.addClickListener(new Button.ClickListener() {

            /**
             * 
             */
            private static final long serialVersionUID = -4576816989032659552L;

            @Override
            public void buttonClick(ClickEvent event) {
                WideService service = new WideService(PersistenceListener.getEntityManagerFactory());
                Category sample = service.getOrCreateCategory("sample", null);
                List<Feature> features = new ArrayList<Feature>();
                features.add(service.saveOrUpdateFeature(new Feature("aa", "bb", Feature.FEATURE_TYPES.SHORT_TEXT)));
                features.add(service.saveOrUpdateFeature(new Feature("aa2", "bb2", Feature.FEATURE_TYPES.LONG_TEXT)));
                service.saveOrUpdateExercise(new Exercise(title.getValue(), Integer.parseInt(diff.getValue()), 0, "", Exercise.SCHOOL_LEVELS.ELEMENTARY, "",
                        sample, features));
            }
        });
        layout.addComponent(button2);
        layout.addComponent(title);
        layout.addComponent(diff);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {
    }
}
