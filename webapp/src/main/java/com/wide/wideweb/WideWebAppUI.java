package com.wide.wideweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.wide.wideweb.core.NavigatorFactory;
import com.wide.wideweb.util.ViewUtils;

/**
 * 
 * @author Attila Cs.
 * 
 */
@Theme("wideweb")
@Component
@Scope("prototype")
@JavaScript({ "app://VAADIN/themes/wideweb/js/jquery-2.1.0.js", "app://VAADIN/themes/wideweb/js/jquery-ui.js",
        "http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML" })
@StyleSheet({ "app://VAADIN/themes/wideweb/styles.css", "app://VAADIN/themes/wideweb/css/jquery-ui.css" })
public class WideWebAppUI extends UI implements ErrorHandler
{

    private static final long serialVersionUID = 1L;

    @Autowired
    private transient ApplicationContext applicationContext;

    @Autowired
    private NavigatorFactory navigatorFactory;

    private Navigator navigator;

    @Override
    protected void init(final VaadinRequest request)
    {
        setSizeFull();
        this.navigator = this.navigatorFactory.getNavigator(WideWebAppUI.this, WideWebAppUI.this);
        VaadinSession.getCurrent().setErrorHandler(WideWebAppUI.this);
        String fragment = Page.getCurrent().getLocation().getFragment();
        if (fragment != null && !fragment.isEmpty()) {
            String[] fragParts = fragment.substring(1).split("/");
            if (ViewUtils.VIEW_EXERCISE.equals(fragParts[0])) {
                fragment = "/showExercise=" + fragParts[1];
            } else {
                fragment = "";
            }
        } else {
            fragment = "";
        }

        ViewUtils.setCurrentCategory(null);
        ViewUtils.setCurrentExercise(null);

        this.navigator.navigateTo(ViewUtils.MAIN + fragment);
        this.setNavigator(null);

        com.vaadin.ui.JavaScript.getCurrent().execute("MathJax.Hub.Config({tex2jax: { inlineMath: [['$','$']] }});");
    }

    /**
     * Exception on action
     */
    @Override
    public void error(com.vaadin.server.ErrorEvent event)
    {
        DefaultErrorHandler.doDefault(event);
    }

    @Override
    public void doInit(VaadinRequest request, int uiId) {
        // Workaround to get rid of the double navigation to the main view
        super.doInit(request, uiId);
        this.setNavigator(this.navigator);
    }
}