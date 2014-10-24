package com.wide.wideweb.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.stat.LogEntry.EntryType;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(ViewUtils.CREATE_EXERCISE)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CreateExerciseView extends AbstractExerciseView {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(CreateExerciseView.class);

    @Override
    public void enter(ViewChangeEvent event) {
        init();
        com.vaadin.ui.JavaScript.getCurrent().execute("MathJax.Hub.Queue([\"Typeset\",MathJax.Hub]);");
    }

    @Override
    public void actionButtonClicked(AbstractExerciseView view) {
        // check for invalid fields
        for (Field<?> field : view.form.getFields()) {
            try {
                field.validate();
                ((AbstractComponent) field).setComponentError(null);
            } catch (InvalidValueException ex) {
                Notification.show(ex.getMessage());
                ((AbstractComponent) field).setComponentError(new UserError(ex.getMessage()));
                return; // display only one error at a time
            }
        }
        // if there is no general error, we try to commit the changes
        try {
            view.form.commit();
            logger.info("Exercise created: {}", view.current);
            Exercise dbExercise = view.current.convert(); // convert wrapper bean to DB entity
            Long exId = view.service.saveOrUpdateExercise(dbExercise).getId();
            Notification.show("Exercise created! You are awesome!");
            ViewDataCache.getInstance().doAllInit();
            ViewUtils.logEntry(EntryType.ADD_EXERCISE, exId.toString());
        } catch (CommitException e) {
            logger.error("Error during commiting newly created exercise.", e);
            Notification.show("Fatal error. :(");
        }
    }

    @Override
    public String actionButtonLabel() {
        return "Create";
    }

    @Override
    public boolean isViewProvideDelete() {
        return false;
    }

    @Override
    public Button getDeleteButton() {
        // This call will never happen
        return null;
    }

}
