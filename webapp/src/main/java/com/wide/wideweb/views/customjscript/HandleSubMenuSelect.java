package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.domainmodel.Category;
import com.wide.wideweb.util.CompositeFilter;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

public class HandleSubMenuSelect implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = -1696128925160318613L;

    private final CustomLayout layout;
    private final ViewDataCache cache = ViewDataCache.getInstance();

    public HandleSubMenuSelect(CustomLayout layout) {
        this.layout = layout;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        try {
            Thread.sleep(arguments.getInt(1));
            Category selectedCategory = ":current:".equals(arguments.getString(0)) ? ViewUtils.getCurrentCategory() : this.cache.getCategoryByName(arguments
                    .getString(0));
            if (selectedCategory.getName() == null) {
                // Return because it is probably a test or an exercise which will be handled in a later development phase
                return;
            }
            ViewUtils.setCurrentCategory(selectedCategory);
            boolean menu = arguments.getBoolean(2);
            boolean lesson = arguments.getBoolean(3);
            boolean test = arguments.getBoolean(3);
            this.layout.replaceComponent(this.layout.getComponent("crumb"), ViewUtils.getBreadCrumb(selectedCategory));
            this.layout.replaceComponent(this.layout.getComponent("secondaryLevel"), ViewUtils.getSecondaryLevel(selectedCategory,
                    new CompositeFilter("Enter search keywords here", "Author", "Publisher", "Book", "Title", "Submits No.", "Language", menu, lesson, test)));
            ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
        } catch (InterruptedException e) {
            ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
        }
    }

}
