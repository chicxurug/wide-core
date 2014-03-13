package com.wide.wideweb.util;

import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Test;

public class EmptyFilter implements ContentFilterInterface {

    @Override
    public boolean isFiltered(Category cat) {
        return false;
    }

    @Override
    public boolean isFiltered(Exercise ex) {
        return false;
    }

    @Override
    public boolean isFiltered(Test test) {
        return false;
    }

}
