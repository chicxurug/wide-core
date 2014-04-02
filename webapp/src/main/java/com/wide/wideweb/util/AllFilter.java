package com.wide.wideweb.util;

import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Test;

public class AllFilter implements ContentFilterInterface {

    private String searchText;

    public AllFilter(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public boolean isFiltered(Category cat) {
        if (!cat.getName().contains(this.searchText)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isFiltered(Exercise ex) {
        if (!ex.getAuthor().contains(this.searchText) && !ex.getTitle().contains(this.searchText) && !ex.getBookTitle().contains(this.searchText)
                && !ex.getPublisher().contains(this.searchText)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isFiltered(Test test) {
        if (!test.getDescription().contains(this.searchText)) {
            return true;
        }
        return false;
    }

}
