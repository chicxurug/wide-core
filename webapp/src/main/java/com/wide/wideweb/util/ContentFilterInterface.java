package com.wide.wideweb.util;

import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Test;

public interface ContentFilterInterface {

    public boolean isFiltered(Category cat);

    public boolean isFiltered(Exercise ex);

    public boolean isFiltered(Test test);

    public boolean isNoneFiltered();
}
