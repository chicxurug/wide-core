package com.wide.wideweb.views.customjscript;

import org.json.JSONArray;
import org.json.JSONException;

import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScriptFunction;
import com.wide.domainmodel.Category;
import com.wide.wideweb.util.ViewDataCache;
import com.wide.wideweb.util.ViewUtils;

public class HandleMenuSelect implements JavaScriptFunction {

    /**
     * 
     */
    private static final long serialVersionUID = 1968033905776616207L;

    private final CustomLayout layout;
    private final ViewDataCache cache = ViewDataCache.getInstance();

    public HandleMenuSelect(CustomLayout layout) {
        this.layout = layout;
    }

    @Override
    public void call(JSONArray arguments) throws JSONException {
        Category selectedCategory = this.cache.getCategoryByName(arguments.getString(0));
        this.layout.replaceComponent(this.layout.getComponent("subMenuItems"), ViewUtils.getCategoryList(selectedCategory));
        ViewUtils.injectJs("/VAADIN/themes/wideweb/js/subHeader_full.js");
    }

}
