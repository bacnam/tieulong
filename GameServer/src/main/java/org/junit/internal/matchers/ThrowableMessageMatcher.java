package org.junit.internal.matchers;

import org.hamcrest.*;
import org.hamcrest.TypeSafeMatcher;

public class ThrowableMessageMatcher<T extends Throwable>
        extends TypeSafeMatcher<T> {
    private final Matcher<String> matcher;

    public ThrowableMessageMatcher(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    @Factory
    public static <T extends Throwable> Matcher<T> hasMessage(Matcher<String> matcher) {
        return (Matcher) new ThrowableMessageMatcher<Throwable>(matcher);
    }

    public void describeTo(Description description) {
        description.appendText("exception with message ");
        description.appendDescriptionOf((SelfDescribing) this.matcher);
    }

    protected boolean matchesSafely(T item) {
        return this.matcher.matches(item.getMessage());
    }

    protected void describeMismatchSafely(T item, Description description) {
        description.appendText("message ");
        this.matcher.describeMismatch(item.getMessage(), description);
    }
}

