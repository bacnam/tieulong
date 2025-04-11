package org.junit.internal.runners.rules;

import org.junit.runners.model.FrameworkMember;

import java.lang.annotation.Annotation;

class ValidationError
        extends Exception {
    public ValidationError(FrameworkMember<?> member, Class<? extends Annotation> annotation, String suffix) {
        super(String.format("The @%s '%s' %s", new Object[]{annotation.getSimpleName(), member.getName(), suffix}));
    }
}

