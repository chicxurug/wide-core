package com.wide.wideweb.views.customjscript;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.wide.domainmodel.user.Group;
import com.wide.wideweb.util.SpringSecurityHelper;
import com.wide.wideweb.util.ViewUtils;

public class HandleSendCart implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = 3715672003086772436L;

    @Override
    public void call(JSONArray arguments) throws JSONException {
        if (!SpringSecurityHelper.hasRole("ROLE_USER")) {
            return;
        }
        final Window sendDetails = new Window("Share URL details");
        sendDetails.center();
        sendDetails.setModal(true);
        sendDetails.setClosable(false);
        sendDetails.setResizable(false);
        sendDetails.setHeight("400px");
        sendDetails.setWidth("800px");

        VerticalLayout subContent = new VerticalLayout();
        subContent.setMargin(true);
        sendDetails.setContent(subContent);

        HorizontalLayout line1 = new HorizontalLayout();
        line1.addComponent(new Label("You are about to share the following exercises with the group members of"));
        final ComboBox groups = new ComboBox();
        groups.setNullSelectionAllowed(false);
        List<Group> myGroups = ViewUtils.myGroups();
        for (Group g : myGroups) {
            groups.addItem(g);
        }
        groups.select(myGroups.get(0));
        line1.addComponent(groups);

        HorizontalLayout line3 = new HorizontalLayout();
        Button send = new Button("Send");
        Button close = new Button("Close");
        Button clear = new Button("Clear");
        line3.addComponent(send);
        line3.addComponent(close);
        line3.addComponent(clear);

        // Put some components in it
        subContent.addComponent(line1);
        final TextArea template = new TextArea("The template of the text:");
        template.setSizeFull();
        template.setHeight("250px");
        template.setValue(ViewUtils.getShareTemplate());
        subContent.addComponent(template);
        subContent.addComponent(line3);

        send.addClickListener(new ClickListener() {

            /**
             * 
             */
            private static final long serialVersionUID = -3041643503693062760L;

            @Override
            public void buttonClick(ClickEvent event) {
                ViewUtils.sendCartByMail(template.getValue(), (Group) groups.getValue());
                sendDetails.close();
            }
        });

        close.addClickListener(new ClickListener() {

            /**
             * 
             */
            private static final long serialVersionUID = 5531170099297850801L;

            @Override
            public void buttonClick(ClickEvent event) {
                sendDetails.close();

            }
        });

        clear.addClickListener(new ClickListener() {

            /**
             * 
             */
            private static final long serialVersionUID = 5242088230948403970L;

            @Override
            public void buttonClick(ClickEvent event) {
                ViewUtils.clearCart();
                sendDetails.close();
            }
        });

        UI.getCurrent().addWindow(sendDetails);
    }

}
