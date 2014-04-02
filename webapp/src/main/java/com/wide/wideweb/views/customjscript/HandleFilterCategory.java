package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.wideweb.util.CompositeFilter;
import com.wide.wideweb.util.ViewUtils;
import com.wide.wideweb.views.MainView;

public class HandleFilterCategory implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = 2983054985326606047L;

    private final CustomLayout layout;

    public HandleFilterCategory(CustomLayout layout) {
        this.layout = layout;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        String searchText = arguments.getString(0);
        String author = arguments.getString(1);
        String publisher = arguments.getString(2);
        String book = arguments.getString(3);
        String title = arguments.getString(4);
        String submitNo = arguments.getString(5);
        boolean menu = arguments.getBoolean(6);
        boolean lesson = arguments.getBoolean(7);
        boolean test = arguments.getBoolean(8);
        this.layout.replaceComponent(
                this.layout.getComponent("secondaryLevel"),
                MainView.current != null ? ViewUtils.getSecondaryLevel(MainView.current, new CompositeFilter(searchText, author, publisher, book, title,
                        submitNo, menu, lesson, test)) :
                        ViewUtils.searchAll(searchText));
        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
    }

}
