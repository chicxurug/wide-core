package com.wide.wideweb.util;

import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Test;

public class CompositeFilter implements ContentFilterInterface {

    public static final String DEF_SEARCH_TEXT = "Enter search keywords here";
    public static final String DEF_AUTHOR = "Author";
    public static final String DEF_PUBLISHER = "Publisher";
    public static final String DEF_BOOK = "Book";
    public static final String DEF_TITLE = "Title";
    public static final String DEF_SUBMITNO = "Submits No.";
    public static final String DEF_LANGUAGE = "Language";

    private String searchText;
    private String author;
    private String publisher;
    private String book;
    private String title;
    private int submitNo;
    private String language;
    private boolean menu;
    private boolean lesson;
    private boolean test;

    public CompositeFilter(String searchText, String author, String publisher, String book, String title, String submitNo, String language, boolean menu,
            boolean lesson,
            boolean test) {
        this.searchText = searchText;
        this.author = author;
        this.publisher = publisher;
        this.book = book;
        this.title = title;
        this.submitNo = DEF_SUBMITNO.equals(submitNo) ? 0 : Integer.parseInt(submitNo);
        this.language = language;
        this.menu = menu;
        this.lesson = lesson;
        this.test = test;
    }

    @Override
    public boolean isFiltered(Category cat) {
        if (!this.menu) {
            return true;
        }
        if (!DEF_SEARCH_TEXT.equals(this.searchText) && !cat.getName().contains(this.searchText)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isFiltered(Exercise ex) {
        if (!this.lesson) {
            return true;
        }
        if (!DEF_SEARCH_TEXT.equals(this.searchText) && !ex.getTitle().contains(this.searchText)) {
            return true;
        }
        if (!DEF_AUTHOR.equals(this.author) && !ex.getAuthor().contains(this.author)) {
            return true;
        }
        if (!DEF_PUBLISHER.equals(this.publisher) && !ex.getPublisher().contains(this.publisher)) {
            return true;
        }
        if (!DEF_BOOK.equals(this.book) && !ex.getBookTitle().contains(this.book)) {
            return true;
        }
        if (!DEF_TITLE.equals(this.title) && !ex.getTitle().contains(this.title)) {
            return true;
        }
        // TODO: add logic
        if (this.submitNo < 0) {
            return true;
        }
        if (!DEF_LANGUAGE.equals(this.language) && !ex.getLanguage().contains(this.language)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isFiltered(Test test) {
        if (!this.test) {
            return true;
        }
        if (!DEF_SEARCH_TEXT.equals(this.searchText) && !test.getDescription().contains(this.searchText)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isNoneFiltered() {
        return DEF_SEARCH_TEXT.equals(this.searchText) && DEF_AUTHOR.equals(this.author) && DEF_PUBLISHER.equals(this.publisher) && DEF_BOOK.equals(this.book)
                && DEF_TITLE.equals(this.title) && DEF_LANGUAGE.equals(this.language) && (this.submitNo == 0);
    }

}
