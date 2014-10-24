package com.wide.wideweb.util;

import java.util.Map;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.wide.wideweb.beans.ExerciseBean;
import com.wide.wideweb.views.components.VariableLayout;

public class ExerciseBeanFieldGroup extends BeanFieldGroup<ExerciseBean> {

    /**
     * 
     */
    private static final long serialVersionUID = 7782367682656856869L;

    public ExerciseBeanFieldGroup(Class<ExerciseBean> beanType) {
        super(beanType);
    }

    public void bind(VariableLayout var, Object propertyId) {
        if (!(propertyId instanceof Map)) {

        }
    }
}
