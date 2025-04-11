package org.junit.experimental.categories;

import org.junit.runner.manipulation.Filter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class IncludeCategories
        extends CategoryFilterFactory {
    protected Filter createFilter(List<Class<?>> categories) {
        return new IncludesAny(categories);
    }

    private static class IncludesAny extends Categories.CategoryFilter {
        public IncludesAny(List<Class<?>> categories) {
            this(new HashSet<Class<?>>(categories));
        }

        public IncludesAny(Set<Class<?>> categories) {
            super(true, categories, true, null);
        }

        public String describe() {
            return "includes " + super.describe();
        }
    }
}

