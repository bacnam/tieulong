package org.junit.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.TypeSafeMatcher;

public class ThrowableCauseMatcher<T extends Throwable>
extends TypeSafeMatcher<T>
{
private final Matcher<? extends Throwable> causeMatcher;

public ThrowableCauseMatcher(Matcher<? extends Throwable> causeMatcher) {
this.causeMatcher = causeMatcher;
}

public void describeTo(Description description) {
description.appendText("exception with cause ");
description.appendDescriptionOf((SelfDescribing)this.causeMatcher);
}

protected boolean matchesSafely(T item) {
return this.causeMatcher.matches(item.getCause());
}

protected void describeMismatchSafely(T item, Description description) {
description.appendText("cause ");
this.causeMatcher.describeMismatch(item.getCause(), description);
}

@Factory
public static <T extends Throwable> Matcher<T> hasCause(Matcher<? extends Throwable> matcher) {
return (Matcher)new ThrowableCauseMatcher<Throwable>(matcher);
}
}

