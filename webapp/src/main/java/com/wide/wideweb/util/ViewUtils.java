package com.wide.wideweb.util;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;

/**
 * 
 * @author Attila Cs.
 * 
 */
public class ViewUtils {

    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String MAIN = "";
    public static final String ERROR = "error";

    public static void navigateToErrorView(VaadinSession session, ViewChangeEvent event, Exception e) {
        session.setAttribute("unhandled.exception", e);
        event.getNavigator().navigateTo(ViewUtils.ERROR);
    }

}
