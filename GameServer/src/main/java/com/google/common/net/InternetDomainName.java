package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.*;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.util.List;

@Beta
@GwtCompatible(emulated = true)
public final class InternetDomainName {
    private static final CharMatcher DOTS_MATCHER = CharMatcher.anyOf(".。．｡");

    private static final Splitter DOT_SPLITTER = Splitter.on('.');
    private static final Joiner DOT_JOINER = Joiner.on('.');

    private static final int NO_PUBLIC_SUFFIX_FOUND = -1;

    private static final String DOT_REGEX = "\\.";

    private static final int MAX_PARTS = 127;

    private static final int MAX_LENGTH = 253;

    private static final int MAX_DOMAIN_PART_LENGTH = 63;
    private static final CharMatcher DASH_MATCHER = CharMatcher.anyOf("-_");
    private static final CharMatcher PART_CHAR_MATCHER = CharMatcher.JAVA_LETTER_OR_DIGIT.or(DASH_MATCHER);
    private final String name;
    private final ImmutableList<String> parts;
    private final int publicSuffixIndex;

    InternetDomainName(String name) {
        name = Ascii.toLowerCase(DOTS_MATCHER.replaceFrom(name, '.'));

        if (name.endsWith(".")) {
            name = name.substring(0, name.length() - 1);
        }

        Preconditions.checkArgument((name.length() <= 253), "Domain name too long: '%s':", new Object[]{name});
        this.name = name;

        this.parts = ImmutableList.copyOf(DOT_SPLITTER.split(name));
        Preconditions.checkArgument((this.parts.size() <= 127), "Domain has too many parts: '%s'", new Object[]{name});
        Preconditions.checkArgument(validateSyntax((List<String>) this.parts), "Not a valid domain name: '%s'", new Object[]{name});

        this.publicSuffixIndex = findPublicSuffix();
    }

    @Deprecated
    public static InternetDomainName fromLenient(String domain) {
        return from(domain);
    }

    public static InternetDomainName from(String domain) {
        return new InternetDomainName((String) Preconditions.checkNotNull(domain));
    }

    private static boolean validateSyntax(List<String> parts) {
        int lastIndex = parts.size() - 1;

        if (!validatePart(parts.get(lastIndex), true)) {
            return false;
        }

        for (int i = 0; i < lastIndex; i++) {
            String part = parts.get(i);
            if (!validatePart(part, false)) {
                return false;
            }
        }

        return true;
    }

    private static boolean validatePart(String part, boolean isFinalPart) {
        if (part.length() < 1 || part.length() > 63) {
            return false;
        }

        String asciiChars = CharMatcher.ASCII.retainFrom(part);

        if (!PART_CHAR_MATCHER.matchesAllOf(asciiChars)) {
            return false;
        }

        if (DASH_MATCHER.matches(part.charAt(0)) || DASH_MATCHER.matches(part.charAt(part.length() - 1))) {
            return false;
        }

        if (isFinalPart && CharMatcher.DIGIT.matches(part.charAt(0))) {
            return false;
        }

        return true;
    }

    @Deprecated
    public static boolean isValidLenient(String name) {
        return isValid(name);
    }

    public static boolean isValid(String name) {
        try {
            from(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean matchesWildcardPublicSuffix(String domain) {
        String[] pieces = domain.split("\\.", 2);
        return (pieces.length == 2 && TldPatterns.UNDER.contains(pieces[1]));
    }

    private int findPublicSuffix() {
        int partsSize = this.parts.size();

        for (int i = 0; i < partsSize; i++) {
            String ancestorName = DOT_JOINER.join((Iterable) this.parts.subList(i, partsSize));

            if (TldPatterns.EXACT.contains(ancestorName)) {
                return i;
            }

            if (TldPatterns.EXCLUDED.contains(ancestorName)) {
                return i + 1;
            }

            if (matchesWildcardPublicSuffix(ancestorName)) {
                return i;
            }
        }

        return -1;
    }

    public String name() {
        return this.name;
    }

    public ImmutableList<String> parts() {
        return this.parts;
    }

    public boolean isPublicSuffix() {
        return (this.publicSuffixIndex == 0);
    }

    public boolean hasPublicSuffix() {
        return (this.publicSuffixIndex != -1);
    }

    public InternetDomainName publicSuffix() {
        return hasPublicSuffix() ? ancestor(this.publicSuffixIndex) : null;
    }

    public boolean isUnderPublicSuffix() {
        return (this.publicSuffixIndex > 0);
    }

    public boolean isTopPrivateDomain() {
        return (this.publicSuffixIndex == 1);
    }

    public InternetDomainName topPrivateDomain() {
        if (isTopPrivateDomain()) {
            return this;
        }
        Preconditions.checkState(isUnderPublicSuffix(), "Not under a public suffix: %s", new Object[]{this.name});
        return ancestor(this.publicSuffixIndex - 1);
    }

    public boolean hasParent() {
        return (this.parts.size() > 1);
    }

    public InternetDomainName parent() {
        Preconditions.checkState(hasParent(), "Domain '%s' has no parent", new Object[]{this.name});
        return ancestor(1);
    }

    private InternetDomainName ancestor(int levels) {
        return from(DOT_JOINER.join((Iterable) this.parts.subList(levels, this.parts.size())));
    }

    public InternetDomainName child(String leftParts) {
        return from((String) Preconditions.checkNotNull(leftParts) + "." + this.name);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("name", this.name).toString();
    }

    public boolean equals(@Nullable Object object) {
        if (object == this) {
            return true;
        }

        if (object instanceof InternetDomainName) {
            InternetDomainName that = (InternetDomainName) object;
            return this.name.equals(that.name);
        }

        return false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}

