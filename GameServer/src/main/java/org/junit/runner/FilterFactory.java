package org.junit.runner;

import org.junit.runner.manipulation.Filter;

public interface FilterFactory {
    Filter createFilter(FilterFactoryParams paramFilterFactoryParams) throws FilterNotCreatedException;

    public static class FilterNotCreatedException
            extends Exception {
        public FilterNotCreatedException(Exception exception) {
            super(exception.getMessage(), exception);
        }
    }
}

