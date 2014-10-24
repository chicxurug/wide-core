package com.wide.wideweb.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.wide.common.FeatureFactory;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Feature;
import com.wide.domainmodel.Test;
import com.wide.domainmodel.stat.LogEntry;
import com.wide.domainmodel.stat.LogEntry.EntryType;
import com.wide.domainmodel.user.Group;
import com.wide.domainmodel.user.User;
import com.wide.persistence.PersistenceListener;
import com.wide.service.WideService;
import com.wide.wideweb.beans.ExerciseBean;

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
        sb.append("<img src=\"" + getImgPath(ex, "share")
                + "\" onclick=\"window.$('" + (SpringSecurityHelper.hasRole("ROLE_USER") ? "#dialog-message" : "#dialog-message-err")
                + "').dialog('open');\" style=\"cursor: pointer; cursor: hand;\" width=16 height=16/>\n");
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
        if ("share".equals(type)) {
            return path + ".png";
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

    public static void logEntry(EntryType type) {
        ViewDataCache cache = ViewDataCache.getInstance();
        WideService service = new WideService(PersistenceListener.getEntityManagerFactory());
        switch (type) {
            case VIEW_EXERCISE:
            case CHECK_SOLUTION:
                service.saveLog(new LogEntry(cache.getUser(), type, "EX_" + getCurrentExercise().getId()));
                break;
            default:
                break;
        }
    }

    public static void logEntry(EntryType type, String extra) {
        ViewDataCache cache = ViewDataCache.getInstance();
        WideService service = new WideService(PersistenceListener.getEntityManagerFactory());
        switch (type) {
            case SUBMIT_SOLUTION:
                service.saveLog(new LogEntry(cache.getUser(), type, "EX_" + getCurrentExercise().getId()
                        + (getFeatureValue(getCurrentExercise(), FeatureFactory.SHORT_ANSWER).equals(extra) ? "_SOLVED" : "_WRONG"), extra));
                break;
            case ADD_EXERCISE:
            case MODIFY_EXERCISE:
                service.saveLog(new LogEntry(cache.getUser(), type, "EX_" + extra));
            case SEARCH_EXERCISE:
                service.saveLog(new LogEntry(cache.getUser(), type, "SEARCH", extra));
                break;
            default:
                break;
        }
    }

    public static void addToCart() {
        ViewDataCache cache = ViewDataCache.getInstance();
        cache.addToCart(getCurrentExercise(), getExURL(getCurrentExercise()));
        Notification.show("Exercise is added to cart! You are awesome!");
    }

    public static String getShareTemplate() {
        ViewDataCache cache = ViewDataCache.getInstance();
        Map<Exercise, String> cart = cache.getCart();
        StringBuffer msgBody = new StringBuffer();
        msgBody.append("Dear $RCPT$,\n\n");
        msgBody.append((cache.getUser().getProfile() == null ? cache.getUser().getUsername() : cache.getUser().getProfile().getName())
                + " shared the following exercises with you:\n\n");
        for (Exercise e : cart.keySet()) {
            msgBody.append(e.getTitle() + "\n");
            msgBody.append(cart.get(e) + "\n\n");
        }
        msgBody.append("Happy practicing!\n\n");
        msgBody.append("The WIDE team");

        return msgBody.toString();
    }

    public static List<Group> myGroups() {
        ViewDataCache cache = ViewDataCache.getInstance();
        return cache.getCurrentUserGroups();
    }

    public static String getExURL(Exercise currentEx) {
        return Page.getCurrent().getLocation().getScheme() + ":" + Page.getCurrent().getLocation().getSchemeSpecificPart() +
                "#!" + ViewUtils.VIEW_EXERCISE + "/" + currentEx.getId();
    }

    public static void sendCartByMail(final String msgTemplate, final Group recepients) {
        Thread senMailThread = new Thread(new Runnable() {

            @Override
            public void run() {
                ViewDataCache cache = ViewDataCache.getInstance();
                final User currentUser = cache.getUser();

                String host = "smtp.gmail.com";
                int port = 465;
                String username = "rf.user.123";
                String password = "rf_pass_123";

                Properties props = new Properties();
                props.put("mail.smtps.auth", "true");

                Session session = Session.getInstance(props);
                try {
                    for (User u : recepients.getMembers()) {
                        if (currentUser.equals(u) || u.getProfile() == null || u.getProfile().getEmail() == null || u.getProfile().getEmail().isEmpty()) {
                            continue;
                        }
                        Message msg = new MimeMessage(session);
                        msg.setFrom(new InternetAddress("no-reply@wide.com"));
                        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(u.getProfile().getEmail()));
                        msg.setSubject("WIDE exercise sharing");
                        msg.setContent(msgTemplate.replace("$RCPT$", u.getProfile().getName()), "text/plain");
                        Transport t = session.getTransport("smtps");
                        t.connect(InetAddress.getByName(host).getCanonicalHostName(), port, username, password);
                        t.sendMessage(msg, msg.getAllRecipients());
                        t.close();
                        System.out.println("E-mail sent to: " + u.getProfile().getEmail());
                    }
                    cache.clearCart();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });

        // Start mail sending in a separate thread
        senMailThread.start();

    }

    public static void clearSession() {
        setCurrentCategory(null);
        setCurrentExercise(null);
        clearCart();
    }

    public static void clearCart() {
        ViewDataCache cache = ViewDataCache.getInstance();
        cache.clearCart();
    }

    public static Map<Exercise, String> getCart() {
        ViewDataCache cache = ViewDataCache.getInstance();
        return cache.getCart();
    }

    public static Label getAnswerTable(Exercise currentEx) {
        StringBuilder tableBuilder = new StringBuilder();
        tableBuilder.append("<table>\n");
        String answer = getFeatureValue(currentEx, FeatureFactory.SHORT_ANSWER);
        for (String var : answer.split(ExerciseBean.VAR_SEPARATOR)) {
            if (":=".equals(var)) {
                continue;
            }
            tableBuilder.append("<tr><td>\n");
            tableBuilder.append(var.split(":=")[0] + ":");
            tableBuilder.append("</td></tr>\n");
            tableBuilder.append("<tr><td style=\"padding:5px;spacing:-5px\">\n");
            tableBuilder.append("<input type=\"text\" class=\"filled default\" value=\"Your solution comes here\">\n");
            tableBuilder.append("</td></tr>\n");
        }
        tableBuilder.append("</table>\n");
        return new Label(tableBuilder.toString(), ContentMode.HTML);
    }

}
