package org.junit.internal.matchers;

import org.hamcrest.*;
import org.hamcrest.TypeSafeMatcher;

public class ThrowableCauseMatcher<T extends Throwable>
        extends TypeSafeMatcher<T> {
    private final Matcher<? extends Throwable> causeMatcher;

    public ThrowableCauseMatcher(Matcher<? extends Throwable> causeMatcher) {
        this.causeMatcher = causeMatcher;
    }

    @Factory
    public static <T extends Throwable> Matcher<T> hasCause(Matcher<? extends Throwable> matcher) {
        return (Matcher) new ThrowableCauseMatcher<Throwable>(matcher);
    }

    public void describeTo(Description description) {
        description.appendText("exception with cause ");
        description.appendDescriptionOf((SelfDescribing) this.causeMatcher);
    }

    protected boolean matchesSafely(T item) {
        return this.causeMatcher.matches(item.getCause());
    }

    protected void describeMismatchSafely(T item, Description description) {
        description.appendText("cause ");
        this.causeMatcher.describeMismatch(item.getCause(), description);
    }
}

