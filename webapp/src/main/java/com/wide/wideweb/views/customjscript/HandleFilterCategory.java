package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.domainmodel.stat.LogEntry.EntryType;
import com.wide.wideweb.util.CompositeFilter;
import com.wide.wideweb.util.ContentFilterInterface;
import com.wide.wideweb.util.ViewUtils;

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
        String language = arguments.getString(6);
        boolean menu = arguments.getBoolean(7);
        boolean lesson = arguments.getBoolean(8);
        boolean test = arguments.getBoolean(9);
        ContentFilterInterface filter = new CompositeFilter(searchText, author, publisher, book, title, submitNo, language, menu, lesson, test);
        this.layout.replaceComponent(
                this.layout.getComponent("secondaryLevel"),
                ViewUtils.getCurrentCategory() != null ? ViewUtils.getSecondaryLevel(ViewUtils.getCurrentCategory(), filter, !filter.isNoneFiltered()) :
                        ViewUtils.searchAll(searchText));
        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
        ViewUtils.logEntry(EntryType.SEARCH_EXERCISE, searchText);
    }

}
