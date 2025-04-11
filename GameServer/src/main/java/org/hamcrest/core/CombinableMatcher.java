package org.hamcrest.core;

import org.hamcrest.*;

import java.util.ArrayList;

public class CombinableMatcher<T> extends TypeSafeDiagnosingMatcher<T> {
    private final Matcher<? super T> matcher;

    public CombinableMatcher(Matcher<? super T> matcher) {
        this.matcher = matcher;
    }

    @Factory
    public static <LHS> CombinableBothMatcher<LHS> both(Matcher<? super LHS> matcher) {
        return new CombinableBothMatcher<LHS>(matcher);
    }

    @Factory
    public static <LHS> CombinableEitherMatcher<LHS> either(Matcher<? super LHS> matcher) {
        return new CombinableEitherMatcher<LHS>(matcher);
    }

    protected boolean matchesSafely(T item, Description mismatch) {
        if (!this.matcher.matches(item)) {
            this.matcher.describeMismatch(item, mismatch);
            return false;
        }
        return true;
    }

    public void describeTo(Description description) {
        description.appendDescriptionOf((SelfDescribing) this.matcher);
    }

    public CombinableMatcher<T> and(Matcher<? super T> other) {
        return new CombinableMatcher((Matcher<? super T>) new AllOf<T>(templatedListWith(other)));
    }

    public CombinableMatcher<T> or(Matcher<? super T> other) {
        return new CombinableMatcher((Matcher<? super T>) new AnyOf<T>(templatedListWith(other)));
    }

    private ArrayList<Matcher<? super T>> templatedListWith(Matcher<? super T> other) {
        ArrayList<Matcher<? super T>> matchers = new ArrayList<Matcher<? super T>>();
        matchers.add(this.matcher);
        matchers.add(other);
        return matchers;
    }

    public static final class CombinableBothMatcher<X> {
        private final Matcher<? super X> first;

        public CombinableBothMatcher(Matcher<? super X> matcher) {
            this.first = matcher;
        }

        public CombinableMatcher<X> and(Matcher<? super X> other) {
            return (new CombinableMatcher<X>(this.first)).and(other);
        }
    }

    public static final class CombinableEitherMatcher<X> {
        private final Matcher<? super X> first;

        public CombinableEitherMatcher(Matcher<? super X> matcher) {
            this.first = matcher;
        }

        public CombinableMatcher<X> or(Matcher<? super X> other) {
            return (new CombinableMatcher<X>(this.first)).or(other);
        }
    }

}

