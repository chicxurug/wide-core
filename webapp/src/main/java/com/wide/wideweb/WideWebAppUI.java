package com.wide.wideweb;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Feature;
import com.wide.persistence.PersistenceListener;
import com.wide.service.WideService;

@SuppressWarnings("serial")
@Theme("wideweb")
public class WideWebAppUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = WideWebAppUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        Button button = new Button("List exercises");
        button.addClickListener(new Button.ClickListener() {

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

}