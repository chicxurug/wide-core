package com.wide.wideweb.views;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.wide.wideweb.util.ViewUtils;

/**
 * 
 * @author Attila Cs.
 * 
 */
@Component
@Scope("SCOPE_PROTOTYPE")
@VaadinView(ViewUtils.ERROR)
public class ErrorView extends VerticalLayout implements View {

    private static final long serialVersionUID = -583103480424334639L;

    @Override
    public void enter(ViewChangeEvent event) {
        Exception e = (Exception) getSession().getAttribute("unhandled.exception");
        TextArea area = new TextArea("error.viewtitle");
        area.setSizeFull();
        if (e != null) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            area.setValue(sw.toString());
        }
        setMargin(true);
        setSpacing(true);
        setSizeFull();
        addComponent(area);

    }

}
