package com.google.protobuf;

import com.google.protobuf.DescriptorProtos.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

public final class Descriptors {

    private static String computeFullName(final FileDescriptor file,
                                          final Descriptor parent,
                                          final String name) {
        if (parent != null) {
            return parent.getFullName() + '.' + name;
        } else if (file.getPackage().length() > 0) {
            return file.getPackage() + '.' + name;
        } else {
            return name;
        }
    }

    private interface GenericDescriptor {
        Message toProto();

        String getName();

        String getFullName();

        FileDescriptor getFile();
    }

    public static final class FileDescriptor {

        private final Descriptor[] messageTypes;
        private final EnumDescriptor[] enumTypes;
        private final ServiceDescriptor[] services;
        private final FieldDescriptor[] extensions;
        private final FileDescriptor[] dependencies;
        private final FileDescriptor[] publicDependencies;
        private final DescriptorPool pool;
        private FileDescriptorProto proto;

        private FileDescriptor(final FileDescriptorProto proto,
                               final FileDescriptor[] dependencies,
                               final DescriptorPool pool)
                throws DescriptorValidationException {
            this.pool = pool;
            this.proto = proto;
            this.dependencies = dependencies.clone();
            this.publicDependencies =
                    new FileDescriptor[proto.getPublicDependencyCount()];
            for (int i = 0; i < proto.getPublicDependencyCount(); i++) {
                int index = proto.getPublicDependency(i);
                if (index < 0 || index >= this.dependencies.length) {
                    throw new DescriptorValidationException(this,
                            "Invalid public dependency index.");
                }
                this.publicDependencies[i] =
                        this.dependencies[proto.getPublicDependency(i)];
            }

            pool.addPackage(getPackage(), this);

            messageTypes = new Descriptor[proto.getMessageTypeCount()];
            for (int i = 0; i < proto.getMessageTypeCount(); i++) {
                messageTypes[i] =
                        new Descriptor(proto.getMessageType(i), this, null, i);
            }

            enumTypes = new EnumDescriptor[proto.getEnumTypeCount()];
            for (int i = 0; i < proto.getEnumTypeCount(); i++) {
                enumTypes[i] = new EnumDescriptor(proto.getEnumType(i), this, null, i);
            }

            services = new ServiceDescriptor[proto.getServiceCount()];
            for (int i = 0; i < proto.getServiceCount(); i++) {
                services[i] = new ServiceDescriptor(proto.getService(i), this, i);
            }

            extensions = new FieldDescriptor[proto.getExtensionCount()];
            for (int i = 0; i < proto.getExtensionCount(); i++) {
                extensions[i] = new FieldDescriptor(
                        proto.getExtension(i), this, null, i, true);
            }
        }

        public static FileDescriptor buildFrom(final FileDescriptorProto proto,
                                               final FileDescriptor[] dependencies)
                throws DescriptorValidationException {

            final DescriptorPool pool = new DescriptorPool(dependencies);
            final FileDescriptor result =
                    new FileDescriptor(proto, dependencies, pool);

            if (dependencies.length != proto.getDependencyCount()) {
                throw new DescriptorValidationException(result,
                        "Dependencies passed to FileDescriptor.buildFrom() don't match " +
                                "those listed in the FileDescriptorProto.");
            }
            for (int i = 0; i < proto.getDependencyCount(); i++) {
                if (!dependencies[i].getName().equals(proto.getDependency(i))) {
                    throw new DescriptorValidationException(result,
                            "Dependencies passed to FileDescriptor.buildFrom() don't match " +
                                    "those listed in the FileDescriptorProto.");
                }
            }

            result.crossLink();
            return result;
        }

        public static void internalBuildGeneratedFileFrom(
                final String[] descriptorDataParts,
                final FileDescriptor[] dependencies,
                final InternalDescriptorAssigner descriptorAssigner) {

            StringBuilder descriptorData = new StringBuilder();
            for (String part : descriptorDataParts) {
                descriptorData.append(part);
            }

            final byte[] descriptorBytes;
            try {
                descriptorBytes = descriptorData.toString().getBytes("ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(
                        "Standard encoding ISO-8859-1 not supported by JVM.", e);
            }

            FileDescriptorProto proto;
            try {
                proto = FileDescriptorProto.parseFrom(descriptorBytes);
            } catch (InvalidProtocolBufferException e) {
                throw new IllegalArgumentException(
                        "Failed to parse protocol buffer descriptor for generated code.", e);
            }

            final FileDescriptor result;
            try {
                result = buildFrom(proto, dependencies);
            } catch (DescriptorValidationException e) {
                throw new IllegalArgumentException(
                        "Invalid embedded descriptor for \"" + proto.getName() + "\".", e);
            }

            final ExtensionRegistry registry =
                    descriptorAssigner.assignDescriptors(result);

            if (registry != null) {

                try {
                    proto = FileDescriptorProto.parseFrom(descriptorBytes, registry);
                } catch (InvalidProtocolBufferException e) {
                    throw new IllegalArgumentException(
                            "Failed to parse protocol buffer descriptor for generated code.",
                            e);
                }

                result.setProto(proto);
            }
        }

        public FileDescriptorProto toProto() {
            return proto;
        }

        public String getName() {
            return proto.getName();
        }

        public String getPackage() {
            return proto.getPackage();
        }

        public FileOptions getOptions() {
            return proto.getOptions();
        }

