package org.junit.experimental.categories;

import org.junit.internal.Classes;
import org.junit.runner.FilterFactory;
import org.junit.runner.FilterFactoryParams;
import org.junit.runner.manipulation.Filter;

import java.util.ArrayList;
import java.util.List;

abstract class CategoryFilterFactory
        implements FilterFactory {
    public Filter createFilter(FilterFactoryParams params) throws FilterFactory.FilterNotCreatedException {
        try {
            return createFilter(parseCategories(params.getArgs()));
        } catch (ClassNotFoundException e) {
            throw new FilterFactory.FilterNotCreatedException(e);
        }
    }

    protected abstract Filter createFilter(List<Class<?>> paramList);

    private List<Class<?>> parseCategories(String categories) throws ClassNotFoundException {
        List<Class<?>> categoryClasses = new ArrayList<Class<?>>();

        for (String category : categories.split(",")) {
            Class<?> categoryClass = Classes.getClass(category);

            categoryClasses.add(categoryClass);
        }

        return categoryClasses;
    }
}

