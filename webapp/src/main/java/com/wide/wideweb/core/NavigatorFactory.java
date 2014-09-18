package com.wide.wideweb.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;
import com.wide.wideweb.WideWebAppUI;
import com.wide.wideweb.util.SpringSecurityHelper;
import com.wide.wideweb.util.ViewUtils;

/**
 * 
 * @author Attila Cs.
 * 
 */

@Component
public class NavigatorFactory implements ApplicationContextAware, InitializingBean, Serializable {

    private static final long serialVersionUID = 6892954025364945278L;

    private ViewProvider viewProvider;
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {

        String[] beanNames = this.applicationContext.getBeanDefinitionNames();

        Map<String, String> viewNamesToBeanNames = new HashMap<String, String>();
        for (String bean : beanNames) {
            Class<?> clazz = this.applicationContext.getType(bean);
            if (clazz.isAnnotationPresent(VaadinView.class) && View.class.isAssignableFrom(clazz)) {
                VaadinView annot = clazz.getAnnotation(VaadinView.class);
                String viewName = annot.value();
                viewNamesToBeanNames.put(viewName, bean);
            }
        }
        this.viewProvider = new CustomViewProvider(viewNamesToBeanNames);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

    }

    private class ExceptionHandlingNavigator extends Navigator {

        private static final long serialVersionUID = -9040084970129933755L;

        public ExceptionHandlingNavigator(UI ui, SingleComponentContainer container) {
            super(ui, container);
        }

        @Override
        public void navigateTo(String navigationState) {
            addProvider(NavigatorFactory.this.viewProvider);

            try {
                super.navigateTo(navigationState);
            } catch (AuthenticationException e) {
                super.navigateTo(ViewUtils.LOGIN);
//                    } catch (AlreadyLoggedInException e) {
//                        navigateTo(this.errorhandling.accessDeniedAuthenticatedView)
            } catch (AccessDeniedException e) {

                if (SpringSecurityHelper.isAuthenticated()) {
                    super.navigateTo(ViewUtils.MAIN);
                } else {
                    super.navigateTo(ViewUtils.LOGIN);
                }

            }

        }

    }

    private class CustomViewProvider implements ViewProvider {

        private static final long serialVersionUID = -6935736473128422543L;

        Map<String, String> viewNameToBeanName;

        public CustomViewProvider(Map<String, String> viewNameToBeanName) {
            this.viewNameToBeanName = viewNameToBeanName;
        }

        @Override
        public String getViewName(String viewAndParameters) {
            if (viewAndParameters != null) {
                String view = viewAndParameters.split("/")[0];
                if (this.viewNameToBeanName.containsKey(view)) {
                    return view;
                } else {
                    return null;
                }
            } else {
                return null;
            }

        }

        @Override
        public View getView(String viewName) {

            if (this.viewNameToBeanName.get(viewName) == null) {
                return null;
            } else {
                return (View) NavigatorFactory.this.applicationContext.getBean(this.viewNameToBeanName.get(viewName));
            }
        }
    }

    public Navigator getNavigator(WideWebAppUI wideWebAppUI, WideWebAppUI wideWebAppUI2) {
        return new ExceptionHandlingNavigator(wideWebAppUI, wideWebAppUI2);

    }
}
