package com.wide.wideweb.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.wide.domainmodel.Category;
import com.wide.wideweb.util.SpringSecurityHelper;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

/**
 * @author Attila Cs.
 * @author kancsuki
 */
@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(ViewUtils.MAIN)
public class MainView extends Panel implements View
{

    private static final long serialVersionUID = 749302556423345956L;

    private final ViewDataCache cache = ViewDataCache.getInstance();

    // TODO @Autowired?
    // private WideService service = new WideService(PersistenceListener.getEntityManagerFactory());

    private Label usernameLabel = new Label();
    private Label rolesLabel = new Label();

    @PostConstruct
    public void PostConstruct()
    {
        CustomLayout layout = new CustomLayout("csuki");

        StringBuilder sb = new StringBuilder();
        final Collection<Category> mainCategories = this.cache.getCategories().get(this.cache.getRootCategory());
        for (Category mainCategory : mainCategories) {
            sb.append("\t");
            sb.append("<li>");
            sb.append("<a href=\"#\" target=\"\">" + mainCategory.getName() + "</a>");
            sb.append("</li>\n");
        }
        Label mainCategoryList = new Label(sb.toString(), ContentMode.HTML);
        layout.addComponent(mainCategoryList, "mainMenuItems");

        // this.selection = new Label("-", ContentMode.HTML);
        // layout.addComponent(this.selection);

        setContent(layout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {
        if (SpringSecurityHelper.isAuthenticated()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<String> roles = new ArrayList<String>();
            for (GrantedAuthority grantedAuthority : user.getAuthorities())
            {
                roles.add(grantedAuthority.getAuthority());
            }

            this.usernameLabel.setValue(user.getUsername());
            this.rolesLabel.setValue(StringUtils.join(roles, ","));
        }
    }
}