        public List<Descriptor> getMessageTypes() {
            return Collections.unmodifiableList(Arrays.asList(messageTypes));
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(enumTypes));
        }

        public List<ServiceDescriptor> getServices() {
            return Collections.unmodifiableList(Arrays.asList(services));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(extensions));
        }

        public List<FileDescriptor> getDependencies() {
            return Collections.unmodifiableList(Arrays.asList(dependencies));
        }

        public List<FileDescriptor> getPublicDependencies() {
            return Collections.unmodifiableList(Arrays.asList(publicDependencies));
        }

        public Descriptor findMessageTypeByName(String name) {

            if (name.indexOf('.') != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                name = getPackage() + '.' + name;
            }
            final GenericDescriptor result = pool.findSymbol(name);
            if (result != null && result instanceof Descriptor &&
                    result.getFile() == this) {
                return (Descriptor) result;
            } else {
                return null;
            }
        }

        public EnumDescriptor findEnumTypeByName(String name) {

            if (name.indexOf('.') != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                name = getPackage() + '.' + name;
            }
            final GenericDescriptor result = pool.findSymbol(name);
            if (result != null && result instanceof EnumDescriptor &&
                    result.getFile() == this) {
                return (EnumDescriptor) result;
            } else {
                return null;
            }
        }

        public ServiceDescriptor findServiceByName(String name) {

            if (name.indexOf('.') != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                name = getPackage() + '.' + name;
            }
            final GenericDescriptor result = pool.findSymbol(name);
            if (result != null && result instanceof ServiceDescriptor &&
                    result.getFile() == this) {
                return (ServiceDescriptor) result;
            } else {
                return null;
            }
        }

        public FieldDescriptor findExtensionByName(String name) {
            if (name.indexOf('.') != -1) {
                return null;
            }
            if (getPackage().length() > 0) {
                name = getPackage() + '.' + name;
            }
            final GenericDescriptor result = pool.findSymbol(name);
            if (result != null && result instanceof FieldDescriptor &&
                    result.getFile() == this) {
                return (FieldDescriptor) result;
            } else {
                return null;
            }
        }

        private void crossLink() throws DescriptorValidationException {
            for (final Descriptor messageType : messageTypes) {
                messageType.crossLink();
            }

            for (final ServiceDescriptor service : services) {
                service.crossLink();
            }

            for (final FieldDescriptor extension : extensions) {
                extension.crossLink();
            }
        }

        private void setProto(final FileDescriptorProto proto) {
            this.proto = proto;

            for (int i = 0; i < messageTypes.length; i++) {
                messageTypes[i].setProto(proto.getMessageType(i));
            }

            for (int i = 0; i < enumTypes.length; i++) {
                enumTypes[i].setProto(proto.getEnumType(i));
            }

            for (int i = 0; i < services.length; i++) {
                services[i].setProto(proto.getService(i));
            }

            for (int i = 0; i < extensions.length; i++) {
                extensions[i].setProto(proto.getExtension(i));
            }
        }

        public interface InternalDescriptorAssigner {
            ExtensionRegistry assignDescriptors(FileDescriptor root);
        }
    }

    public static final class Descriptor implements GenericDescriptor {

        private final int index;
        private final String fullName;
        private final FileDescriptor file;
        private final Descriptor containingType;
        private final Descriptor[] nestedTypes;
        private final EnumDescriptor[] enumTypes;
        private final FieldDescriptor[] fields;
        private final FieldDescriptor[] extensions;
        private DescriptorProto proto;

        private Descriptor(final DescriptorProto proto,
                           final FileDescriptor file,
                           final Descriptor parent,
                           final int index)
                throws DescriptorValidationException {
            this.index = index;
            this.proto = proto;
            fullName = computeFullName(file, parent, proto.getName());
            this.file = file;
            containingType = parent;

            nestedTypes = new Descriptor[proto.getNestedTypeCount()];
            for (int i = 0; i < proto.getNestedTypeCount(); i++) {
                nestedTypes[i] = new Descriptor(
                        proto.getNestedType(i), file, this, i);
            }

            enumTypes = new EnumDescriptor[proto.getEnumTypeCount()];
            for (int i = 0; i < proto.getEnumTypeCount(); i++) {
                enumTypes[i] = new EnumDescriptor(
                        proto.getEnumType(i), file, this, i);
            }

            fields = new FieldDescriptor[proto.getFieldCount()];
            for (int i = 0; i < proto.getFieldCount(); i++) {
                fields[i] = new FieldDescriptor(
                        proto.getField(i), file, this, i, false);
            }

            extensions = new FieldDescriptor[proto.getExtensionCount()];
            for (int i = 0; i < proto.getExtensionCount(); i++) {
                extensions[i] = new FieldDescriptor(
                        proto.getExtension(i), file, this, i, true);
            }

            file.pool.addSymbol(this);
        }

        public int getIndex() {
            return index;
        }

        public DescriptorProto toProto() {
            return proto;
        }

        public String getName() {
            return proto.getName();
        }

        public String getFullName() {
            return fullName;
        }

        public FileDescriptor getFile() {
            return file;
        }

        public Descriptor getContainingType() {
            return containingType;
        }

        public MessageOptions getOptions() {
            return proto.getOptions();
        }

        public List<FieldDescriptor> getFields() {
            return Collections.unmodifiableList(Arrays.asList(fields));
        }

        public List<FieldDescriptor> getExtensions() {
            return Collections.unmodifiableList(Arrays.asList(extensions));
        }

        public List<Descriptor> getNestedTypes() {
            return Collections.unmodifiableList(Arrays.asList(nestedTypes));
        }

        public List<EnumDescriptor> getEnumTypes() {
            return Collections.unmodifiableList(Arrays.asList(enumTypes));
        }

        public boolean isExtensionNumber(final int number) {
            for (final DescriptorProto.ExtensionRange range :
                    proto.getExtensionRangeList()) {
                if (range.getStart() <= number && number < range.getEnd()) {
                    return true;
                }
            }
            return false;
        }

        public FieldDescriptor findFieldByName(final String name) {
            final GenericDescriptor result =
                    file.pool.findSymbol(fullName + '.' + name);
            if (result != null && result instanceof FieldDescriptor) {
                return (FieldDescriptor) result;
            } else {
                return null;
            }
        }

        public FieldDescriptor findFieldByNumber(final int number) {
            return file.pool.fieldsByNumber.get(
                    new DescriptorPool.DescriptorIntPair(this, number));
        }

        public Descriptor findNestedTypeByName(final String name) {
            final GenericDescriptor result =
                    file.pool.findSymbol(fullName + '.' + name);
            if (result != null && result instanceof Descriptor) {
                return (Descriptor) result;
            } else {
                return null;
            }
        }

        public EnumDescriptor findEnumTypeByName(final String name) {
            final GenericDescriptor result =
                    file.pool.findSymbol(fullName + '.' + name);
            if (result != null && result instanceof EnumDescriptor) {
                return (EnumDescriptor) result;
            } else {
                return null;
            }
        }

        private void crossLink() throws DescriptorValidationException {
            for (final Descriptor nestedType : nestedTypes) {
                nestedType.crossLink();
            }

            for (final FieldDescriptor field : fields) {
                field.crossLink();
            }

            for (final FieldDescriptor extension : extensions) {
                extension.crossLink();
            }
        }

        private void setProto(final DescriptorProto proto) {
            this.proto = proto;

            for (int i = 0; i < nestedTypes.length; i++) {
                nestedTypes[i].setProto(proto.getNestedType(i));
            }

            for (int i = 0; i < enumTypes.length; i++) {
                enumTypes[i].setProto(proto.getEnumType(i));
            }

            for (int i = 0; i < fields.length; i++) {
                fields[i].setProto(proto.getField(i));
            }

            for (int i = 0; i < extensions.length; i++) {
                extensions[i].setProto(proto.getExtension(i));
            }
        }
    }

    public static final class FieldDescriptor
            implements GenericDescriptor, Comparable<FieldDescriptor>,
            FieldSet.FieldDescriptorLite<FieldDescriptor> {

        private static final WireFormat.FieldType[] table =
                WireFormat.FieldType.values();

        static {

            if (Type.values().length != FieldDescriptorProto.Type.values().length) {
                throw new RuntimeException(
                        "descriptor.proto has a new declared type but Desrciptors.java " +
                                "wasn't updated.");
            }
        }

        private final int index;
        private final String fullName;
        private final FileDescriptor file;
        private final Descriptor extensionScope;
        private FieldDescriptorProto proto;
        private Type type;
        private Descriptor containingType;
        private Descriptor messageType;
        private EnumDescriptor enumType;
        private Object defaultValue;

        private FieldDescriptor(final FieldDescriptorProto proto,
                                final FileDescriptor file,
                                final Descriptor parent,
                                final int index,
                                final boolean isExtension)
                throws DescriptorValidationException {
            this.index = index;
            this.proto = proto;
            fullName = computeFullName(file, parent, proto.getName());
            this.file = file;

            if (proto.hasType()) {
                type = Type.valueOf(proto.getType());
            }

            if (getNumber() <= 0) {
                throw new DescriptorValidationException(this,
                        "Field numbers must be positive integers.");
            }

            if (proto.getOptions().getPacked() && !isPackable()) {
                throw new DescriptorValidationException(this,
                        "[packed = true] can only be specified for repeated primitive " +
                                "fields.");
            }

            if (isExtension) {
                if (!proto.hasExtendee()) {
                    throw new DescriptorValidationException(this,
                            "FieldDescriptorProto.extendee not set for extension field.");
                }
                containingType = null;
                if (parent != null) {
                    extensionScope = parent;
                } else {
                    extensionScope = null;
                }
            } else {
                if (proto.hasExtendee()) {
                    throw new DescriptorValidationException(this,
                            "FieldDescriptorProto.extendee set for non-extension field.");
                }
                containingType = parent;
                extensionScope = null;
            }

            file.pool.addSymbol(this);
        }

        public int getIndex() {
            return index;
        }

        public FieldDescriptorProto toProto() {
            return proto;
        }

        public String getName() {
            return proto.getName();
        }

        public int getNumber() {
            return proto.getNumber();
        }

        public String getFullName() {
            return fullName;
        }

        public JavaType getJavaType() {
            return type.getJavaType();
        }

        public WireFormat.JavaType getLiteJavaType() {
            return getLiteType().getJavaType();
        }

        public FileDescriptor getFile() {
            return file;
        }

        public Type getType() {
            return type;
        }

        public WireFormat.FieldType getLiteType() {
            return table[type.ordinal()];
        }

        public boolean isRequired() {
            return proto.getLabel() == FieldDescriptorProto.Label.LABEL_REQUIRED;
        }

        public boolean isOptional() {
            return proto.getLabel() == FieldDescriptorProto.Label.LABEL_OPTIONAL;
        }

        public boolean isRepeated() {
            return proto.getLabel() == FieldDescriptorProto.Label.LABEL_REPEATED;
        }

        public boolean isPacked() {
            return getOptions().getPacked();
        }

        public boolean isPackable() {
            return isRepeated() && getLiteType().isPackable();
        }

        public boolean hasDefaultValue() {
            return proto.hasDefaultValue();
        }

        public Object getDefaultValue() {
            if (getJavaType() == JavaType.MESSAGE) {
                throw new UnsupportedOperationException(
                        "FieldDescriptor.getDefaultValue() called on an embedded message " +
                                "field.");
            }
            return defaultValue;
        }

        public FieldOptions getOptions() {
            return proto.getOptions();
        }

        public boolean isExtension() {
            return proto.hasExtendee();
        }

        public Descriptor getContainingType() {
            return containingType;
        }

        public Descriptor getExtensionScope() {
            if (!isExtension()) {
                throw new UnsupportedOperationException(
                        "This field is not an extension.");
            }
            return extensionScope;
        }

        public Descriptor getMessageType() {
            if (getJavaType() != JavaType.MESSAGE) {
                throw new UnsupportedOperationException(
                        "This field is not of message type.");
            }
            return messageType;
        }

        public EnumDescriptor getEnumType() {
            if (getJavaType() != JavaType.ENUM) {
                throw new UnsupportedOperationException(
                        "This field is not of enum type.");
            }
            return enumType;
        }

        public int compareTo(final FieldDescriptor other) {
            if (other.containingType != containingType) {
                throw new IllegalArgumentException(
                        "FieldDescriptors can only be compared to other FieldDescriptors " +
                                "for fields of the same message type.");
            }
            return getNumber() - other.getNumber();
        }

        private void crossLink() throws DescriptorValidationException {
            if (proto.hasExtendee()) {
                final GenericDescriptor extendee =
                        file.pool.lookupSymbol(proto.getExtendee(), this,
                                DescriptorPool.SearchFilter.TYPES_ONLY);
                if (!(extendee instanceof Descriptor)) {
                    throw new DescriptorValidationException(this,
                            '\"' + proto.getExtendee() + "\" is not a message type.");
                }
                containingType = (Descriptor) extendee;

                if (!getContainingType().isExtensionNumber(getNumber())) {
                    throw new DescriptorValidationException(this,
                            '\"' + getContainingType().getFullName() +
                                    "\" does not declare " + getNumber() +
                                    " as an extension number.");
                }
            }

            if (proto.hasTypeName()) {
                final GenericDescriptor typeDescriptor =
                        file.pool.lookupSymbol(proto.getTypeName(), this,
                                DescriptorPool.SearchFilter.TYPES_ONLY);

                if (!proto.hasType()) {

                    if (typeDescriptor instanceof Descriptor) {
                        type = Type.MESSAGE;
                    } else if (typeDescriptor instanceof EnumDescriptor) {
                        type = Type.ENUM;
                    } else {
                        throw new DescriptorValidationException(this,
                                '\"' + proto.getTypeName() + "\" is not a type.");
                    }
                }

                if (getJavaType() == JavaType.MESSAGE) {
                    if (!(typeDescriptor instanceof Descriptor)) {
                        throw new DescriptorValidationException(this,
                                '\"' + proto.getTypeName() + "\" is not a message type.");
                    }
                    messageType = (Descriptor) typeDescriptor;

                    if (proto.hasDefaultValue()) {
                        throw new DescriptorValidationException(this,
                                "Messages can't have default values.");
                    }
                } else if (getJavaType() == JavaType.ENUM) {
                    if (!(typeDescriptor instanceof EnumDescriptor)) {
                        throw new DescriptorValidationException(this,
                                '\"' + proto.getTypeName() + "\" is not an enum type.");
                    }
                    enumType = (EnumDescriptor) typeDescriptor;
                } else {
                    throw new DescriptorValidationException(this,
                            "Field with primitive type has type_name.");
                }
            } else {
                if (getJavaType() == JavaType.MESSAGE ||
                        getJavaType() == JavaType.ENUM) {
                    throw new DescriptorValidationException(this,
                            "Field with message or enum type missing type_name.");
                }
            }

            if (proto.hasDefaultValue()) {
                if (isRepeated()) {
                    throw new DescriptorValidationException(this,
                            "Repeated fields cannot have default values.");
                }

                try {
                    switch (getType()) {
                        case INT32:
                        case SINT32:
                        case SFIXED32:
                            defaultValue = TextFormat.parseInt32(proto.getDefaultValue());
                            break;
                        case UINT32:
                        case FIXED32:
                            defaultValue = TextFormat.parseUInt32(proto.getDefaultValue());
                            break;
                        case INT64:
                        case SINT64:
                        case SFIXED64:
                            defaultValue = TextFormat.parseInt64(proto.getDefaultValue());
                            break;
                        case UINT64:
                        case FIXED64:
                            defaultValue = TextFormat.parseUInt64(proto.getDefaultValue());
                            break;
                        case FLOAT:
                            if (proto.getDefaultValue().equals("inf")) {
                                defaultValue = Float.POSITIVE_INFINITY;
                            } else if (proto.getDefaultValue().equals("-inf")) {
                                defaultValue = Float.NEGATIVE_INFINITY;
                            } else if (proto.getDefaultValue().equals("nan")) {
                                defaultValue = Float.NaN;
                            } else {
                                defaultValue = Float.valueOf(proto.getDefaultValue());
                            }
                            break;
                        case DOUBLE:
                            if (proto.getDefaultValue().equals("inf")) {
                                defaultValue = Double.POSITIVE_INFINITY;
                            } else if (proto.getDefaultValue().equals("-inf")) {
                                defaultValue = Double.NEGATIVE_INFINITY;
                            } else if (proto.getDefaultValue().equals("nan")) {
                                defaultValue = Double.NaN;
                            } else {
                                defaultValue = Double.valueOf(proto.getDefaultValue());
                            }
                            break;
                        case BOOL:
                            defaultValue = Boolean.valueOf(proto.getDefaultValue());
                            break;
                        case STRING:
                            defaultValue = proto.getDefaultValue();
                            break;
                        case BYTES:
                            try {
                                defaultValue =
                                        TextFormat.unescapeBytes(proto.getDefaultValue());
                            } catch (TextFormat.InvalidEscapeSequenceException e) {
                                throw new DescriptorValidationException(this,
                                        "Couldn't parse default value: " + e.getMessage(), e);
                            }
                            break;
                        case ENUM:
                            defaultValue = enumType.findValueByName(proto.getDefaultValue());
                            if (defaultValue == null) {
                                throw new DescriptorValidationException(this,
                                        "Unknown enum default value: \"" +
                                                proto.getDefaultValue() + '\"');
                            }
                            break;
                        case MESSAGE:
                        case GROUP:
                            throw new DescriptorValidationException(this,
                                    "Message type had default value.");
                    }
                } catch (NumberFormatException e) {
                    throw new DescriptorValidationException(this,
                            "Could not parse default value: \"" +
                                    proto.getDefaultValue() + '\"', e);
                }
            } else {

                if (isRepeated()) {
                    defaultValue = Collections.emptyList();
                } else {
                    switch (getJavaType()) {
                        case ENUM:

                            defaultValue = enumType.getValues().get(0);
                            break;
                        case MESSAGE:
                            defaultValue = null;
                            break;
                        default:
                            defaultValue = getJavaType().defaultDefault;
                            break;
                    }
                }
            }

            if (!isExtension()) {
                file.pool.addFieldByNumber(this);
            }

            if (containingType != null &&
                    containingType.getOptions().getMessageSetWireFormat()) {
                if (isExtension()) {
                    if (!isOptional() || getType() != Type.MESSAGE) {
                        throw new DescriptorValidationException(this,
                                "Extensions of MessageSets must be optional messages.");
                    }
                } else {
                    throw new DescriptorValidationException(this,
                            "MessageSets cannot have fields, only extensions.");
                }
            }
        }

        private void setProto(final FieldDescriptorProto proto) {
            this.proto = proto;
        }

        public MessageLite.Builder internalMergeFrom(
                MessageLite.Builder to, MessageLite from) {

            return ((Message.Builder) to).mergeFrom((Message) from);
        }

        public enum Type {
            DOUBLE(JavaType.DOUBLE),
            FLOAT(JavaType.FLOAT),
            INT64(JavaType.LONG),
            UINT64(JavaType.LONG),
            INT32(JavaType.INT),
            FIXED64(JavaType.LONG),
            FIXED32(JavaType.INT),
            BOOL(JavaType.BOOLEAN),
            STRING(JavaType.STRING),
            GROUP(JavaType.MESSAGE),
            MESSAGE(JavaType.MESSAGE),
            BYTES(JavaType.BYTE_STRING),
            UINT32(JavaType.INT),
            ENUM(JavaType.ENUM),
            SFIXED32(JavaType.INT),
            SFIXED64(JavaType.LONG),
            SINT32(JavaType.INT),
            SINT64(JavaType.LONG);

            private JavaType javaType;

            Type(final JavaType javaType) {
                this.javaType = javaType;
            }

            public static Type valueOf(final FieldDescriptorProto.Type type) {
                return values()[type.getNumber() - 1];
            }

            public FieldDescriptorProto.Type toProto() {
                return FieldDescriptorProto.Type.valueOf(ordinal() + 1);
            }

            public JavaType getJavaType() {
                return javaType;
            }
        }

        public enum JavaType {
            INT(0),
            LONG(0L),
            FLOAT(0F),
            DOUBLE(0D),
            BOOLEAN(false),
            STRING(""),
            BYTE_STRING(ByteString.EMPTY),
            ENUM(null),
            MESSAGE(null);

            private final Object defaultDefault;

            JavaType(final Object defaultDefault) {
                this.defaultDefault = defaultDefault;
            }
        }
    }

    public static final class EnumDescriptor
            implements GenericDescriptor, Internal.EnumLiteMap<EnumValueDescriptor> {

        private final int index;
        private final String fullName;
        private final FileDescriptor file;
        private final Descriptor containingType;
        private EnumDescriptorProto proto;
        private EnumValueDescriptor[] values;

        private EnumDescriptor(final EnumDescriptorProto proto,
                               final FileDescriptor file,
                               final Descriptor parent,
                               final int index)
                throws DescriptorValidationException {
            this.index = index;
            this.proto = proto;
            fullName = computeFullName(file, parent, proto.getName());
            this.file = file;
            containingType = parent;

            if (proto.getValueCount() == 0) {

                throw new DescriptorValidationException(this,
                        "Enums must contain at least one value.");
            }

            values = new EnumValueDescriptor[proto.getValueCount()];
            for (int i = 0; i < proto.getValueCount(); i++) {
                values[i] = new EnumValueDescriptor(
                        proto.getValue(i), file, this, i);
            }

            file.pool.addSymbol(this);
        }

        public int getIndex() {
            return index;
        }

        public EnumDescriptorProto toProto() {
            return proto;
        }

        public String getName() {
            return proto.getName();
        }

        public String getFullName() {
            return fullName;
        }

        public FileDescriptor getFile() {
            return file;
        }

        public Descriptor getContainingType() {
            return containingType;
        }

        public EnumOptions getOptions() {
            return proto.getOptions();
        }

        public List<EnumValueDescriptor> getValues() {
            return Collections.unmodifiableList(Arrays.asList(values));
        }

        public EnumValueDescriptor findValueByName(final String name) {
            final GenericDescriptor result =
                    file.pool.findSymbol(fullName + '.' + name);
            if (result != null && result instanceof EnumValueDescriptor) {
                return (EnumValueDescriptor) result;
            } else {
                return null;
            }
        }

        public EnumValueDescriptor findValueByNumber(final int number) {
            return file.pool.enumValuesByNumber.get(
                    new DescriptorPool.DescriptorIntPair(this, number));
        }

        private void setProto(final EnumDescriptorProto proto) {
            this.proto = proto;

            for (int i = 0; i < values.length; i++) {
                values[i].setProto(proto.getValue(i));
            }
        }
    }

    public static final class EnumValueDescriptor
            implements GenericDescriptor, Internal.EnumLite {

        private final int index;
        private final String fullName;
        private final FileDescriptor file;
        private final EnumDescriptor type;
        private EnumValueDescriptorProto proto;

        private EnumValueDescriptor(final EnumValueDescriptorProto proto,
                                    final FileDescriptor file,
                                    final EnumDescriptor parent,
                                    final int index)
                throws DescriptorValidationException {
            this.index = index;
            this.proto = proto;
            this.file = file;
            type = parent;

            fullName = parent.getFullName() + '.' + proto.getName();

            file.pool.addSymbol(this);
            file.pool.addEnumValueByNumber(this);
        }

        public int getIndex() {
            return index;
        }

        public EnumValueDescriptorProto toProto() {
            return proto;
        }

        public String getName() {
            return proto.getName();
        }

        public int getNumber() {
            return proto.getNumber();
        }

        public String getFullName() {
            return fullName;
        }

        public FileDescriptor getFile() {
            return file;
        }

        public EnumDescriptor getType() {
            return type;
        }

        public EnumValueOptions getOptions() {
            return proto.getOptions();
        }

        private void setProto(final EnumValueDescriptorProto proto) {
            this.proto = proto;
        }
    }

    public static final class ServiceDescriptor implements GenericDescriptor {

        private final int index;
        private final String fullName;
        private final FileDescriptor file;
        private ServiceDescriptorProto proto;
        private MethodDescriptor[] methods;

        private ServiceDescriptor(final ServiceDescriptorProto proto,
                                  final FileDescriptor file,
                                  final int index)
                throws DescriptorValidationException {
            this.index = index;
            this.proto = proto;
            fullName = computeFullName(file, null, proto.getName());
            this.file = file;

            methods = new MethodDescriptor[proto.getMethodCount()];
            for (int i = 0; i < proto.getMethodCount(); i++) {
                methods[i] = new MethodDescriptor(
                        proto.getMethod(i), file, this, i);
            }

            file.pool.addSymbol(this);
        }

        public int getIndex() {
            return index;
        }

        public ServiceDescriptorProto toProto() {
            return proto;
        }

        public String getName() {
            return proto.getName();
        }

        public String getFullName() {
            return fullName;
        }

        public FileDescriptor getFile() {
            return file;
        }

        public ServiceOptions getOptions() {
            return proto.getOptions();
        }

        public List<MethodDescriptor> getMethods() {
            return Collections.unmodifiableList(Arrays.asList(methods));
        }

        public MethodDescriptor findMethodByName(final String name) {
            final GenericDescriptor result =
                    file.pool.findSymbol(fullName + '.' + name);
            if (result != null && result instanceof MethodDescriptor) {
                return (MethodDescriptor) result;
            } else {
                return null;
            }
        }

        private void crossLink() throws DescriptorValidationException {
            for (final MethodDescriptor method : methods) {
                method.crossLink();
            }
        }

        private void setProto(final ServiceDescriptorProto proto) {
            this.proto = proto;

            for (int i = 0; i < methods.length; i++) {
                methods[i].setProto(proto.getMethod(i));
            }
        }
    }

    public static final class MethodDescriptor implements GenericDescriptor {

        private final int index;
        private final String fullName;
        private final FileDescriptor file;
        private final ServiceDescriptor service;
        private MethodDescriptorProto proto;
        private Descriptor inputType;
        private Descriptor outputType;

        private MethodDescriptor(final MethodDescriptorProto proto,
                                 final FileDescriptor file,
                                 final ServiceDescriptor parent,
                                 final int index)
                throws DescriptorValidationException {
            this.index = index;
            this.proto = proto;
            this.file = file;
            service = parent;

            fullName = parent.getFullName() + '.' + proto.getName();

            file.pool.addSymbol(this);
        }

        public int getIndex() {
            return index;
        }

        public MethodDescriptorProto toProto() {
            return proto;
        }

        public String getName() {
            return proto.getName();
        }

        public String getFullName() {
            return fullName;
        }

        public FileDescriptor getFile() {
            return file;
        }

        public ServiceDescriptor getService() {
            return service;
        }

        public Descriptor getInputType() {
            return inputType;
        }

        public Descriptor getOutputType() {
            return outputType;
        }

        public MethodOptions getOptions() {
            return proto.getOptions();
        }

        private void crossLink() throws DescriptorValidationException {
            final GenericDescriptor input =
                    file.pool.lookupSymbol(proto.getInputType(), this,
                            DescriptorPool.SearchFilter.TYPES_ONLY);
            if (!(input instanceof Descriptor)) {
                throw new DescriptorValidationException(this,
                        '\"' + proto.getInputType() + "\" is not a message type.");
            }
            inputType = (Descriptor) input;

            final GenericDescriptor output =
                    file.pool.lookupSymbol(proto.getOutputType(), this,
                            DescriptorPool.SearchFilter.TYPES_ONLY);
            if (!(output instanceof Descriptor)) {
                throw new DescriptorValidationException(this,
                        '\"' + proto.getOutputType() + "\" is not a message type.");
            }
            outputType = (Descriptor) output;
        }

        private void setProto(final MethodDescriptorProto proto) {
            this.proto = proto;
        }
    }

    public static class DescriptorValidationException extends Exception {
        private static final long serialVersionUID = 5750205775490483148L;
        private final String name;
        private final Message proto;
        private final String description;

        private DescriptorValidationException(
                final GenericDescriptor problemDescriptor,
                final String description) {
            super(problemDescriptor.getFullName() + ": " + description);

            name = problemDescriptor.getFullName();
            proto = problemDescriptor.toProto();
            this.description = description;
        }
        private DescriptorValidationException(
                final GenericDescriptor problemDescriptor,
                final String description,
                final Throwable cause) {
            this(problemDescriptor, description);
            initCause(cause);
        }
        private DescriptorValidationException(
                final FileDescriptor problemDescriptor,
                final String description) {
            super(problemDescriptor.getName() + ": " + description);

            name = problemDescriptor.getName();
            proto = problemDescriptor.toProto();
            this.description = description;
        }

        public String getProblemSymbolName() {
            return name;
        }

        public Message getProblemProto() {
            return proto;
        }

        public String getDescription() {
            return description;
        }
    }

    private static final class DescriptorPool {

        private final Set<FileDescriptor> dependencies;
        private final Map<String, GenericDescriptor> descriptorsByName =
                new HashMap<String, GenericDescriptor>();
        private final Map<DescriptorIntPair, FieldDescriptor> fieldsByNumber =
                new HashMap<DescriptorIntPair, FieldDescriptor>();
        private final Map<DescriptorIntPair, EnumValueDescriptor> enumValuesByNumber
                = new HashMap<DescriptorIntPair, EnumValueDescriptor>();

        DescriptorPool(final FileDescriptor[] dependencies) {
            this.dependencies = new HashSet<FileDescriptor>();

            for (int i = 0; i < dependencies.length; i++) {
                this.dependencies.add(dependencies[i]);
                importPublicDependencies(dependencies[i]);
            }

            for (final FileDescriptor dependency : this.dependencies) {
                try {
                    addPackage(dependency.getPackage(), dependency);
                } catch (DescriptorValidationException e) {

                    assert false;
                }
            }
        }

        static void validateSymbolName(final GenericDescriptor descriptor)
                throws DescriptorValidationException {
            final String name = descriptor.getName();
            if (name.length() == 0) {
                throw new DescriptorValidationException(descriptor, "Missing name.");
            } else {
                boolean valid = true;
                for (int i = 0; i < name.length(); i++) {
                    final char c = name.charAt(i);

                    if (c >= 128) {
                        valid = false;
                    }

                    if (Character.isLetter(c) || c == '_' ||
                            (Character.isDigit(c) && i > 0)) {

                    } else {
                        valid = false;
                    }
                }
                if (!valid) {
                    throw new DescriptorValidationException(descriptor,
                            '\"' + name + "\" is not a valid identifier.");
                }
            }
        }

        private void importPublicDependencies(final FileDescriptor file) {
            for (FileDescriptor dependency : file.getPublicDependencies()) {
                if (dependencies.add(dependency)) {
                    importPublicDependencies(dependency);
                }
            }
        }

        GenericDescriptor findSymbol(final String fullName) {
            return findSymbol(fullName, SearchFilter.ALL_SYMBOLS);
        }

        GenericDescriptor findSymbol(final String fullName,
                                     final SearchFilter filter) {
            GenericDescriptor result = descriptorsByName.get(fullName);
            if (result != null) {
                if ((filter == SearchFilter.ALL_SYMBOLS) ||
                        ((filter == SearchFilter.TYPES_ONLY) && isType(result)) ||
                        ((filter == SearchFilter.AGGREGATES_ONLY) && isAggregate(result))) {
                    return result;
                }
            }

            for (final FileDescriptor dependency : dependencies) {
                result = dependency.pool.descriptorsByName.get(fullName);
                if (result != null) {
                    if ((filter == SearchFilter.ALL_SYMBOLS) ||
                            ((filter == SearchFilter.TYPES_ONLY) && isType(result)) ||
                            ((filter == SearchFilter.AGGREGATES_ONLY) && isAggregate(result))) {
                        return result;
                    }
                }
            }

            return null;
        }

        boolean isType(GenericDescriptor descriptor) {
            return (descriptor instanceof Descriptor) ||
                    (descriptor instanceof EnumDescriptor);
        }

        boolean isAggregate(GenericDescriptor descriptor) {
            return (descriptor instanceof Descriptor) ||
                    (descriptor instanceof EnumDescriptor) ||
                    (descriptor instanceof PackageDescriptor) ||
                    (descriptor instanceof ServiceDescriptor);
        }

        GenericDescriptor lookupSymbol(final String name,
                                       final GenericDescriptor relativeTo,
                                       final DescriptorPool.SearchFilter filter)
                throws DescriptorValidationException {

            GenericDescriptor result;
            if (name.startsWith(".")) {

                result = findSymbol(name.substring(1), filter);
            } else {

                final int firstPartLength = name.indexOf('.');
                final String firstPart;
                if (firstPartLength == -1) {
                    firstPart = name;
                } else {
                    firstPart = name.substring(0, firstPartLength);
                }

                final StringBuilder scopeToTry =
                        new StringBuilder(relativeTo.getFullName());

                while (true) {

                    final int dotpos = scopeToTry.lastIndexOf(".");
                    if (dotpos == -1) {
                        result = findSymbol(name, filter);
                        break;
                    } else {
                        scopeToTry.setLength(dotpos + 1);

                        scopeToTry.append(firstPart);
                        result = findSymbol(scopeToTry.toString(),
                                DescriptorPool.SearchFilter.AGGREGATES_ONLY);

                        if (result != null) {
                            if (firstPartLength != -1) {

                                scopeToTry.setLength(dotpos + 1);
                                scopeToTry.append(name);
                                result = findSymbol(scopeToTry.toString(), filter);
                            }
                            break;
                        }

                        scopeToTry.setLength(dotpos);
                    }
                }
            }

            if (result == null) {
                throw new DescriptorValidationException(relativeTo,
                        '\"' + name + "\" is not defined.");
            } else {
                return result;
            }
        }

        void addSymbol(final GenericDescriptor descriptor)
                throws DescriptorValidationException {
            validateSymbolName(descriptor);

            final String fullName = descriptor.getFullName();
            final int dotpos = fullName.lastIndexOf('.');

            final GenericDescriptor old = descriptorsByName.put(fullName, descriptor);
            if (old != null) {
                descriptorsByName.put(fullName, old);

                if (descriptor.getFile() == old.getFile()) {
                    if (dotpos == -1) {
                        throw new DescriptorValidationException(descriptor,
                                '\"' + fullName + "\" is already defined.");
                    } else {
                        throw new DescriptorValidationException(descriptor,
                                '\"' + fullName.substring(dotpos + 1) +
                                        "\" is already defined in \"" +
                                        fullName.substring(0, dotpos) + "\".");
                    }
                } else {
                    throw new DescriptorValidationException(descriptor,
                            '\"' + fullName + "\" is already defined in file \"" +
                                    old.getFile().getName() + "\".");
                }
            }
        }

        void addPackage(final String fullName, final FileDescriptor file)
                throws DescriptorValidationException {
            final int dotpos = fullName.lastIndexOf('.');
            final String name;
            if (dotpos == -1) {
                name = fullName;
            } else {
                addPackage(fullName.substring(0, dotpos), file);
                name = fullName.substring(dotpos + 1);
            }

            final GenericDescriptor old =
                    descriptorsByName.put(fullName,
                            new PackageDescriptor(name, fullName, file));
            if (old != null) {
                descriptorsByName.put(fullName, old);
                if (!(old instanceof PackageDescriptor)) {
                    throw new DescriptorValidationException(file,
                            '\"' + name + "\" is already defined (as something other than a "
                                    + "package) in file \"" + old.getFile().getName() + "\".");
                }
            }
        }

        void addFieldByNumber(final FieldDescriptor field)
                throws DescriptorValidationException {
            final DescriptorIntPair key =
                    new DescriptorIntPair(field.getContainingType(), field.getNumber());
            final FieldDescriptor old = fieldsByNumber.put(key, field);
            if (old != null) {
                fieldsByNumber.put(key, old);
                throw new DescriptorValidationException(field,
                        "Field number " + field.getNumber() +
                                "has already been used in \"" +
                                field.getContainingType().getFullName() +
                                "\" by field \"" + old.getName() + "\".");
            }
        }

        void addEnumValueByNumber(final EnumValueDescriptor value) {
            final DescriptorIntPair key =
                    new DescriptorIntPair(value.getType(), value.getNumber());
            final EnumValueDescriptor old = enumValuesByNumber.put(key, value);
            if (old != null) {
                enumValuesByNumber.put(key, old);

            }
        }

        enum SearchFilter {
            TYPES_ONLY, AGGREGATES_ONLY, ALL_SYMBOLS
        }

        private static final class PackageDescriptor implements GenericDescriptor {
            private final String name;
            private final String fullName;
            private final FileDescriptor file;

            PackageDescriptor(final String name, final String fullName,
                              final FileDescriptor file) {
                this.file = file;
                this.fullName = fullName;
                this.name = name;
            }

            public Message toProto() {
                return file.toProto();
            }

            public String getName() {
                return name;
            }

            public String getFullName() {
                return fullName;
            }

            public FileDescriptor getFile() {
                return file;
            }
        }

        private static final class DescriptorIntPair {
            private final GenericDescriptor descriptor;
            private final int number;

            DescriptorIntPair(final GenericDescriptor descriptor, final int number) {
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
}
