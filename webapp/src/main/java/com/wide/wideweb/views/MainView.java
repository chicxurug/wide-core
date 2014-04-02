package com.wide.wideweb.views;

import java.util.ArrayList;
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
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.wide.domainmodel.Category;
import com.wide.wideweb.util.SpringSecurityHelper;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;
import com.wide.wideweb.views.customjscript.HandleCreateExcersize;
import com.wide.wideweb.views.customjscript.HandleFilterCategory;
import com.wide.wideweb.views.customjscript.HandleFilterWelcome;
import com.wide.wideweb.views.customjscript.HandleLogin;
import com.wide.wideweb.views.customjscript.HandleMenuSelect;
import com.wide.wideweb.views.customjscript.HandleSubMenuSelect;

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

    private Label usernameLabel = new Label("Guest");
    private Label rolesLabel = new Label();

    public static Category current;

    @PostConstruct
    public void PostConstruct()
    {
        final CustomLayout layout = new CustomLayout("2");

        JavaScript.getCurrent().execute("window.$(\"body\").addClass(\"welcome\");");
        layout.addComponent(ViewUtils.getCategoryList(this.cache.getRootCategory()), "mainMenuItems");
        layout.addComponent(ViewUtils.getCategoryList(this.cache.getRootCategory()), "subMenuItems");
        layout.addComponent(new Label("", ContentMode.HTML), "crumb");
        layout.addComponent(ViewUtils.getSecondaryLevel(this.cache.getRootCategory(), new com.wide.wideweb.util.EmptyFilter()), "secondaryLevel");
        layout.addComponent(this.usernameLabel, "auth_user");
        setContent(layout);
        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/welcomeAnimation.js", "/VAADIN/themes/wideweb/js/subHeader.js");

        JavaScript.getCurrent().addFunction("com.wide.wideweb.menuSelect", new HandleMenuSelect(layout));

        JavaScript.getCurrent().addFunction("com.wide.wideweb.subMenuSelect", new HandleSubMenuSelect(layout));

        JavaScript.getCurrent().addFunction("com.wide.wideweb.filterCategory", new HandleFilterCategory(layout));

        JavaScript.getCurrent().addFunction("com.wide.wideweb.filterWelcome", new HandleFilterWelcome(layout));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event)
    {

        JavaScript.getCurrent().addFunction("com.wide.wideweb.loginSelect", new HandleLogin(event.getNavigator()));
        JavaScript.getCurrent().addFunction("com.wide.wideweb.createExercise", new HandleCreateExcersize(event.getNavigator()));

        if (SpringSecurityHelper.isAuthenticated()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<String> roles = new ArrayList<String>();
            for (GrantedAuthority grantedAuthority : user.getAuthorities())
            {
                roles.add(grantedAuthority.getAuthority());
            }

            this.usernameLabel.setValue(user.getUsername());
            this.rolesLabel.setValue(StringUtils.join(roles, ","));
            this.cache.setUserName(user.getUsername());
        } else {
            this.usernameLabel.setValue("Guest");
            this.rolesLabel.setValue("");
            this.cache.setUserName("Guest");
        }
    }
}
