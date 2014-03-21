package com.wide.wideweb.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Test;

/**
 * 
 * @author Attila Cs.
 * 
 */
public class ViewUtils {

    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String MAIN = "";
    public static final String ERROR = "error";

    public static void navigateToErrorView(VaadinSession session, ViewChangeEvent event, Exception e) {
        session.setAttribute("unhandled.exception", e);
        event.getNavigator().navigateTo(ViewUtils.ERROR);
    }

    public static void injectJs(String... jsScripts) {
        for (String jsPath : jsScripts) {
            String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
            FileResource resource = new FileResource(new File(basepath + jsPath));
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getStream().getStream()));
            String line = "";
            StringBuilder b = new StringBuilder();

            try {
                while ((line = br.readLine()) != null) {
                    b.append(line);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            com.vaadin.ui.JavaScript.getCurrent().execute(b.toString());
        }
    }

    public static Label getCategoryList(Category cat) {
        ViewDataCache cache = ViewDataCache.getInstance();
        StringBuilder sb = new StringBuilder();
        final Collection<Category> mainCategories = cache.getCategories().get(cat);
        for (Category mainCategory : mainCategories) {
            sb.append("\t");
            sb.append("<li>");
            if (mainCategory.getParent().getName().isEmpty() && cache.getCategories().get(mainCategory).isEmpty()) {
                sb.append("<strike>");
            }
            if ("Maths".equals(mainCategory.getName())) {
                sb.append("<a href=\"#\" class=\"math\" target=\"\">" + mainCategory.getName() + "</a>");
            } else {
                sb.append("<a href=\"#\" target=\"\">" + mainCategory.getName() + "</a>");
            }
            if (mainCategory.getParent().getName().isEmpty() && cache.getCategories().get(mainCategory).isEmpty()) {
                sb.append("</strike>");
            }
            sb.append("</li>\n");
        }
        return new Label(sb.toString(), ContentMode.HTML);
    }

    public static Component getSecondaryLevel(Category category, ContentFilterInterface filter) {
        ViewDataCache cache = ViewDataCache.getInstance();
        String basepath = Page.getCurrent().getLocation().toString().replaceAll("#", "") + "VAADIN/themes/wideweb";
        if (category.getParent() == null) {
            return new Label("<ul class=\"filterList\">"
                    + "<li class=\"folder\">"
                    + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/1.jpg\" />"
                    + "         <div class=\"title\">This is a Mississippi long like subcategory name as you see</div>"
                    + "         <div class=\"subfolders\">Subcategories: <b>82</b></div>"
                    + "         <div class=\"lessons\">Lessons: <b>33</b></div>"
                    + "         <div class=\"tests\">Tests: <b>12</b></div>"
                    + "</li>"
                    + "<li class=\"folder\">"
                    + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/3.jpg\" />"
                    + "         <div class=\"title\">This is a Mississippi long like subcategory name as you see</div>"
                    + "         <div class=\"subfolders\">Subcategories: <b>82</b></div>"
                    + "         <div class=\"lessons\">Lessons: <b>33</b></div>"
                    + "         <div class=\"tests\">Tests: <b>12</b></div>"
                    + "</li>"
                    + "<li class=\"lesson\">"
                    + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/2.jpg\" />"
                    + "         <div class=\"title\"><div class=\"schoolLevel university\"></div>This is a Mississippi long like lesson name as you see</div>"
                    + "         <img class=\"level\" src=\"" + basepath + "/img/level2.png\" />"
                    + "         <img class=\"rank\" src=\"" + basepath + "/img/rank4.png\" />"
                    + "         <div class=\"author\">Author: <a href=\"#\">Stephen Hawking</a></div>"
                    + "</li>"
                    + "</ul>", ContentMode.HTML);
        } else {
            final Collection<Category> mainCategories = cache.getCategories().get(category);
            StringBuilder sb = new StringBuilder("<ul class=\"filterList\">\n");
            for (Category cat : mainCategories) {
                if (filter.isFiltered(cat)) {
                    continue;
                }
                sb.append("<li class=\"folder\">\n"
                        + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/1.jpg\" />\n"
                        + "         <div class=\"title\">" + cat.getName() + "</div>\n"
                        + "         <div class=\"subfolders\">Subcategories: <b>" + cache.getCategories().get(cat).size() + "</b></div>\n"
                        + "         <div class=\"lessons\">Lessons: <b>" + cache.getExerciseByCategory(cat).size() + "</b></div>\n"
                        + "         <div class=\"tests\">Tests: <b>" + cache.getTestsByCategory(cat).size() + "</b></div>\n"
                        + "</li>\n");
            }

            final Collection<Exercise> exercises = cache.getExerciseByCategory(category);
            for (Exercise ex : exercises) {
                if (filter.isFiltered(ex)) {
                    continue;
                }
                sb.append("<li class=\"lesson\">"
                        + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/2.jpg\" />"
                        + "         <div class=\"title\"><div class=\"schoolLevel university\"></div>" + ex.getTitle() + "</div>"
                        + "         <img class=\"level\" src=\"" + basepath + "/img/level2.png\" />"
                        + "         <img class=\"rank\" src=\"" + basepath + "/img/rank4.png\" />"
                        + "         <div class=\"author\">Author: <a href=\"#\">" + ex.getAuthor() + "</a></div>"
                        + "         </li>\n");
            }

            final Collection<Test> tests = cache.getTestsByCategory(category);
            for (Test t : tests) {
                if (filter.isFiltered(t)) {
                    continue;
                }
                sb.append("<li class=\"test\">"
                        + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/3.jpg\" />"
                        + "         <div class=\"title\">" + t.getDescription() + "</div>"
                        + "         <div class=\"lessons\">Lessons: <b>" + t.getExercises().size() + "</b></div>\n"
                        + "         </li>\n");
            }

            return new Label(sb.toString() + "</ul>", ContentMode.HTML);
        }
    }

    public static Component getBreadCrump(Category cat) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> crumbs = new ArrayList<String>();
        while (cat.getParent() != null) {
            crumbs.add("\t<li><a href=\"#\">" + cat.getName() + "</a></li>\n");
            cat = cat.getParent();
        }
        for (int i = crumbs.size() - 1; i >= 0; i--) {
            sb.append(crumbs.get(i));
        }
        return new Label(sb.toString(), ContentMode.HTML);
    }
}
