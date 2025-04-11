package org.junit.rules;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.matchers.JUnitMatchers;

import java.util.ArrayList;
import java.util.List;

class ExpectedExceptionMatcherBuilder {
    private final List<Matcher<?>> matchers = new ArrayList<Matcher<?>>();

    void add(Matcher<?> matcher) {
        this.matchers.add(matcher);
    }

    boolean expectsThrowable() {
        return !this.matchers.isEmpty();
    }

    Matcher<Throwable> build() {
        return JUnitMatchers.isThrowable(allOfTheMatchers());
    }

    private Matcher<Throwable> allOfTheMatchers() {
        if (this.matchers.size() == 1) {
            return cast(this.matchers.get(0));
        }
        return CoreMatchers.allOf(castedMatchers());
    }

    private List<Matcher<? super Throwable>> castedMatchers() {
        return (List) new ArrayList<Matcher<?>>(this.matchers);
    }

    private Matcher<Throwable> cast(Matcher<?> singleMatcher) {
        return (Matcher) singleMatcher;
    }
}

