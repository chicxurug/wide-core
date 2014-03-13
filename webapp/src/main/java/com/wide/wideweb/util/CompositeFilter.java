package com.wide.wideweb.util;

import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Test;

public class CompositeFilter implements ContentFilterInterface {

    private String searchText;
    private String author;
    private String publisher;
    private String book;
    private String title;
    private int submitNo;
    private boolean menu;
    private boolean lesson;
    private boolean test;

    public CompositeFilter(String searchText, String author, String publisher, String book, String title, String submitNo, boolean menu, boolean lesson,
            boolean test) {
        this.searchText = searchText;
        this.author = author;
        this.publisher = publisher;
        this.book = book;
        this.title = title;
        this.submitNo = "Submits No.".equals(submitNo) ? 0 : Integer.parseInt(submitNo);
        this.menu = menu;
        this.lesson = lesson;
        this.test = test;
    }

    @Override
    public boolean isFiltered(Category cat) {
        if (!this.menu) {
            return true;
        }
        if (!"Enter search keywords here".equals(this.searchText) && !cat.getName().contains(this.searchText)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isFiltered(Exercise ex) {
        if (!this.lesson) {
            return true;
        }
        if (!"Enter search keywords here".equals(this.searchText) && !ex.getTitle().contains(this.searchText)) {
            return true;
        }
        if (!"Author".equals(this.author) && !ex.getAuthor().contains(this.author)) {
            return true;
        }
        if (!"Publisher".equals(this.publisher) && !ex.getPublisher().contains(this.publisher)) {
            return true;
        }
        if (!"Book".equals(this.book) && !ex.getBook_title().contains(this.book)) {
            return true;
        }
        if (!"Title".equals(this.title) && !ex.getTitle().contains(this.title)) {
            return true;
        }
        // TODO: add logic
        if (this.submitNo < 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isFiltered(Test test) {
        if (!this.test) {
            return true;
        }
        return false;
    }

}
