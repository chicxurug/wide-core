package com.wide.wideweb;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ru.xpoft.vaadin.SpringVaadinSystemMessagesProvider;

import com.vaadin.server.Constants;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletService;
import com.wide.wideweb.core.SpringUIProvider;

/**
 * 
 * @author Attila Cs.
 * 
 */
public class SpringVaadinServlet extends VaadinServlet {

    private static final long serialVersionUID = 2866305700516183158L;

    private ApplicationContext applicationContext;
    private String systemMessagesBeanName;
    private final String SYSTEM_MESSAGES_BEAN_NAME_PARAMETER = "systemMessagesBeanName";

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        this.applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletConfig.getServletContext());
        this.systemMessagesBeanName = servletConfig.getInitParameter(this.SYSTEM_MESSAGES_BEAN_NAME_PARAMETER);
        super.init(servletConfig);
    }

    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException {
        VaadinServletService service = super.createServletService(deploymentConfiguration);

        service.setSystemMessagesProvider(new SpringVaadinSystemMessagesProvider(this.applicationContext, this.systemMessagesBeanName));

        if (service.getDeploymentConfiguration().getApplicationOrSystemProperty(Constants.SERVLET_PARAMETER_UI_PROVIDER, "").isEmpty()) {
            service.addSessionInitListener(new SessionInitListener() {

                private static final long serialVersionUID = 4406379077648895488L;

                @Override
                public void sessionInit(SessionInitEvent event) throws ServiceException {
                    event.getSession().addUIProvider(new SpringUIProvider(SpringVaadinServlet.this.applicationContext));

                }
            });

        }
        return service;
    }

}
