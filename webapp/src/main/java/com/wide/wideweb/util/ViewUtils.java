package com.wide.wideweb.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Queue;

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
import com.wide.domainmodel.Feature;
import com.wide.domainmodel.Test;

/**
 * 
 * @author Attila Cs.
 * 
 */
public class ViewUtils {

    public static final String LOGIN = "login";
    public static final String REGISTER = "register";
    public static final String MAIN = "main";
    public static final String CREATE_EXERCISE = "createExercise";
    public static final String EDIT_EXERCISE = "editExercise";
    public static final String VIEW_EXERCISE = "viewExercise";
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
        return getCategoryList(cat, "#");
    }

    public static Label getCategoryList(Category cat, String relURL) {
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
                sb.append("<a href=\"" + relURL + "\" class=\"math\" target=\"\">" + mainCategory.getName() + "</a>");
            } else {
                sb.append("<a href=\"" + relURL + "\" target=\"\">" + mainCategory.getName() + "</a>");
            }
            if (mainCategory.getParent().getName().isEmpty() && cache.getCategories().get(mainCategory).isEmpty()) {
                sb.append("</strike>");
            }
            sb.append("</li>\n");
        }
        return new Label(sb.toString(), ContentMode.HTML);
    }

    public static Component getSecondaryLevel(Category category, ContentFilterInterface filter, boolean isRecursive) {
        ViewDataCache cache = ViewDataCache.getInstance();
        String basepath = getBasePath();
        if (category.getParent() == null) {
            return new Label(
                    "<ul class=\"filterList\">"
                            + "<li class=\"folder\">"
                            + "         <img class=\"icon\" src=\""
                            + basepath
                            + "/layouts/example/1.jpg\" />"
                            + "         <div class=\"title\" style=\"word-break: break-all;\">This is a Mississippi long like subcategory name as you see</div>"
                            + "         <div class=\"subfolders\">Subcategories: <b>82</b></div>"
                            + "         <div class=\"lessons\">Lessons: <b>33</b></div>"
                            + "         <div class=\"tests\">Tests: <b>12</b></div>"
                            + "</li>"
                            + "<li class=\"folder\">"
                            + "         <img class=\"icon\" src=\""
                            + basepath
                            + "/layouts/example/3.jpg\" />"
                            + "         <div class=\"title\" style=\"word-break: break-all;\">This is a Mississippi long like subcategory name as you see</div>"
                            + "         <div class=\"subfolders\">Subcategories: <b>82</b></div>"
                            + "         <div class=\"lessons\">Lessons: <b>33</b></div>"
                            + "         <div class=\"tests\">Tests: <b>12</b></div>"
                            + "</li>"
                            + "<li class=\"lesson\">"
                            + "         <img class=\"icon\" src=\""
                            + basepath
                            + "/layouts/example/2.jpg\" />"
                            + "         <div class=\"title\" style=\"word-break: break-all;\"><div class=\"schoolLevel university\"></div>This is a Mississippi long like lesson name as you see</div>"
                            + "         <img class=\"level\" src=\"" + basepath + "/img/level2.png\" />"
                            + "         <img class=\"rank\" src=\"" + basepath + "/img/rank4.png\" />"
                            + "         <div class=\"author\">Author: <a href=\"#\">Stephen Hawking</a></div>"
                            + "</li>"
                            + "</ul>", ContentMode.HTML);
        } else {
            Collection<Category> mainCategories = new HashSet<Category>(cache.getCategories().get(category));
            Collection<Exercise> exercises = new ArrayList<Exercise>(cache.getExerciseByCategory(category));
            Collection<Test> tests = cache.getTestsByCategory(category);

            if (isRecursive) {
                Queue<Category> cats = new ArrayDeque<Category>(mainCategories);
                while (!cats.isEmpty()) {
                    Category cat = cats.remove();
                    mainCategories.addAll(cache.getCategories().get(cat));
                    cats.addAll(cache.getCategories().get(cat));
                }
                for (Category cat : mainCategories) {
                    exercises.addAll(cache.getExerciseByCategory(cat));
                    tests.addAll(cache.getTestsByCategory(cat));
                }
            }

            StringBuilder sb = new StringBuilder("<ul class=\"filterList\">\n");
            for (Category cat : mainCategories) {
                if (filter.isFiltered(cat)) {
                    continue;
                }
                sb.append("<li class=\"folder\">\n"
                        + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/1.jpg\" />\n"
                        + "         <div class=\"title\" style=\"word-break: break-all;\">" + cat.getName() + "</div>\n"
                        + "         <div class=\"subfolders\">Subcategories: <b>" + cache.getCategories().get(cat).size() + "</b></div>\n"
                        + "         <div class=\"lessons\">Lessons: <b>" + cache.getExerciseByCategory(cat).size() + "</b></div>\n"
                        + "         <div class=\"tests\">Tests: <b>" + cache.getTestsByCategory(cat).size() + "</b></div>\n"
                        + "</li>\n");
            }

            for (Exercise ex : exercises) {
                if (filter.isFiltered(ex)) {
                    continue;
                }
                sb.append("<li id=" + ex.getId() + " class=\"lesson\">"
                        + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/2.jpg\" />"
                        + "         <div class=\"title\" style=\"word-break: break-all;\"><div class=\"schoolLevel "
                        + ex.getLevel().getDescription().replaceAll(" ", "_") + "\"></div>"
                        + ex.getTitle() + "</div>"
                        + "         <img class=\"level\" src=\"" + getImgPath(ex, "difficulty") + "\" width=24 height=16/>"
                        + "         <img class=\"rank\" src=\"" + getImgPath(ex, "score") + "\" width=92 height=16/>"
                        + "         <div class=\"author\">Author: <a href=\"#\">" + ex.getAuthor() + "</a></div>"
                        + "         </li>\n");
            }

            for (Test t : tests) {
                if (filter.isFiltered(t)) {
                    continue;
                }
                sb.append("<li class=\"test\">"
                        + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/3.jpg\" />"
                        + "         <div class=\"title\" style=\"word-break: break-all;\">" + t.getDescription() + "</div>"
                        + "         <div class=\"lessons\">Lessons: <b>" + t.getExercises().size() + "</b></div>\n"
                        + "         </li>\n");
            }

            return new Label(sb.toString() + "</ul>", ContentMode.HTML);
        }
    }

    public static Component searchAll(String searchText) {
        ViewDataCache cache = ViewDataCache.getInstance();
        String basepath = getBasePath();
        StringBuilder sb = new StringBuilder("<ul class=\"filterList\">\n");
        final Collection<Category> mainCategories = cache.getCategories().values();
        ContentFilterInterface filter = new AllFilter(searchText);

        for (Category cat : mainCategories) {
            if (filter.isFiltered(cat)) {
                continue;
            }
            sb.append("<li class=\"folder\">\n"
                    + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/1.jpg\" />\n"
                    + "         <div class=\"title\" style=\"word-break: break-all;\">" + cat.getName() + "</div>\n"
                    + "         <div class=\"subfolders\">Subcategories: <b>" + cache.getCategories().get(cat).size() + "</b></div>\n"
                    + "         <div class=\"lessons\">Lessons: <b>" + cache.getExerciseByCategory(cat).size() + "</b></div>\n"
                    + "         <div class=\"tests\">Tests: <b>" + cache.getTestsByCategory(cat).size() + "</b></div>\n"
                    + "</li>\n");
        }

        final Collection<Exercise> exercises = cache.getExercisesBySearchText(searchText);
        for (Exercise ex : exercises) {
            sb.append("<li id=" + ex.getId() + " class=\"lesson\">"
                    + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/2.jpg\" />"
                    + "         <div class=\"title\" style=\"word-break: break-all;\"><div class=\"schoolLevel "
                    + ex.getLevel().getDescription().replaceAll(" ", "_") + "\"></div>"
                    + ex.getTitle() + "</div>"
                    + "         <img class=\"level\" src=\"" + getImgPath(ex, "difficulty") + "\"  width=24 height=16/>"
                    + "         <img class=\"rank\" src=\"" + getImgPath(ex, "score") + "\" width=92 height=16/>"
                    + "         <div class=\"author\">Author: <a href=\"#\">" + ex.getAuthor() + "</a></div>"
                    + "         </li>\n");
        }

        final Collection<Test> tests = cache.getTestsBySearchText(searchText);
        for (Test t : tests) {
            if (filter.isFiltered(t)) {
                continue;
            }
            sb.append("<li class=\"test\">"
                    + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/3.jpg\" />"
                    + "         <div class=\"title\" style=\"word-break: break-all;\">" + t.getDescription() + "</div>"
                    + "         <div class=\"lessons\">Lessons: <b>" + t.getExercises().size() + "</b></div>\n"
                    + "         </li>\n");
        }

        return new Label(sb.toString() + "</ul>", ContentMode.HTML);
    }

    public static Component searchExercisesByProperty(String propName, String propValue) {
        ViewDataCache cache = ViewDataCache.getInstance();
        String basepath = getBasePath();
        StringBuilder sb = new StringBuilder("<ul class=\"filterList\">\n");

        final Collection<Exercise> exercises = cache.getExercisesByProperty(propName, propValue);
        for (Exercise ex : exercises) {
            sb.append("<li id=" + ex.getId() + " class=\"lesson\">"
                    + "         <img class=\"icon\" src=\"" + basepath + "/layouts/example/2.jpg\" />"
                    + "         <div class=\"title\" style=\"word-break: break-all;\"><div class=\"schoolLevel "
                    + ex.getLevel().getDescription().replaceAll(" ", "_") + "\"></div>"
                    + ex.getTitle() + "</div>"
                    + "         <img class=\"level\" src=\"" + getImgPath(ex, "difficulty") + "\"  width=24 height=16/>"
                    + "         <img class=\"rank\" src=\"" + getImgPath(ex, "score") + "\" width=92 height=16/>"
                    + "         <div class=\"author\">Author: <a href=\"#\">" + ex.getAuthor() + "</a></div>"
                    + "         </li>\n");
        }

        return new Label(sb.toString() + "</ul>", ContentMode.HTML);
    }

    public static Component getBreadCrumb(Category cat, Exercise ex) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> crumbs = new ArrayList<String>();
        while (cat.getParent() != null) {
            crumbs.add("\t<li><a href=\"#!" + ViewUtils.MAIN + "\">" + cat.getName() + "</a></li>\n");
            cat = cat.getParent();
        }
        for (int i = crumbs.size() - 1; i >= 0; i--) {
            sb.append(crumbs.get(i));
        }
        if (ex != null) {
            sb.append("\t<li><a href=\"#!" + ViewUtils.VIEW_EXERCISE + "\" class=\"exercise\">" + ex.getTitle() + "</a></li>\n");
        }
        return new Label(sb.toString(), ContentMode.HTML);
    }

    public static Component getBreadCrumb(Category cat) {
        return getBreadCrumb(cat, null);
    }

    public static Component getExerciseDetails(Exercise ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("<img class=\"level\" src=\"" + getImgPath(ex, "difficulty") + "\"  width=24 height=16/>\n");
        sb.append("<img class=\"rank\" src=\"" + getImgPath(ex, "score") + "\" width=92 height=16/>\n");
        sb.append("<ul class=\"source\">\n");
        sb.append("<li>Szerző: " + getSeparatedLinks("author", ex.getAuthor()) + "</li>\n");
        sb.append("<li>Könyv: " + getSeparatedLinks("booktitle", ex.getBookTitle()) + "</li>\n");
        sb.append("<li>Kiadó: " + getSeparatedLinks("publisher", ex.getPublisher()) + "</li>\n");
        sb.append("</ul>\n");
        sb.append("<ul class=\"tags\">Tags:\n");
        if (ex.getFeatures() != null) {
            for (Feature f : ex.getFeatures()) {
                if (FeatureFactory.TAGS.equals(f.getName())) {
                    for (String tag : f.getValue().split(",")) {
                        sb.append("<li><a href=\"#!" + ViewUtils.MAIN + "/tag=" + tag.trim() + "\">" + tag.trim() + "</a> , </li>");
                    }
                    sb.replace(sb.length() - 8, sb.length(), "</li>");
                }
            }
        }
        return new Label(sb.toString(), ContentMode.HTML);
    }

    public static String getSeparatedLinks(String prop, String propVal) {
        if (propVal == null || propVal.isEmpty()) {
            return "<a>-</a>";
        }
        String aHref = "";
        String[] splitValues = propVal.split(",");
        for (String val : splitValues) {
            aHref += "<a href=\"#!" + ViewUtils.MAIN + "/" + prop + "=" + val.trim() + "\">" + val.trim() + "</a>, ";
        }
        return aHref.substring(0, aHref.length() - 2);
    }

    private static String getBasePath() {
        return Page.getCurrent().getLocation().getScheme() + ":" + Page.getCurrent().getLocation().getSchemeSpecificPart() + "VAADIN/themes/wideweb";
    }

    private static String getImgPath(Exercise e, String type) {
        String path = Page.getCurrent().getLocation().getScheme() + ":" + Page.getCurrent().getLocation().getSchemeSpecificPart()
                + "VAADIN/themes/wideweb/img/" + type;
        if ("difficulty".equals(type)) {
            return path + e.getDifficulty().getDifficulty() + ".png";
        }
        if ("score".equals(type)) {
            return path + (e.getScore() == null ? "0" : e.getScore()) + ".png";
        }

        return "";
    }

    public static void setCurrentCategory(Category c) {
        VaadinSession.getCurrent().setAttribute(":current:", c);
    }

    public static Category getCurrentCategory() {
        return (Category) VaadinSession.getCurrent().getAttribute(":current:");
    }

    public static void setCurrentExercise(Exercise e) {
        VaadinSession.getCurrent().setAttribute(":exercise:", e);
    }

    public static Exercise getCurrentExercise() {
        return (Exercise) VaadinSession.getCurrent().getAttribute(":exercise:");
    }

    public static String getFeatureValue(Exercise ex, String fName) {
        for (Feature f : ex.getFeatures()) {
            if (fName.equals(f.getName())) {
                return f.getValue();
            }
        }
        return "";
    }
}
