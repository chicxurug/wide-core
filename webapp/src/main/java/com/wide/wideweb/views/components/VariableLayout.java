package com.wide.wideweb.views.components;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class VariableLayout extends HorizontalLayout {

    private TextField varName;
    private TextField varVal;

    private Button removeButton;

    /**
     * 
     */
    private static final long serialVersionUID = 2140036312895482807L;

    public VariableLayout(final FormLayout mainLayout) {
        this.varName = new TextField("Variable name");
        this.varName.setRequired(true);
        this.varName.setNullRepresentation("");
        this.varVal = new TextField("Value");
        this.varVal.setRequired(true);
        this.varVal.setNullRepresentation("");
        this.addComponent(this.varName);
        this.addComponent(this.varVal);
        this.removeButton = new Button("Remove");
        this.addComponent(this.removeButton);
        this.removeButton.setVisible(false);
    }

    public void addRemoveAction(ClickListener removeAction) {
        this.removeButton.addClickListener(removeAction);
        this.removeButton.setVisible(true);
    }

    public TextField getVarName() {
        return this.varName;
    }

    public void setVarName(TextField varName) {
        this.varName = varName;
    }

    public TextField getVarVal() {
        return this.varVal;
    }

    public void setVarVal(TextField varVal) {
        this.varVal = varVal;
    }
}
