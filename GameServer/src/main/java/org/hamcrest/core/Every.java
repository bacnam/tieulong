package org.hamcrest.core;

import org.hamcrest.*;

public class Every<T>
        extends TypeSafeDiagnosingMatcher<Iterable<T>> {
    private final Matcher<? super T> matcher;

    public Every(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    @Factory
    public static <U> Matcher<Iterable<U>> everyItem(Matcher<U> itemMatcher) {
        return (Matcher) new Every<U>(itemMatcher);
    }

    public boolean matchesSafely(Iterable<T> collection, Description mismatchDescription) {
        for (T t : collection) {
            if (!this.matcher.matches(t)) {
                mismatchDescription.appendText("an item ");
                this.matcher.describeMismatch(t, mismatchDescription);
                return false;
            }
        }
        return true;
    }

    public void describeTo(Description description) {
        description.appendText("every item is ").appendDescriptionOf((SelfDescribing) this.matcher);
    }
}

