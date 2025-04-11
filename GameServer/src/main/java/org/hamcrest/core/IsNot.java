package org.hamcrest.core;

import org.hamcrest.*;

public class IsNot<T>
        extends BaseMatcher<T> {
    private final Matcher<T> matcher;

    public IsNot(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    @Factory
    public static <T> Matcher<T> not(Matcher<T> matcher) {
        return (Matcher<T>) new IsNot<T>(matcher);
    }

    @Factory
    public static <T> Matcher<T> not(T value) {
        return not(IsEqual.equalTo(value));
    }

    public boolean matches(Object arg) {
        return !this.matcher.matches(arg);
    }

    public void describeTo(Description description) {
        description.appendText("not ").appendDescriptionOf((SelfDescribing) this.matcher);
    }
}

