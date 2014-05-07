package com.wide.wideweb.views;

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
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Feature;
import com.wide.wideweb.util.FeatureFactory;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;
import com.wide.wideweb.views.customjscript.HandleCheckSolution;

@Component
@Scope("prototype")
@VaadinView(ViewUtils.VIEW_EXERCISE)
public class ExerciseView extends Panel implements View {

    /**
     * 
     */
    private static final long serialVersionUID = -3001891325956515469L;

    @Override
    public void enter(ViewChangeEvent event) {
        ViewDataCache cache = ViewDataCache.getInstance();
        Exercise currentEx = ViewUtils.getCurrentExercise();
        if (currentEx == null) {
            return;
        }
        CustomLayout layout = new CustomLayout("3");
        layout.addComponent(ViewUtils.getBreadCrumb(currentEx.getCategory()), "crumb");
        layout.addComponent(ViewUtils.getCategoryList(cache.getRootCategory(), "!#" + ViewUtils.VIEW_EXERCISE), "mainMenuItems");
        layout.addComponent(ViewUtils.getCategoryList(cache.getRootCategory(), "!#" + ViewUtils.VIEW_EXERCISE), "subMenuItems");
        layout.addComponent(new Label(
                "<div class=\"title\"><div class=\"schoolLevel university inTest\"></div>" + currentEx.getTitle() + "</div>"
                        + "<div class=\"author\">by: <a href=\"#\">" + currentEx.getAuthor() + "</a></div>", ContentMode.HTML), "lessonHeader");
        layout.addComponent(ViewUtils.getExerciseDetails(currentEx), "lessonDetails");
        String exDesc = currentEx.getTitle();
        StringBuilder sb = new StringBuilder("<ul class=\"links\">Links:");
        if (currentEx.getFeatures() != null) {
            for (Feature f : currentEx.getFeatures()) {
                if (FeatureFactory.EXERCISE_TEXT.equals(f.getName())) {
                    exDesc = f.getValue();
                }
                if (FeatureFactory.RELEATED_LINKS.equals(f.getName())) {
                    for (String l : f.getValue().split("\n")) {
                        sb.append("<li><a href=\"" + l + "\">" + l + "</a></li>");
                    }
                }
            }
        }
        sb.append("</ul>");
        layout.addComponent(new Label(exDesc, ContentMode.HTML), "lessonDesc");
        layout.addComponent(new Label(sb.toString(), ContentMode.HTML), "links");

        setContent(layout);
        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader.js");

        JavaScript.getCurrent().removeFunction("com_wide_wideweb_checkAnswer");
        JavaScript.getCurrent().removeFunction("com_wide_wideweb_checkSolution");

        JavaScript.getCurrent().addFunction("com_wide_wideweb_checkAnswer", new HandleCheckSolution(layout, true));
        JavaScript.getCurrent().addFunction("com_wide_wideweb_checkSolution", new HandleCheckSolution(layout, false));
    }
}
