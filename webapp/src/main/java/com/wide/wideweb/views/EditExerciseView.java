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
import com.vaadin.ui.Field;
import com.vaadin.ui.Notification;
import com.wide.domainmodel.Exercise;
import com.wide.wideweb.beans.ExerciseBean;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(ViewUtils.EDIT_EXERCISE)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class EditExerciseView extends AbstractExerciseView {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(EditExerciseView.class);

    // bean
    private Exercise previous;

    @Override
    public void enter(ViewChangeEvent event) {
        this.previous = ViewUtils.getCurrentExercise();
        this.current = new ExerciseBean(this.previous);
        init();
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
            // convert wrapper bean to DB entity
            Exercise dbExercise = view.current.convertFromPrevious(this.previous);
            view.service.saveOrUpdateExercise(dbExercise);
            Notification.show("Exercise saved! You are awesome!");
            ViewDataCache.getInstance().doAllInit();
        } catch (CommitException e) {
            logger.error("Error during commiting the newly edited exercise.", e);
            Notification.show("Fatal error. :(");
        }
    }

    @Override
    public String actionButtonLabel() {
        return "Save";
    }

}
