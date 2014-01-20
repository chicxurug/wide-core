package com.wide.wideweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.ErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import com.wide.wideweb.core.NavigatorFactory;

/**
 * 
 * @author Attila Cs.
 * 
 */
@Component
@Scope("prototype")
public class WideWebAppUI extends UI implements ErrorHandler
{

    private static final long serialVersionUID = 1L;

    @Autowired
    private transient ApplicationContext applicationContext;

    @Autowired
    private NavigatorFactory navigatorFactory;

    @Override
    protected void init(final VaadinRequest request)
    {
        setSizeFull();
        this.navigatorFactory.getNavigator(WideWebAppUI.this, WideWebAppUI.this);
        VaadinSession.getCurrent().setErrorHandler(WideWebAppUI.this);
    }

    /**
     * Exception on action
     */
    @Override
    public void error(com.vaadin.server.ErrorEvent event)
    {
        DefaultErrorHandler.doDefault(event);
    }
}
