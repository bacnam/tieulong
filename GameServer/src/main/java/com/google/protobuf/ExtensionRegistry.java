package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ExtensionRegistry extends ExtensionRegistryLite {

    private static final ExtensionRegistry EMPTY = new ExtensionRegistry(true);
    private final Map<String, ExtensionInfo> extensionsByName;
    private final Map<DescriptorIntPair, ExtensionInfo> extensionsByNumber;

    private ExtensionRegistry() {
        this.extensionsByName = new HashMap<String, ExtensionInfo>();
        this.extensionsByNumber = new HashMap<DescriptorIntPair, ExtensionInfo>();
    }

    private ExtensionRegistry(ExtensionRegistry other) {
        super(other);
        this.extensionsByName = Collections.unmodifiableMap(other.extensionsByName);
        this.extensionsByNumber =
                Collections.unmodifiableMap(other.extensionsByNumber);
    }

    private ExtensionRegistry(boolean empty) {
        super(ExtensionRegistryLite.getEmptyRegistry());
        this.extensionsByName = Collections.<String, ExtensionInfo>emptyMap();
        this.extensionsByNumber =
                Collections.<DescriptorIntPair, ExtensionInfo>emptyMap();
    }

    public static ExtensionRegistry newInstance() {
        return new ExtensionRegistry();
    }

    public static ExtensionRegistry getEmptyRegistry() {
        return EMPTY;
    }

    @Override
    public ExtensionRegistry getUnmodifiable() {
        return new ExtensionRegistry(this);
    }

    public ExtensionInfo findExtensionByName(final String fullName) {
        return extensionsByName.get(fullName);
    }

    public ExtensionInfo findExtensionByNumber(final Descriptor containingType,
                                               final int fieldNumber) {
        return extensionsByNumber.get(
                new DescriptorIntPair(containingType, fieldNumber));
    }

    public void add(final GeneratedMessage.GeneratedExtension<?, ?> extension) {
        if (extension.getDescriptor().getJavaType() ==
                FieldDescriptor.JavaType.MESSAGE) {
            if (extension.getMessageDefaultInstance() == null) {
                throw new IllegalStateException(
                        "Registered message-type extension had null default instance: " +
                                extension.getDescriptor().getFullName());
            }
            add(new ExtensionInfo(extension.getDescriptor(),
                    extension.getMessageDefaultInstance()));
        } else {
            add(new ExtensionInfo(extension.getDescriptor(), null));
        }
    }

    public void add(final FieldDescriptor type) {
        if (type.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
            throw new IllegalArgumentException(
                    "ExtensionRegistry.add() must be provided a default instance when " +
                            "adding an embedded message extension.");
        }
        add(new ExtensionInfo(type, null));
    }

    public void add(final FieldDescriptor type, final Message defaultInstance) {
        if (type.getJavaType() != FieldDescriptor.JavaType.MESSAGE) {
            throw new IllegalArgumentException(
                    "ExtensionRegistry.add() provided a default instance for a " +
                            "non-message extension.");
        }
        add(new ExtensionInfo(type, defaultInstance));
    }

    private void add(final ExtensionInfo extension) {
        if (!extension.descriptor.isExtension()) {
            throw new IllegalArgumentException(
                    "ExtensionRegistry.add() was given a FieldDescriptor for a regular " +
                            "(non-extension) field.");
        }

        extensionsByName.put(extension.descriptor.getFullName(), extension);
        extensionsByNumber.put(
                new DescriptorIntPair(extension.descriptor.getContainingType(),
                        extension.descriptor.getNumber()),
                extension);

        final FieldDescriptor field = extension.descriptor;
        if (field.getContainingType().getOptions().getMessageSetWireFormat() &&
                field.getType() == FieldDescriptor.Type.MESSAGE &&
                field.isOptional() &&
                field.getExtensionScope() == field.getMessageType()) {

            extensionsByName.put(field.getMessageType().getFullName(), extension);
        }
    }

    public static final class ExtensionInfo {

        public final FieldDescriptor descriptor;

        public final Message defaultInstance;

        private ExtensionInfo(final FieldDescriptor descriptor) {
            this.descriptor = descriptor;
            defaultInstance = null;
        }

        private ExtensionInfo(final FieldDescriptor descriptor,
                              final Message defaultInstance) {
            this.descriptor = descriptor;
            this.defaultInstance = defaultInstance;
        }
    }

    private static final class DescriptorIntPair {
        private final Descriptor descriptor;
        private final int number;

        DescriptorIntPair(final Descriptor descriptor, final int number) {
            this.descriptor = descriptor;
            this.number = number;
        }

        @Override
        public int hashCode() {
            return descriptor.hashCode() * ((1 << 16) - 1) + number;
        }

        @Override
        public boolean equals(final Object obj) {
            if (!(obj instanceof DescriptorIntPair)) {
                return false;
            }
            final DescriptorIntPair other = (DescriptorIntPair) obj;
            return descriptor == other.descriptor && number == other.number;
        }
    }
}
