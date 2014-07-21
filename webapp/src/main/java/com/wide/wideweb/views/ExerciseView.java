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
import com.wide.wideweb.views.customjscript.HandleMenuSelect;
import com.wide.wideweb.views.customjscript.HandleSubMenuSelectEx;

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
        layout.addComponent(ViewUtils.getCategoryList(cache.getRootCategory(), "#!" + ViewUtils.VIEW_EXERCISE), "mainMenuItems");
        layout.addComponent(ViewUtils.getCategoryList(cache.getRootCategory(), "#!" + ViewUtils.VIEW_EXERCISE), "subMenuItems");
        layout.addComponent(
                new Label(
                        "<div class=\"title\"><div class=\"schoolLevel " + currentEx.getLevel().getDescription().replaceAll(" ", "_") + "\"></div>"
                                + currentEx.getTitle() + "</div>"
                                + "<div class=\"author\">by: " + ViewUtils.getSeparatedLinks("uploader", currentEx.getUploader()) + "</div>", ContentMode.HTML),
                "lessonHeader");
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
                        String shortURL = l;
                        String absoluteURL = l;
                        if (l.length() > 45) {
                            shortURL = l.substring(0, 45) + "...";
                        }
                        if (!absoluteURL.contains("//")) {
                            absoluteURL = "http://" + absoluteURL;
                        }
                        sb.append("<li><img src=\"http://www.google.com/s2/favicons?domain_url=" + l + "\"> <a href=\"" + absoluteURL + "\" target=\"_blank\""
                                + (l.equals(shortURL) ? "" : " title=\"" + l + "\"") + ">" + shortURL
                                + "</a></li>");
                    }
                }
            }
        }
        sb.append("</ul>");
        layout.addComponent(new Label(exDesc, ContentMode.HTML), "lessonDesc");
        layout.addComponent(new Label(sb.toString(), ContentMode.HTML), "links");
        layout.addComponent(new Label("<p style=\"padding-top:34px; color: white; text-align: center; font-family: education;\">" + cache.getUsername()
                + "</p>", ContentMode.HTML), "auth_user");

        setContent(layout);
        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader.js");

        JavaScript.getCurrent().removeFunction("com_wide_wideweb_checkAnswer");
        JavaScript.getCurrent().removeFunction("com_wide_wideweb_checkSolution");
        JavaScript.getCurrent().removeFunction("com_wide_wideweb_subMenuSelect");
        JavaScript.getCurrent().removeFunction("com_wide_wideweb_menuSelect");

        JavaScript.getCurrent().addFunction("com_wide_wideweb_checkAnswer", new HandleCheckSolution(layout, true));
        JavaScript.getCurrent().addFunction("com_wide_wideweb_checkSolution", new HandleCheckSolution(layout, false));
        JavaScript.getCurrent().addFunction("com_wide_wideweb_subMenuSelect", new HandleSubMenuSelectEx(event.getNavigator(), layout));
        JavaScript.getCurrent().addFunction("com_wide_wideweb_menuSelect", new HandleMenuSelect(layout));
    }
}
