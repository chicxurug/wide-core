package com.wide.wideweb.views;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.wide.domainmodel.Exercise;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

@Component
@Scope("prototype")
@VaadinView(ViewUtils.VIEW_EXERCISE)
public class ExerciseView extends Panel implements View {

    /**
     * 
     */
    private static final long serialVersionUID = -3001891325956515469L;

    private static Exercise currentEx;

    @Override
    public void enter(ViewChangeEvent event) {
        String param = event.getParameters();
        ViewDataCache cache = ViewDataCache.getInstance();
        if (param != null && !param.isEmpty()) {
            currentEx = cache.getExerciseByCategory(MainView.current, param);
        }
        CustomLayout layout = new CustomLayout("3");
        layout.addComponent(ViewUtils.getBreadCrump(currentEx.getCategory()), "crumb");
        layout.addComponent(new Label(
                "<div class=\"title\"><div class=\"schoolLevel university inTest\"></div>" + currentEx.getTitle() + "</div>"
                        + "<div class=\"author\">by: <a href=\"#\">" + currentEx.getAuthor() + "</a></div>", ContentMode.HTML), "lessonHeader");
        layout.addComponent(
                new Label(
                        currentEx.getTitle()
                                + "<br/><br/><img width=\"600\" src=\"https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTl-uB5Q21R5wURYxf_N_s7OwnIgS9fPOvdMaquAFM8yq0JQNTokg\"/>",
                        ContentMode.HTML), "lessonDesc");
        layout.addComponent(ViewUtils.getExerciseDetails(currentEx), "lessonDetails");
        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader.js");
        setContent(layout);
    }
}
