package com.wide.wideweb.core;

import org.springframework.context.ApplicationContext;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * 
 * @author Attila Cs.
 * 
 */
public class SpringUIProvider extends UIProvider {

    private static final long serialVersionUID = 8260725512732714707L;

    ApplicationContext ac;

    public SpringUIProvider(ApplicationContext ac) {
        this.ac = ac;
    }

    @Override
    public UI createInstance(UICreateEvent event) {
        return (UI) this.ac.getBean(getUIBeanName(event.getRequest()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        return (Class<? extends UI>) this.ac.getType(getUIBeanName(event.getRequest()));
    }

    @Override
    public boolean isPreservedOnRefresh(UICreateEvent event) {
        if (this.ac.isPrototype(getUIBeanName(event.getRequest()))) {
            return super.isPreservedOnRefresh(event);
        } else {
            return false;
        }
    }

    private String getUIBeanName(VaadinRequest request) {
        return request.getService().getDeploymentConfiguration().getApplicationOrSystemProperty("beanName", "ui");
    }

}
