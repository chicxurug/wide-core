package com.wide.wideweb.views;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.wide.domainmodel.Exercise.SchoolLevel;
import com.wide.domainmodel.user.Group;
import com.wide.domainmodel.user.User;
import com.wide.persistence.PersistenceListener;
import com.wide.service.WideService;
import com.wide.wideweb.beans.UserProfileBean;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

@Component
@Scope("prototype")
@VaadinView(ViewUtils.REGISTER)
public class RegisterView extends Panel implements View {

    /**
     * 
     */
    private static final long serialVersionUID = 5002015558694341506L;
    private final WideService service = new WideService(PersistenceListener.getEntityManagerFactory());

    private static final Logger logger = LoggerFactory.getLogger(RegisterView.class);

    private FormLayout registerLayout = new FormLayout();
    private UserProfileBean current = new UserProfileBean();
    protected final BeanFieldGroup<UserProfileBean> form = new BeanFieldGroup<UserProfileBean>(UserProfileBean.class);

    @Override
    public void enter(ViewChangeEvent event) {
        this.form.setItemDataSource(this.current);
        final Navigator navigator = event.getNavigator();
        setContent(this.registerLayout);

        final TextField uname = new TextField("Username");
        this.registerLayout.addComponent(uname);
        uname.setWidth("10%");
        uname.setNullRepresentation("");
        uname.setRequired(true);
        uname.setRequiredError("Username cannot be empty.");
        this.form.bind(uname, "username");

        final PasswordField pwd = new PasswordField("Your password");
        this.registerLayout.addComponent(pwd);
        pwd.setWidth("10%");
        pwd.setNullRepresentation("");
        pwd.setRequired(true);
        this.form.bind(pwd, "password");

        final PasswordField pwd2 = new PasswordField("Confirm password");
        this.registerLayout.addComponent(pwd2);
        pwd2.setWidth("10%");
        pwd2.setNullRepresentation("");
        pwd2.setRequired(true);

        final TextField name = new TextField("Your name");
        this.registerLayout.addComponent(name);
        name.setWidth("30%");
        name.setNullRepresentation("");
        name.setRequired(true);
        name.setRequiredError("The name cannot be empty.");
        this.form.bind(name, "name");

        final DateField bdate = new DateField("Date of birth");
        this.registerLayout.addComponent(bdate);
        bdate.setValue(new Date());
        bdate.setRequired(true);
        this.form.bind(bdate, "date_of_birth");

        final TextField email = new TextField("Your e-mail address");
        this.registerLayout.addComponent(email);
        email.setWidth("10%");
        email.setNullRepresentation("");
        email.setRequired(true);
        this.form.bind(email, "email");

        BeanItemContainer<String> genderitems = new BeanItemContainer<String>(String.class, Arrays.asList(new String[] { "Female", "Male" }));
        final OptionGroup gendergr = new OptionGroup("Gender", genderitems);
        this.registerLayout.addComponent(gendergr);
        gendergr.setNullSelectionAllowed(true);
        gendergr.setRequired(true);
        this.form.bind(gendergr, "gender");

        BeanItemContainer<SchoolLevel> slitems = new BeanItemContainer<SchoolLevel>(SchoolLevel.class, Arrays.asList(SchoolLevel.values()));
        final OptionGroup schoolLevel = new OptionGroup("Education level", slitems);
        schoolLevel.setItemCaptionMode(ItemCaptionMode.PROPERTY);
        schoolLevel.setItemCaptionPropertyId("description");
        this.registerLayout.addComponent(schoolLevel);
        schoolLevel.setNullSelectionAllowed(true);
        schoolLevel.setRequired(true);
        this.form.bind(schoolLevel, "edu_level");

        final TextArea interests = new TextArea("Topics of interest");
        this.registerLayout.addComponent(interests);
        interests.setWidth("30%");
        this.form.bind(interests, "interests");

        final Button btn_reg = new Button("Register");
        btn_reg.addClickListener(new Button.ClickListener() {

            /**
             * 
             */
            private static final long serialVersionUID = -1262741709705417829L;

            @Override
            public void buttonClick(ClickEvent event) {
                // check for invalid fields
                for (Field<?> field : RegisterView.this.form.getFields()) {
                    try {
                        field.validate();
                        ((AbstractComponent) field).setComponentError(null);
                    } catch (InvalidValueException ex) {
                        Notification.show(ex.getMessage());
                        ((AbstractComponent) field).setComponentError(new UserError(ex.getMessage()));
                        return; // display only one error at a time
                    }
                }
                ViewDataCache cache = ViewDataCache.getInstance();
                List<User> users = cache.getUsers();
                for (User u : users) {
                    if (u.getUsername().equals(uname.getValue())) {
                        Notification.show("Error", "username already exists", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                }
                try {
                    if (!pwd.getValue().equals(pwd2.getValue())) {
                        Notification.show("Error", "passwords do not match", Notification.Type.ERROR_MESSAGE);
                        return;
                    }
                    RegisterView.this.form.commit();
                    User newUser = RegisterView.this.current.getAccount();
                    Group group = RegisterView.this.service.getOrCreateGroup("Registered user");
                    group.getMembers().add(newUser);
                    RegisterView.this.service.saveOrUpdateGroup(group);
                    Notification.show("Account created!");
                    ViewDataCache.getInstance().initUsers();
                    ViewDataCache.getInstance().initGroups();
                    navigator.navigateTo(ViewUtils.LOGIN);
                } catch (CommitException e) {
                    logger.error("Error during commiting newly created user.", e);
                    Notification.show("Fatal error. :(");
                }
            }
        });
        this.registerLayout.addComponent(btn_reg);
        this.registerLayout.addComponent(new Link("Go back", new ExternalResource("#!" + ViewUtils.MAIN)));
    }
}
