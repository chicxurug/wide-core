package com.wide.wideweb.views;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
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
public class RegisterView extends Panel implements View, JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = 5002015558694341506L;
    private final WideService service = new WideService(PersistenceListener.getEntityManagerFactory());

    private static final Logger logger = LoggerFactory.getLogger(RegisterView.class);

    private CustomLayout registerLayout = new CustomLayout("register");
    private UserProfileBean current = new UserProfileBean();
    protected final BeanFieldGroup<UserProfileBean> form = new BeanFieldGroup<UserProfileBean>(UserProfileBean.class);

    private Navigator navigator;

    private TextField uname;
    private PasswordField pwd;
    private PasswordField pwd2;

    @Override
    public void enter(ViewChangeEvent event) {
        ViewDataCache cache = ViewDataCache.getInstance();

        this.form.setItemDataSource(this.current);
        this.navigator = event.getNavigator();
        setContent(this.registerLayout);

        FormLayout fm = new FormLayout();
        this.uname = new TextField("Username:");
        fm.addComponent(this.uname);
        this.registerLayout.addComponent(fm, "username");
        this.uname.setNullRepresentation("");
        this.uname.setRequired(true);
        this.form.bind(this.uname, "username");
        this.uname.setValue("Username");

        fm = new FormLayout();
        this.pwd = new PasswordField("Your password:");
        fm.addComponent(this.pwd);
        this.registerLayout.addComponent(fm, "password");
        this.pwd.setNullRepresentation("");
        this.pwd.setRequired(true);
        this.form.bind(this.pwd, "password");

        fm = new FormLayout();
        this.pwd2 = new PasswordField("Confirm password:");
        fm.addComponent(this.pwd2);
        this.registerLayout.addComponent(fm, "cpassword");
        this.pwd2.setNullRepresentation("");
        this.pwd2.setRequired(true);

        fm = new FormLayout();
        final TextField name = new TextField("Your name:");
        fm.addComponent(name);
        this.registerLayout.addComponent(fm, "name");
        name.setNullRepresentation("");
        name.setRequired(true);
        this.form.bind(name, "name");
        name.setValue("Your name");

        fm = new FormLayout();
        final DateField bdate = new DateField("Date of birth:");
        fm.addComponent(bdate);
        this.registerLayout.addComponent(fm, "dateofbirth");
        bdate.setValue(new Date());
        bdate.setRequired(true);
        this.form.bind(bdate, "date_of_birth");
        bdate.setValue(new Date());

        fm = new FormLayout();
        final TextField email = new TextField("Your e-mail address:");
        fm.addComponent(email);
        this.registerLayout.addComponent(fm, "email");
        email.setNullRepresentation("");
        email.setRequired(true);
        this.form.bind(email, "email");
        email.setValue("Your e-mail address");

        fm = new FormLayout();
        final TextArea interests = new TextArea("Topics of interest");
        fm.addComponent(interests);
        this.registerLayout.addComponent(fm, "interests");
        this.form.bind(interests, "interests");
        interests.setValue("Topics of interest");
        this.registerLayout.addComponent(
                new Label("<p style=\"padding-top:34px; color: white; text-align: center; font-family: education;\">" + cache.getUsername()
                        + "</p>", ContentMode.HTML), "auth_user");

        fixStyle();
        JavaScript.getCurrent().addFunction("com_wide_wideweb_register", this);
        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader.js");
    }

    private void fixStyle() {
        JavaScript.getCurrent().execute("window.$(\"td[class='v-formlayout-captioncell']\").attr('class','inputLabel')");
        JavaScript.getCurrent().execute("window.$(\"div[class='v-caption v-caption-hasdescription']\").attr('class','')");
        JavaScript.getCurrent().execute("window.$(\"tr[class='v-formlayout-row v-formlayout-firstrow v-formlayout-lastrow']\").attr('class','')");
        JavaScript.getCurrent().execute(
                "window.$(\".registerPanel input[type='text'], .registerPanel input[type='password'], .registerPanel textarea\").attr('class','default')");
        JavaScript.getCurrent().execute("window.$(\"span:contains('Topics of interest')\").attr('class','inputLabel')");
        JavaScript.getCurrent().execute("window.$(\"span:contains('Topics of interest')\").parent().parent().attr('class','')");
        JavaScript.getCurrent().execute("window.$(\"span:contains('Topics of interest')\").parent().parent().attr('style','vertical-align: top')");
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        // check for invalid fields
        for (Field<?> field : RegisterView.this.form.getFields()) {
            try {
                field.validate();
                ((AbstractComponent) field).setComponentError(null);
                if (field.getValue().equals(field.getCaption().substring(0, field.getCaption().length() - 1))) {
                    throw new InvalidValueException("Default caption");
                }
            } catch (InvalidValueException ex) {
                Notification.show("Invalid value in '" + field.getCaption() + "' field: '" + ex.getMessage() + "'", Notification.Type.ERROR_MESSAGE);
                ((AbstractComponent) field).setComponentError(new UserError("Invalid value"));
                fixStyle();
                return; // display only one error at a time
            }
        }
        ViewDataCache cache = ViewDataCache.getInstance();
        List<User> users = cache.getUsers();
        for (User u : users) {
            if (u.getUsername().equals(this.uname.getValue())) {
                Notification.show("Error", "username already exists", Notification.Type.ERROR_MESSAGE);
                fixStyle();
                return;
            }
        }
        try {
            if (!this.pwd.getValue().equals(this.pwd2.getValue())) {
                Notification.show("Error", "passwords do not match", Notification.Type.ERROR_MESSAGE);
                fixStyle();
                return;
            }
            RegisterView.this.form.commit();
            RegisterView.this.current.setGender(arguments.get(0).equals("option") ? "Male" : "Female");
            User newUser = RegisterView.this.current.getAccount();
            Group group = RegisterView.this.service.getOrCreateGroup("Registered user");
            group.getMembers().add(newUser);
            RegisterView.this.service.saveOrUpdateGroup(group);
            Notification.show("Account created!");
            ViewDataCache.getInstance().initUsers();
            ViewDataCache.getInstance().initGroups();
            this.navigator.navigateTo(ViewUtils.LOGIN);
        } catch (CommitException e) {
            logger.error("Error during commiting newly created user.", e);
            Notification.show("Fatal error. :(");
        }
        fixStyle();
    }
}
