

package com.google.protobuf;

public final class DescriptorProtos {
  private DescriptorProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface FileDescriptorSetOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    java.util.List<com.google.protobuf.DescriptorProtos.FileDescriptorProto> 
        getFileList();

    com.google.protobuf.DescriptorProtos.FileDescriptorProto getFile(int index);

    int getFileCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder> 
        getFileOrBuilderList();

    com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder getFileOrBuilder(
        int index);
  }

  public static final class FileDescriptorSet extends
      com.google.protobuf.GeneratedMessage
      implements FileDescriptorSetOrBuilder {

    private FileDescriptorSet(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private FileDescriptorSet(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final FileDescriptorSet defaultInstance;
    public static FileDescriptorSet getDefaultInstance() {
      return defaultInstance;
    }

    public FileDescriptorSet getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private FileDescriptorSet(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                file_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.FileDescriptorProto>();
                mutable_bitField0_ |= 0x00000001;
              }
              file_.add(input.readMessage(com.google.protobuf.DescriptorProtos.FileDescriptorProto.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          file_ = java.util.Collections.unmodifiableList(file_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.FileDescriptorSet.class, com.google.protobuf.DescriptorProtos.FileDescriptorSet.Builder.class);
    }

    public static com.google.protobuf.Parser<FileDescriptorSet> PARSER =
        new com.google.protobuf.AbstractParser<FileDescriptorSet>() {
      public FileDescriptorSet parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new FileDescriptorSet(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<FileDescriptorSet> getParserForType() {
      return PARSER;
    }

    public static final int FILE_FIELD_NUMBER = 1;
    private java.util.List<com.google.protobuf.DescriptorProtos.FileDescriptorProto> file_;

    public java.util.List<com.google.protobuf.DescriptorProtos.FileDescriptorProto> getFileList() {
      return file_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder> 
        getFileOrBuilderList() {
      return file_;
    }

    public int getFileCount() {
      return file_.size();
    }

    public com.google.protobuf.DescriptorProtos.FileDescriptorProto getFile(int index) {
      return file_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder getFileOrBuilder(
        int index) {
      return file_.get(index);
    }

    private void initFields() {
      file_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getFileCount(); i++) {
        if (!getFile(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (int i = 0; i < file_.size(); i++) {
        output.writeMessage(1, file_.get(i));
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < file_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, file_.get(i));
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorSet parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.FileDescriptorSet prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.FileDescriptorSetOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.FileDescriptorSet.class, com.google.protobuf.DescriptorProtos.FileDescriptorSet.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getFileFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (fileBuilder_ == null) {
          file_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          fileBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorSet_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorSet getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.FileDescriptorSet.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorSet build() {
        com.google.protobuf.DescriptorProtos.FileDescriptorSet result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorSet buildPartial() {
        com.google.protobuf.DescriptorProtos.FileDescriptorSet result = new com.google.protobuf.DescriptorProtos.FileDescriptorSet(this);
        int from_bitField0_ = bitField0_;
        if (fileBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            file_ = java.util.Collections.unmodifiableList(file_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.file_ = file_;
        } else {
          result.file_ = fileBuilder_.build();
        }
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.FileDescriptorSet) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.FileDescriptorSet)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.FileDescriptorSet other) {
        if (other == com.google.protobuf.DescriptorProtos.FileDescriptorSet.getDefaultInstance()) return this;
        if (fileBuilder_ == null) {
          if (!other.file_.isEmpty()) {
            if (file_.isEmpty()) {
              file_ = other.file_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureFileIsMutable();
              file_.addAll(other.file_);
            }
            onChanged();
          }
        } else {
          if (!other.file_.isEmpty()) {
            if (fileBuilder_.isEmpty()) {
              fileBuilder_.dispose();
              fileBuilder_ = null;
              file_ = other.file_;
              bitField0_ = (bitField0_ & ~0x00000001);
              fileBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getFileFieldBuilder() : null;
            } else {
              fileBuilder_.addAllMessages(other.file_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getFileCount(); i++) {
          if (!getFile(i).isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.FileDescriptorSet parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.FileDescriptorSet) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<com.google.protobuf.DescriptorProtos.FileDescriptorProto> file_ =
        java.util.Collections.emptyList();
      private void ensureFileIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          file_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.FileDescriptorProto>(file_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.FileDescriptorProto, com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder> fileBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.FileDescriptorProto> getFileList() {
        if (fileBuilder_ == null) {
          return java.util.Collections.unmodifiableList(file_);
        } else {
          return fileBuilder_.getMessageList();
        }
      }

      public int getFileCount() {
        if (fileBuilder_ == null) {
          return file_.size();
        } else {
          return fileBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorProto getFile(int index) {
        if (fileBuilder_ == null) {
          return file_.get(index);
        } else {
          return fileBuilder_.getMessage(index);
        }
      }

      public Builder setFile(
          int index, com.google.protobuf.DescriptorProtos.FileDescriptorProto value) {
        if (fileBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureFileIsMutable();
          file_.set(index, value);
          onChanged();
        } else {
          fileBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setFile(
          int index, com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder builderForValue) {
        if (fileBuilder_ == null) {
          ensureFileIsMutable();
          file_.set(index, builderForValue.build());
          onChanged();
        } else {
          fileBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addFile(com.google.protobuf.DescriptorProtos.FileDescriptorProto value) {
        if (fileBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureFileIsMutable();
          file_.add(value);
          onChanged();
        } else {
          fileBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addFile(
          int index, com.google.protobuf.DescriptorProtos.FileDescriptorProto value) {
        if (fileBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureFileIsMutable();
          file_.add(index, value);
          onChanged();
        } else {
          fileBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addFile(
          com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder builderForValue) {
        if (fileBuilder_ == null) {
          ensureFileIsMutable();
          file_.add(builderForValue.build());
          onChanged();
        } else {
          fileBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addFile(
          int index, com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder builderForValue) {
        if (fileBuilder_ == null) {
          ensureFileIsMutable();
          file_.add(index, builderForValue.build());
          onChanged();
        } else {
          fileBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllFile(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.FileDescriptorProto> values) {
        if (fileBuilder_ == null) {
          ensureFileIsMutable();
          super.addAll(values, file_);
          onChanged();
        } else {
          fileBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearFile() {
        if (fileBuilder_ == null) {
          file_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          fileBuilder_.clear();
        }
        return this;
      }

      public Builder removeFile(int index) {
        if (fileBuilder_ == null) {
          ensureFileIsMutable();
          file_.remove(index);
          onChanged();
        } else {
          fileBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder getFileBuilder(
          int index) {
        return getFileFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder getFileOrBuilder(
          int index) {
        if (fileBuilder_ == null) {
          return file_.get(index);  } else {
          return fileBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder> 
           getFileOrBuilderList() {
        if (fileBuilder_ != null) {
          return fileBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(file_);
        }
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder addFileBuilder() {
        return getFileFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.FileDescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder addFileBuilder(
          int index) {
        return getFileFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.FileDescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder> 
           getFileBuilderList() {
        return getFileFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.FileDescriptorProto, com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder> 
          getFileFieldBuilder() {
        if (fileBuilder_ == null) {
          fileBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.FileDescriptorProto, com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder>(
                  file_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          file_ = null;
        }
        return fileBuilder_;
      }

    }

    static {
      defaultInstance = new FileDescriptorSet(true);
      defaultInstance.initFields();
    }

  }

  public interface FileDescriptorProtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    boolean hasName();

    java.lang.String getName();

    com.google.protobuf.ByteString
        getNameBytes();

    boolean hasPackage();

    java.lang.String getPackage();

    com.google.protobuf.ByteString
        getPackageBytes();

    java.util.List<java.lang.String>
    getDependencyList();

    int getDependencyCount();

    java.lang.String getDependency(int index);

    com.google.protobuf.ByteString
        getDependencyBytes(int index);

    java.util.List<java.lang.Integer> getPublicDependencyList();

    int getPublicDependencyCount();

    int getPublicDependency(int index);

    java.util.List<java.lang.Integer> getWeakDependencyList();

    int getWeakDependencyCount();

    int getWeakDependency(int index);

    java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> 
        getMessageTypeList();

    com.google.protobuf.DescriptorProtos.DescriptorProto getMessageType(int index);

    int getMessageTypeCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> 
        getMessageTypeOrBuilderList();

    com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder getMessageTypeOrBuilder(
        int index);

    java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> 
        getEnumTypeList();

    com.google.protobuf.DescriptorProtos.EnumDescriptorProto getEnumType(int index);

    int getEnumTypeCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> 
        getEnumTypeOrBuilderList();

    com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(
        int index);

    java.util.List<com.google.protobuf.DescriptorProtos.ServiceDescriptorProto> 
        getServiceList();

    com.google.protobuf.DescriptorProtos.ServiceDescriptorProto getService(int index);

    int getServiceCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder> 
        getServiceOrBuilderList();

    com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder getServiceOrBuilder(
        int index);

    java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> 
        getExtensionList();

    com.google.protobuf.DescriptorProtos.FieldDescriptorProto getExtension(int index);

    int getExtensionCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
        getExtensionOrBuilderList();

    com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(
        int index);

    boolean hasOptions();

    com.google.protobuf.DescriptorProtos.FileOptions getOptions();

    com.google.protobuf.DescriptorProtos.FileOptionsOrBuilder getOptionsOrBuilder();

    boolean hasSourceCodeInfo();

    com.google.protobuf.DescriptorProtos.SourceCodeInfo getSourceCodeInfo();

    com.google.protobuf.DescriptorProtos.SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder();
  }

  public static final class FileDescriptorProto extends
      com.google.protobuf.GeneratedMessage
      implements FileDescriptorProtoOrBuilder {

    private FileDescriptorProto(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private FileDescriptorProto(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final FileDescriptorProto defaultInstance;
    public static FileDescriptorProto getDefaultInstance() {
      return defaultInstance;
    }

    public FileDescriptorProto getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private FileDescriptorProto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              name_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              package_ = input.readBytes();
              break;
            }
            case 26: {
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
                dependency_ = new com.google.protobuf.LazyStringArrayList();
                mutable_bitField0_ |= 0x00000004;
              }
              dependency_.add(input.readBytes());
              break;
            }
            case 34: {
              if (!((mutable_bitField0_ & 0x00000020) == 0x00000020)) {
                messageType_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.DescriptorProto>();
                mutable_bitField0_ |= 0x00000020;
              }
              messageType_.add(input.readMessage(com.google.protobuf.DescriptorProtos.DescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 42: {
              if (!((mutable_bitField0_ & 0x00000040) == 0x00000040)) {
                enumType_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.EnumDescriptorProto>();
                mutable_bitField0_ |= 0x00000040;
              }
              enumType_.add(input.readMessage(com.google.protobuf.DescriptorProtos.EnumDescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 50: {
              if (!((mutable_bitField0_ & 0x00000080) == 0x00000080)) {
                service_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.ServiceDescriptorProto>();
                mutable_bitField0_ |= 0x00000080;
              }
              service_.add(input.readMessage(com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 58: {
              if (!((mutable_bitField0_ & 0x00000100) == 0x00000100)) {
                extension_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.FieldDescriptorProto>();
                mutable_bitField0_ |= 0x00000100;
              }
              extension_.add(input.readMessage(com.google.protobuf.DescriptorProtos.FieldDescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 66: {
              com.google.protobuf.DescriptorProtos.FileOptions.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = options_.toBuilder();
              }
              options_ = input.readMessage(com.google.protobuf.DescriptorProtos.FileOptions.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(options_);
                options_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
            case 74: {
              com.google.protobuf.DescriptorProtos.SourceCodeInfo.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = sourceCodeInfo_.toBuilder();
              }
              sourceCodeInfo_ = input.readMessage(com.google.protobuf.DescriptorProtos.SourceCodeInfo.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(sourceCodeInfo_);
                sourceCodeInfo_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
            case 80: {
              if (!((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
                publicDependency_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000008;
              }
              publicDependency_.add(input.readInt32());
              break;
            }
            case 82: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000008) == 0x00000008) && input.getBytesUntilLimit() > 0) {
                publicDependency_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000008;
              }
              while (input.getBytesUntilLimit() > 0) {
                publicDependency_.add(input.readInt32());
              }
              input.popLimit(limit);
              break;
            }
            case 88: {
              if (!((mutable_bitField0_ & 0x00000010) == 0x00000010)) {
                weakDependency_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000010;
              }
              weakDependency_.add(input.readInt32());
              break;
            }
            case 90: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              if (!((mutable_bitField0_ & 0x00000010) == 0x00000010) && input.getBytesUntilLimit() > 0) {
                weakDependency_ = new java.util.ArrayList<java.lang.Integer>();
                mutable_bitField0_ |= 0x00000010;
              }
              while (input.getBytesUntilLimit() > 0) {
                weakDependency_.add(input.readInt32());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
          dependency_ = new com.google.protobuf.UnmodifiableLazyStringList(dependency_);
        }
        if (((mutable_bitField0_ & 0x00000020) == 0x00000020)) {
          messageType_ = java.util.Collections.unmodifiableList(messageType_);
        }
        if (((mutable_bitField0_ & 0x00000040) == 0x00000040)) {
          enumType_ = java.util.Collections.unmodifiableList(enumType_);
        }
        if (((mutable_bitField0_ & 0x00000080) == 0x00000080)) {
          service_ = java.util.Collections.unmodifiableList(service_);
        }
        if (((mutable_bitField0_ & 0x00000100) == 0x00000100)) {
          extension_ = java.util.Collections.unmodifiableList(extension_);
        }
        if (((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
          publicDependency_ = java.util.Collections.unmodifiableList(publicDependency_);
        }
        if (((mutable_bitField0_ & 0x00000010) == 0x00000010)) {
          weakDependency_ = java.util.Collections.unmodifiableList(weakDependency_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.FileDescriptorProto.class, com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder.class);
    }

    public static com.google.protobuf.Parser<FileDescriptorProto> PARSER =
        new com.google.protobuf.AbstractParser<FileDescriptorProto>() {
      public FileDescriptorProto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new FileDescriptorProto(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<FileDescriptorProto> getParserForType() {
      return PARSER;
    }

    private int bitField0_;

    public static final int NAME_FIELD_NUMBER = 1;
    private java.lang.Object name_;

    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int PACKAGE_FIELD_NUMBER = 2;
    private java.lang.Object package_;

    public boolean hasPackage() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public java.lang.String getPackage() {
      java.lang.Object ref = package_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          package_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getPackageBytes() {
      java.lang.Object ref = package_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        package_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int DEPENDENCY_FIELD_NUMBER = 3;
    private com.google.protobuf.LazyStringList dependency_;

    public java.util.List<java.lang.String>
        getDependencyList() {
      return dependency_;
    }

    public int getDependencyCount() {
      return dependency_.size();
    }

    public java.lang.String getDependency(int index) {
      return dependency_.get(index);
    }

    public com.google.protobuf.ByteString
        getDependencyBytes(int index) {
      return dependency_.getByteString(index);
    }

    public static final int PUBLIC_DEPENDENCY_FIELD_NUMBER = 10;
    private java.util.List<java.lang.Integer> publicDependency_;

    public java.util.List<java.lang.Integer>
        getPublicDependencyList() {
      return publicDependency_;
    }

    public int getPublicDependencyCount() {
      return publicDependency_.size();
    }

    public int getPublicDependency(int index) {
      return publicDependency_.get(index);
    }

    public static final int WEAK_DEPENDENCY_FIELD_NUMBER = 11;
    private java.util.List<java.lang.Integer> weakDependency_;

    public java.util.List<java.lang.Integer>
        getWeakDependencyList() {
      return weakDependency_;
    }

    public int getWeakDependencyCount() {
      return weakDependency_.size();
    }

    public int getWeakDependency(int index) {
      return weakDependency_.get(index);
    }

    public static final int MESSAGE_TYPE_FIELD_NUMBER = 4;
    private java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> messageType_;

    public java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> getMessageTypeList() {
      return messageType_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> 
        getMessageTypeOrBuilderList() {
      return messageType_;
    }

    public int getMessageTypeCount() {
      return messageType_.size();
    }

    public com.google.protobuf.DescriptorProtos.DescriptorProto getMessageType(int index) {
      return messageType_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder getMessageTypeOrBuilder(
        int index) {
      return messageType_.get(index);
    }

    public static final int ENUM_TYPE_FIELD_NUMBER = 5;
    private java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> enumType_;

    public java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> getEnumTypeList() {
      return enumType_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> 
        getEnumTypeOrBuilderList() {
      return enumType_;
    }

    public int getEnumTypeCount() {
      return enumType_.size();
    }

    public com.google.protobuf.DescriptorProtos.EnumDescriptorProto getEnumType(int index) {
      return enumType_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(
        int index) {
      return enumType_.get(index);
    }

    public static final int SERVICE_FIELD_NUMBER = 6;
    private java.util.List<com.google.protobuf.DescriptorProtos.ServiceDescriptorProto> service_;

    public java.util.List<com.google.protobuf.DescriptorProtos.ServiceDescriptorProto> getServiceList() {
      return service_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder> 
        getServiceOrBuilderList() {
      return service_;
    }

    public int getServiceCount() {
      return service_.size();
    }

    public com.google.protobuf.DescriptorProtos.ServiceDescriptorProto getService(int index) {
      return service_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder getServiceOrBuilder(
        int index) {
      return service_.get(index);
    }

    public static final int EXTENSION_FIELD_NUMBER = 7;
    private java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> extension_;

    public java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> getExtensionList() {
      return extension_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
        getExtensionOrBuilderList() {
      return extension_;
    }

    public int getExtensionCount() {
      return extension_.size();
    }

    public com.google.protobuf.DescriptorProtos.FieldDescriptorProto getExtension(int index) {
      return extension_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(
        int index) {
      return extension_.get(index);
    }

    public static final int OPTIONS_FIELD_NUMBER = 8;
    private com.google.protobuf.DescriptorProtos.FileOptions options_;

    public boolean hasOptions() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }

    public com.google.protobuf.DescriptorProtos.FileOptions getOptions() {
      return options_;
    }

    public com.google.protobuf.DescriptorProtos.FileOptionsOrBuilder getOptionsOrBuilder() {
      return options_;
    }

    public static final int SOURCE_CODE_INFO_FIELD_NUMBER = 9;
    private com.google.protobuf.DescriptorProtos.SourceCodeInfo sourceCodeInfo_;

    public boolean hasSourceCodeInfo() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }

    public com.google.protobuf.DescriptorProtos.SourceCodeInfo getSourceCodeInfo() {
      return sourceCodeInfo_;
    }

    public com.google.protobuf.DescriptorProtos.SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder() {
      return sourceCodeInfo_;
    }

    private void initFields() {
      name_ = "";
      package_ = "";
      dependency_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      publicDependency_ = java.util.Collections.emptyList();
      weakDependency_ = java.util.Collections.emptyList();
      messageType_ = java.util.Collections.emptyList();
      enumType_ = java.util.Collections.emptyList();
      service_ = java.util.Collections.emptyList();
      extension_ = java.util.Collections.emptyList();
      options_ = com.google.protobuf.DescriptorProtos.FileOptions.getDefaultInstance();
      sourceCodeInfo_ = com.google.protobuf.DescriptorProtos.SourceCodeInfo.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getMessageTypeCount(); i++) {
        if (!getMessageType(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getEnumTypeCount(); i++) {
        if (!getEnumType(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getServiceCount(); i++) {
        if (!getService(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getExtensionCount(); i++) {
        if (!getExtension(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (hasOptions()) {
        if (!getOptions().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getPackageBytes());
      }
      for (int i = 0; i < dependency_.size(); i++) {
        output.writeBytes(3, dependency_.getByteString(i));
      }
      for (int i = 0; i < messageType_.size(); i++) {
        output.writeMessage(4, messageType_.get(i));
      }
      for (int i = 0; i < enumType_.size(); i++) {
        output.writeMessage(5, enumType_.get(i));
      }
      for (int i = 0; i < service_.size(); i++) {
        output.writeMessage(6, service_.get(i));
      }
      for (int i = 0; i < extension_.size(); i++) {
        output.writeMessage(7, extension_.get(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(8, options_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(9, sourceCodeInfo_);
      }
      for (int i = 0; i < publicDependency_.size(); i++) {
        output.writeInt32(10, publicDependency_.get(i));
      }
      for (int i = 0; i < weakDependency_.size(); i++) {
        output.writeInt32(11, weakDependency_.get(i));
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getPackageBytes());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < dependency_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeBytesSizeNoTag(dependency_.getByteString(i));
        }
        size += dataSize;
        size += 1 * getDependencyList().size();
      }
      for (int i = 0; i < messageType_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, messageType_.get(i));
      }
      for (int i = 0; i < enumType_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, enumType_.get(i));
      }
      for (int i = 0; i < service_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, service_.get(i));
      }
      for (int i = 0; i < extension_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(7, extension_.get(i));
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(8, options_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(9, sourceCodeInfo_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < publicDependency_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(publicDependency_.get(i));
        }
        size += dataSize;
        size += 1 * getPublicDependencyList().size();
      }
      {
        int dataSize = 0;
        for (int i = 0; i < weakDependency_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(weakDependency_.get(i));
        }
        size += dataSize;
        size += 1 * getWeakDependencyList().size();
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FileDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.FileDescriptorProto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.FileDescriptorProtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.FileDescriptorProto.class, com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getMessageTypeFieldBuilder();
          getEnumTypeFieldBuilder();
          getServiceFieldBuilder();
          getExtensionFieldBuilder();
          getOptionsFieldBuilder();
          getSourceCodeInfoFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        package_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        dependency_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        publicDependency_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
        weakDependency_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000010);
        if (messageTypeBuilder_ == null) {
          messageType_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
        } else {
          messageTypeBuilder_.clear();
        }
        if (enumTypeBuilder_ == null) {
          enumType_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
        } else {
          enumTypeBuilder_.clear();
        }
        if (serviceBuilder_ == null) {
          service_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000080);
        } else {
          serviceBuilder_.clear();
        }
        if (extensionBuilder_ == null) {
          extension_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000100);
        } else {
          extensionBuilder_.clear();
        }
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.FileOptions.getDefaultInstance();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000200);
        if (sourceCodeInfoBuilder_ == null) {
          sourceCodeInfo_ = com.google.protobuf.DescriptorProtos.SourceCodeInfo.getDefaultInstance();
        } else {
          sourceCodeInfoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000400);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileDescriptorProto_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorProto getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.FileDescriptorProto.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorProto build() {
        com.google.protobuf.DescriptorProtos.FileDescriptorProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.FileDescriptorProto buildPartial() {
        com.google.protobuf.DescriptorProtos.FileDescriptorProto result = new com.google.protobuf.DescriptorProtos.FileDescriptorProto(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.package_ = package_;
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          dependency_ = new com.google.protobuf.UnmodifiableLazyStringList(
              dependency_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.dependency_ = dependency_;
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          publicDependency_ = java.util.Collections.unmodifiableList(publicDependency_);
          bitField0_ = (bitField0_ & ~0x00000008);
        }
        result.publicDependency_ = publicDependency_;
        if (((bitField0_ & 0x00000010) == 0x00000010)) {
          weakDependency_ = java.util.Collections.unmodifiableList(weakDependency_);
          bitField0_ = (bitField0_ & ~0x00000010);
        }
        result.weakDependency_ = weakDependency_;
        if (messageTypeBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020)) {
            messageType_ = java.util.Collections.unmodifiableList(messageType_);
            bitField0_ = (bitField0_ & ~0x00000020);
          }
          result.messageType_ = messageType_;
        } else {
          result.messageType_ = messageTypeBuilder_.build();
        }
        if (enumTypeBuilder_ == null) {
          if (((bitField0_ & 0x00000040) == 0x00000040)) {
            enumType_ = java.util.Collections.unmodifiableList(enumType_);
            bitField0_ = (bitField0_ & ~0x00000040);
          }
          result.enumType_ = enumType_;
        } else {
          result.enumType_ = enumTypeBuilder_.build();
        }
        if (serviceBuilder_ == null) {
          if (((bitField0_ & 0x00000080) == 0x00000080)) {
            service_ = java.util.Collections.unmodifiableList(service_);
            bitField0_ = (bitField0_ & ~0x00000080);
          }
          result.service_ = service_;
        } else {
          result.service_ = serviceBuilder_.build();
        }
        if (extensionBuilder_ == null) {
          if (((bitField0_ & 0x00000100) == 0x00000100)) {
            extension_ = java.util.Collections.unmodifiableList(extension_);
            bitField0_ = (bitField0_ & ~0x00000100);
          }
          result.extension_ = extension_;
        } else {
          result.extension_ = extensionBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000200) == 0x00000200)) {
          to_bitField0_ |= 0x00000004;
        }
        if (optionsBuilder_ == null) {
          result.options_ = options_;
        } else {
          result.options_ = optionsBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000400) == 0x00000400)) {
          to_bitField0_ |= 0x00000008;
        }
        if (sourceCodeInfoBuilder_ == null) {
          result.sourceCodeInfo_ = sourceCodeInfo_;
        } else {
          result.sourceCodeInfo_ = sourceCodeInfoBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.FileDescriptorProto) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.FileDescriptorProto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.FileDescriptorProto other) {
        if (other == com.google.protobuf.DescriptorProtos.FileDescriptorProto.getDefaultInstance()) return this;
        if (other.hasName()) {
          bitField0_ |= 0x00000001;
          name_ = other.name_;
          onChanged();
        }
        if (other.hasPackage()) {
          bitField0_ |= 0x00000002;
          package_ = other.package_;
          onChanged();
        }
        if (!other.dependency_.isEmpty()) {
          if (dependency_.isEmpty()) {
            dependency_ = other.dependency_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureDependencyIsMutable();
            dependency_.addAll(other.dependency_);
          }
          onChanged();
        }
        if (!other.publicDependency_.isEmpty()) {
          if (publicDependency_.isEmpty()) {
            publicDependency_ = other.publicDependency_;
            bitField0_ = (bitField0_ & ~0x00000008);
          } else {
            ensurePublicDependencyIsMutable();
            publicDependency_.addAll(other.publicDependency_);
          }
          onChanged();
        }
        if (!other.weakDependency_.isEmpty()) {
          if (weakDependency_.isEmpty()) {
            weakDependency_ = other.weakDependency_;
            bitField0_ = (bitField0_ & ~0x00000010);
          } else {
            ensureWeakDependencyIsMutable();
            weakDependency_.addAll(other.weakDependency_);
          }
          onChanged();
        }
        if (messageTypeBuilder_ == null) {
          if (!other.messageType_.isEmpty()) {
            if (messageType_.isEmpty()) {
              messageType_ = other.messageType_;
              bitField0_ = (bitField0_ & ~0x00000020);
            } else {
              ensureMessageTypeIsMutable();
              messageType_.addAll(other.messageType_);
            }
            onChanged();
          }
        } else {
          if (!other.messageType_.isEmpty()) {
            if (messageTypeBuilder_.isEmpty()) {
              messageTypeBuilder_.dispose();
              messageTypeBuilder_ = null;
              messageType_ = other.messageType_;
              bitField0_ = (bitField0_ & ~0x00000020);
              messageTypeBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getMessageTypeFieldBuilder() : null;
            } else {
              messageTypeBuilder_.addAllMessages(other.messageType_);
            }
          }
        }
        if (enumTypeBuilder_ == null) {
          if (!other.enumType_.isEmpty()) {
            if (enumType_.isEmpty()) {
              enumType_ = other.enumType_;
              bitField0_ = (bitField0_ & ~0x00000040);
            } else {
              ensureEnumTypeIsMutable();
              enumType_.addAll(other.enumType_);
            }
            onChanged();
          }
        } else {
          if (!other.enumType_.isEmpty()) {
            if (enumTypeBuilder_.isEmpty()) {
              enumTypeBuilder_.dispose();
              enumTypeBuilder_ = null;
              enumType_ = other.enumType_;
              bitField0_ = (bitField0_ & ~0x00000040);
              enumTypeBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getEnumTypeFieldBuilder() : null;
            } else {
              enumTypeBuilder_.addAllMessages(other.enumType_);
            }
          }
        }
        if (serviceBuilder_ == null) {
          if (!other.service_.isEmpty()) {
            if (service_.isEmpty()) {
              service_ = other.service_;
              bitField0_ = (bitField0_ & ~0x00000080);
            } else {
              ensureServiceIsMutable();
              service_.addAll(other.service_);
            }
            onChanged();
          }
        } else {
          if (!other.service_.isEmpty()) {
            if (serviceBuilder_.isEmpty()) {
              serviceBuilder_.dispose();
              serviceBuilder_ = null;
              service_ = other.service_;
              bitField0_ = (bitField0_ & ~0x00000080);
              serviceBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getServiceFieldBuilder() : null;
            } else {
              serviceBuilder_.addAllMessages(other.service_);
            }
          }
        }
        if (extensionBuilder_ == null) {
          if (!other.extension_.isEmpty()) {
            if (extension_.isEmpty()) {
              extension_ = other.extension_;
              bitField0_ = (bitField0_ & ~0x00000100);
            } else {
              ensureExtensionIsMutable();
              extension_.addAll(other.extension_);
            }
            onChanged();
          }
        } else {
          if (!other.extension_.isEmpty()) {
            if (extensionBuilder_.isEmpty()) {
              extensionBuilder_.dispose();
              extensionBuilder_ = null;
              extension_ = other.extension_;
              bitField0_ = (bitField0_ & ~0x00000100);
              extensionBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getExtensionFieldBuilder() : null;
            } else {
              extensionBuilder_.addAllMessages(other.extension_);
            }
          }
        }
        if (other.hasOptions()) {
          mergeOptions(other.getOptions());
        }
        if (other.hasSourceCodeInfo()) {
          mergeSourceCodeInfo(other.getSourceCodeInfo());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getMessageTypeCount(); i++) {
          if (!getMessageType(i).isInitialized()) {

            return false;
          }
        }
        for (int i = 0; i < getEnumTypeCount(); i++) {
          if (!getEnumType(i).isInitialized()) {

            return false;
          }
        }
        for (int i = 0; i < getServiceCount(); i++) {
          if (!getService(i).isInitialized()) {

            return false;
          }
        }
        for (int i = 0; i < getExtensionCount(); i++) {
          if (!getExtension(i).isInitialized()) {

            return false;
          }
        }
        if (hasOptions()) {
          if (!getOptions().isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.FileDescriptorProto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.FileDescriptorProto) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object name_ = "";

      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }

      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object package_ = "";

      public boolean hasPackage() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public java.lang.String getPackage() {
        java.lang.Object ref = package_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          package_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getPackageBytes() {
        java.lang.Object ref = package_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          package_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setPackage(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        package_ = value;
        onChanged();
        return this;
      }

      public Builder clearPackage() {
        bitField0_ = (bitField0_ & ~0x00000002);
        package_ = getDefaultInstance().getPackage();
        onChanged();
        return this;
      }

      public Builder setPackageBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        package_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.LazyStringList dependency_ = com.google.protobuf.LazyStringArrayList.EMPTY;
      private void ensureDependencyIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          dependency_ = new com.google.protobuf.LazyStringArrayList(dependency_);
          bitField0_ |= 0x00000004;
         }
      }

      public java.util.List<java.lang.String>
          getDependencyList() {
        return java.util.Collections.unmodifiableList(dependency_);
      }

      public int getDependencyCount() {
        return dependency_.size();
      }

      public java.lang.String getDependency(int index) {
        return dependency_.get(index);
      }

      public com.google.protobuf.ByteString
          getDependencyBytes(int index) {
        return dependency_.getByteString(index);
      }

      public Builder setDependency(
          int index, java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureDependencyIsMutable();
        dependency_.set(index, value);
        onChanged();
        return this;
      }

      public Builder addDependency(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureDependencyIsMutable();
        dependency_.add(value);
        onChanged();
        return this;
      }

      public Builder addAllDependency(
          java.lang.Iterable<java.lang.String> values) {
        ensureDependencyIsMutable();
        super.addAll(values, dependency_);
        onChanged();
        return this;
      }

      public Builder clearDependency() {
        dependency_ = com.google.protobuf.LazyStringArrayList.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
        return this;
      }

      public Builder addDependencyBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  ensureDependencyIsMutable();
        dependency_.add(value);
        onChanged();
        return this;
      }

      private java.util.List<java.lang.Integer> publicDependency_ = java.util.Collections.emptyList();
      private void ensurePublicDependencyIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          publicDependency_ = new java.util.ArrayList<java.lang.Integer>(publicDependency_);
          bitField0_ |= 0x00000008;
         }
      }

      public java.util.List<java.lang.Integer>
          getPublicDependencyList() {
        return java.util.Collections.unmodifiableList(publicDependency_);
      }

      public int getPublicDependencyCount() {
        return publicDependency_.size();
      }

      public int getPublicDependency(int index) {
        return publicDependency_.get(index);
      }

      public Builder setPublicDependency(
          int index, int value) {
        ensurePublicDependencyIsMutable();
        publicDependency_.set(index, value);
        onChanged();
        return this;
      }

      public Builder addPublicDependency(int value) {
        ensurePublicDependencyIsMutable();
        publicDependency_.add(value);
        onChanged();
        return this;
      }

      public Builder addAllPublicDependency(
          java.lang.Iterable<? extends java.lang.Integer> values) {
        ensurePublicDependencyIsMutable();
        super.addAll(values, publicDependency_);
        onChanged();
        return this;
      }

      public Builder clearPublicDependency() {
        publicDependency_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
        return this;
      }

      private java.util.List<java.lang.Integer> weakDependency_ = java.util.Collections.emptyList();
      private void ensureWeakDependencyIsMutable() {
        if (!((bitField0_ & 0x00000010) == 0x00000010)) {
          weakDependency_ = new java.util.ArrayList<java.lang.Integer>(weakDependency_);
          bitField0_ |= 0x00000010;
         }
      }

      public java.util.List<java.lang.Integer>
          getWeakDependencyList() {
        return java.util.Collections.unmodifiableList(weakDependency_);
      }

      public int getWeakDependencyCount() {
        return weakDependency_.size();
      }

      public int getWeakDependency(int index) {
        return weakDependency_.get(index);
      }

      public Builder setWeakDependency(
          int index, int value) {
        ensureWeakDependencyIsMutable();
        weakDependency_.set(index, value);
        onChanged();
        return this;
      }

      public Builder addWeakDependency(int value) {
        ensureWeakDependencyIsMutable();
        weakDependency_.add(value);
        onChanged();
        return this;
      }

      public Builder addAllWeakDependency(
          java.lang.Iterable<? extends java.lang.Integer> values) {
        ensureWeakDependencyIsMutable();
        super.addAll(values, weakDependency_);
        onChanged();
        return this;
      }

      public Builder clearWeakDependency() {
        weakDependency_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000010);
        onChanged();
        return this;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> messageType_ =
        java.util.Collections.emptyList();
      private void ensureMessageTypeIsMutable() {
        if (!((bitField0_ & 0x00000020) == 0x00000020)) {
          messageType_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.DescriptorProto>(messageType_);
          bitField0_ |= 0x00000020;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.DescriptorProto, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder, com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> messageTypeBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> getMessageTypeList() {
        if (messageTypeBuilder_ == null) {
          return java.util.Collections.unmodifiableList(messageType_);
        } else {
          return messageTypeBuilder_.getMessageList();
        }
      }

      public int getMessageTypeCount() {
        if (messageTypeBuilder_ == null) {
          return messageType_.size();
        } else {
          return messageTypeBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto getMessageType(int index) {
        if (messageTypeBuilder_ == null) {
          return messageType_.get(index);
        } else {
          return messageTypeBuilder_.getMessage(index);
        }
      }

      public Builder setMessageType(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto value) {
        if (messageTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureMessageTypeIsMutable();
          messageType_.set(index, value);
          onChanged();
        } else {
          messageTypeBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setMessageType(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder builderForValue) {
        if (messageTypeBuilder_ == null) {
          ensureMessageTypeIsMutable();
          messageType_.set(index, builderForValue.build());
          onChanged();
        } else {
          messageTypeBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addMessageType(com.google.protobuf.DescriptorProtos.DescriptorProto value) {
        if (messageTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureMessageTypeIsMutable();
          messageType_.add(value);
          onChanged();
        } else {
          messageTypeBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addMessageType(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto value) {
        if (messageTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureMessageTypeIsMutable();
          messageType_.add(index, value);
          onChanged();
        } else {
          messageTypeBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addMessageType(
          com.google.protobuf.DescriptorProtos.DescriptorProto.Builder builderForValue) {
        if (messageTypeBuilder_ == null) {
          ensureMessageTypeIsMutable();
          messageType_.add(builderForValue.build());
          onChanged();
        } else {
          messageTypeBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addMessageType(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder builderForValue) {
        if (messageTypeBuilder_ == null) {
          ensureMessageTypeIsMutable();
          messageType_.add(index, builderForValue.build());
          onChanged();
        } else {
          messageTypeBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllMessageType(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.DescriptorProto> values) {
        if (messageTypeBuilder_ == null) {
          ensureMessageTypeIsMutable();
          super.addAll(values, messageType_);
          onChanged();
        } else {
          messageTypeBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearMessageType() {
        if (messageTypeBuilder_ == null) {
          messageType_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
          onChanged();
        } else {
          messageTypeBuilder_.clear();
        }
        return this;
      }

      public Builder removeMessageType(int index) {
        if (messageTypeBuilder_ == null) {
          ensureMessageTypeIsMutable();
          messageType_.remove(index);
          onChanged();
        } else {
          messageTypeBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.Builder getMessageTypeBuilder(
          int index) {
        return getMessageTypeFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder getMessageTypeOrBuilder(
          int index) {
        if (messageTypeBuilder_ == null) {
          return messageType_.get(index);  } else {
          return messageTypeBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> 
           getMessageTypeOrBuilderList() {
        if (messageTypeBuilder_ != null) {
          return messageTypeBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(messageType_);
        }
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.Builder addMessageTypeBuilder() {
        return getMessageTypeFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.DescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.Builder addMessageTypeBuilder(
          int index) {
        return getMessageTypeFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.DescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto.Builder> 
           getMessageTypeBuilderList() {
        return getMessageTypeFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.DescriptorProto, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder, com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> 
          getMessageTypeFieldBuilder() {
        if (messageTypeBuilder_ == null) {
          messageTypeBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.DescriptorProto, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder, com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder>(
                  messageType_,
                  ((bitField0_ & 0x00000020) == 0x00000020),
                  getParentForChildren(),
                  isClean());
          messageType_ = null;
        }
        return messageTypeBuilder_;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> enumType_ =
        java.util.Collections.emptyList();
      private void ensureEnumTypeIsMutable() {
        if (!((bitField0_ & 0x00000040) == 0x00000040)) {
          enumType_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.EnumDescriptorProto>(enumType_);
          bitField0_ |= 0x00000040;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumDescriptorProto, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> enumTypeBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> getEnumTypeList() {
        if (enumTypeBuilder_ == null) {
          return java.util.Collections.unmodifiableList(enumType_);
        } else {
          return enumTypeBuilder_.getMessageList();
        }
      }

      public int getEnumTypeCount() {
        if (enumTypeBuilder_ == null) {
          return enumType_.size();
        } else {
          return enumTypeBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto getEnumType(int index) {
        if (enumTypeBuilder_ == null) {
          return enumType_.get(index);
        } else {
          return enumTypeBuilder_.getMessage(index);
        }
      }

      public Builder setEnumType(
          int index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto value) {
        if (enumTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureEnumTypeIsMutable();
          enumType_.set(index, value);
          onChanged();
        } else {
          enumTypeBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setEnumType(
          int index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder builderForValue) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          enumType_.set(index, builderForValue.build());
          onChanged();
        } else {
          enumTypeBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addEnumType(com.google.protobuf.DescriptorProtos.EnumDescriptorProto value) {
        if (enumTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureEnumTypeIsMutable();
          enumType_.add(value);
          onChanged();
        } else {
          enumTypeBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addEnumType(
          int index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto value) {
        if (enumTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureEnumTypeIsMutable();
          enumType_.add(index, value);
          onChanged();
        } else {
          enumTypeBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addEnumType(
          com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder builderForValue) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          enumType_.add(builderForValue.build());
          onChanged();
        } else {
          enumTypeBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addEnumType(
          int index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder builderForValue) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          enumType_.add(index, builderForValue.build());
          onChanged();
        } else {
          enumTypeBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllEnumType(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.EnumDescriptorProto> values) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          super.addAll(values, enumType_);
          onChanged();
        } else {
          enumTypeBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearEnumType() {
        if (enumTypeBuilder_ == null) {
          enumType_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
          onChanged();
        } else {
          enumTypeBuilder_.clear();
        }
        return this;
      }

      public Builder removeEnumType(int index) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          enumType_.remove(index);
          onChanged();
        } else {
          enumTypeBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder getEnumTypeBuilder(
          int index) {
        return getEnumTypeFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(
          int index) {
        if (enumTypeBuilder_ == null) {
          return enumType_.get(index);  } else {
          return enumTypeBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> 
           getEnumTypeOrBuilderList() {
        if (enumTypeBuilder_ != null) {
          return enumTypeBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(enumType_);
        }
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder addEnumTypeBuilder() {
        return getEnumTypeFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.EnumDescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder addEnumTypeBuilder(
          int index) {
        return getEnumTypeFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder> 
           getEnumTypeBuilderList() {
        return getEnumTypeFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumDescriptorProto, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> 
          getEnumTypeFieldBuilder() {
        if (enumTypeBuilder_ == null) {
          enumTypeBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.EnumDescriptorProto, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder>(
                  enumType_,
                  ((bitField0_ & 0x00000040) == 0x00000040),
                  getParentForChildren(),
                  isClean());
          enumType_ = null;
        }
        return enumTypeBuilder_;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.ServiceDescriptorProto> service_ =
        java.util.Collections.emptyList();
      private void ensureServiceIsMutable() {
        if (!((bitField0_ & 0x00000080) == 0x00000080)) {
          service_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.ServiceDescriptorProto>(service_);
          bitField0_ |= 0x00000080;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.ServiceDescriptorProto, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder> serviceBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.ServiceDescriptorProto> getServiceList() {
        if (serviceBuilder_ == null) {
          return java.util.Collections.unmodifiableList(service_);
        } else {
          return serviceBuilder_.getMessageList();
        }
      }

      public int getServiceCount() {
        if (serviceBuilder_ == null) {
          return service_.size();
        } else {
          return serviceBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.ServiceDescriptorProto getService(int index) {
        if (serviceBuilder_ == null) {
          return service_.get(index);
        } else {
          return serviceBuilder_.getMessage(index);
        }
      }

      public Builder setService(
          int index, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto value) {
        if (serviceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureServiceIsMutable();
          service_.set(index, value);
          onChanged();
        } else {
          serviceBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setService(
          int index, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder builderForValue) {
        if (serviceBuilder_ == null) {
          ensureServiceIsMutable();
          service_.set(index, builderForValue.build());
          onChanged();
        } else {
          serviceBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addService(com.google.protobuf.DescriptorProtos.ServiceDescriptorProto value) {
        if (serviceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureServiceIsMutable();
          service_.add(value);
          onChanged();
        } else {
          serviceBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addService(
          int index, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto value) {
        if (serviceBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureServiceIsMutable();
          service_.add(index, value);
          onChanged();
        } else {
          serviceBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addService(
          com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder builderForValue) {
        if (serviceBuilder_ == null) {
          ensureServiceIsMutable();
          service_.add(builderForValue.build());
          onChanged();
        } else {
          serviceBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addService(
          int index, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder builderForValue) {
        if (serviceBuilder_ == null) {
          ensureServiceIsMutable();
          service_.add(index, builderForValue.build());
          onChanged();
        } else {
          serviceBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllService(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.ServiceDescriptorProto> values) {
        if (serviceBuilder_ == null) {
          ensureServiceIsMutable();
          super.addAll(values, service_);
          onChanged();
        } else {
          serviceBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearService() {
        if (serviceBuilder_ == null) {
          service_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000080);
          onChanged();
        } else {
          serviceBuilder_.clear();
        }
        return this;
      }

      public Builder removeService(int index) {
        if (serviceBuilder_ == null) {
          ensureServiceIsMutable();
          service_.remove(index);
          onChanged();
        } else {
          serviceBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder getServiceBuilder(
          int index) {
        return getServiceFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder getServiceOrBuilder(
          int index) {
        if (serviceBuilder_ == null) {
          return service_.get(index);  } else {
          return serviceBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder> 
           getServiceOrBuilderList() {
        if (serviceBuilder_ != null) {
          return serviceBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(service_);
        }
      }

      public com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder addServiceBuilder() {
        return getServiceFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder addServiceBuilder(
          int index) {
        return getServiceFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder> 
           getServiceBuilderList() {
        return getServiceFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.ServiceDescriptorProto, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder> 
          getServiceFieldBuilder() {
        if (serviceBuilder_ == null) {
          serviceBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.ServiceDescriptorProto, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder>(
                  service_,
                  ((bitField0_ & 0x00000080) == 0x00000080),
                  getParentForChildren(),
                  isClean());
          service_ = null;
        }
        return serviceBuilder_;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> extension_ =
        java.util.Collections.emptyList();
      private void ensureExtensionIsMutable() {
        if (!((bitField0_ & 0x00000100) == 0x00000100)) {
          extension_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.FieldDescriptorProto>(extension_);
          bitField0_ |= 0x00000100;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.FieldDescriptorProto, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> extensionBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> getExtensionList() {
        if (extensionBuilder_ == null) {
          return java.util.Collections.unmodifiableList(extension_);
        } else {
          return extensionBuilder_.getMessageList();
        }
      }

      public int getExtensionCount() {
        if (extensionBuilder_ == null) {
          return extension_.size();
        } else {
          return extensionBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto getExtension(int index) {
        if (extensionBuilder_ == null) {
          return extension_.get(index);
        } else {
          return extensionBuilder_.getMessage(index);
        }
      }

      public Builder setExtension(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto value) {
        if (extensionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtensionIsMutable();
          extension_.set(index, value);
          onChanged();
        } else {
          extensionBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setExtension(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder builderForValue) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          extension_.set(index, builderForValue.build());
          onChanged();
        } else {
          extensionBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addExtension(com.google.protobuf.DescriptorProtos.FieldDescriptorProto value) {
        if (extensionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtensionIsMutable();
          extension_.add(value);
          onChanged();
        } else {
          extensionBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addExtension(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto value) {
        if (extensionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtensionIsMutable();
          extension_.add(index, value);
          onChanged();
        } else {
          extensionBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addExtension(
          com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder builderForValue) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          extension_.add(builderForValue.build());
          onChanged();
        } else {
          extensionBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addExtension(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder builderForValue) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          extension_.add(index, builderForValue.build());
          onChanged();
        } else {
          extensionBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllExtension(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProto> values) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          super.addAll(values, extension_);
          onChanged();
        } else {
          extensionBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearExtension() {
        if (extensionBuilder_ == null) {
          extension_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000100);
          onChanged();
        } else {
          extensionBuilder_.clear();
        }
        return this;
      }

      public Builder removeExtension(int index) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          extension_.remove(index);
          onChanged();
        } else {
          extensionBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder getExtensionBuilder(
          int index) {
        return getExtensionFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(
          int index) {
        if (extensionBuilder_ == null) {
          return extension_.get(index);  } else {
          return extensionBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
           getExtensionOrBuilderList() {
        if (extensionBuilder_ != null) {
          return extensionBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(extension_);
        }
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder addExtensionBuilder() {
        return getExtensionFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder addExtensionBuilder(
          int index) {
        return getExtensionFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder> 
           getExtensionBuilderList() {
        return getExtensionFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.FieldDescriptorProto, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
          getExtensionFieldBuilder() {
        if (extensionBuilder_ == null) {
          extensionBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.FieldDescriptorProto, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder>(
                  extension_,
                  ((bitField0_ & 0x00000100) == 0x00000100),
                  getParentForChildren(),
                  isClean());
          extension_ = null;
        }
        return extensionBuilder_;
      }

      private com.google.protobuf.DescriptorProtos.FileOptions options_ = com.google.protobuf.DescriptorProtos.FileOptions.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.FileOptions, com.google.protobuf.DescriptorProtos.FileOptions.Builder, com.google.protobuf.DescriptorProtos.FileOptionsOrBuilder> optionsBuilder_;

      public boolean hasOptions() {
        return ((bitField0_ & 0x00000200) == 0x00000200);
      }

      public com.google.protobuf.DescriptorProtos.FileOptions getOptions() {
        if (optionsBuilder_ == null) {
          return options_;
        } else {
          return optionsBuilder_.getMessage();
        }
      }

      public Builder setOptions(com.google.protobuf.DescriptorProtos.FileOptions value) {
        if (optionsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          options_ = value;
          onChanged();
        } else {
          optionsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000200;
        return this;
      }

      public Builder setOptions(
          com.google.protobuf.DescriptorProtos.FileOptions.Builder builderForValue) {
        if (optionsBuilder_ == null) {
          options_ = builderForValue.build();
          onChanged();
        } else {
          optionsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000200;
        return this;
      }

      public Builder mergeOptions(com.google.protobuf.DescriptorProtos.FileOptions value) {
        if (optionsBuilder_ == null) {
          if (((bitField0_ & 0x00000200) == 0x00000200) &&
              options_ != com.google.protobuf.DescriptorProtos.FileOptions.getDefaultInstance()) {
            options_ =
              com.google.protobuf.DescriptorProtos.FileOptions.newBuilder(options_).mergeFrom(value).buildPartial();
          } else {
            options_ = value;
          }
          onChanged();
        } else {
          optionsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000200;
        return this;
      }

      public Builder clearOptions() {
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.FileOptions.getDefaultInstance();
          onChanged();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000200);
        return this;
      }

      public com.google.protobuf.DescriptorProtos.FileOptions.Builder getOptionsBuilder() {
        bitField0_ |= 0x00000200;
        onChanged();
        return getOptionsFieldBuilder().getBuilder();
      }

      public com.google.protobuf.DescriptorProtos.FileOptionsOrBuilder getOptionsOrBuilder() {
        if (optionsBuilder_ != null) {
          return optionsBuilder_.getMessageOrBuilder();
        } else {
          return options_;
        }
      }

      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.FileOptions, com.google.protobuf.DescriptorProtos.FileOptions.Builder, com.google.protobuf.DescriptorProtos.FileOptionsOrBuilder> 
          getOptionsFieldBuilder() {
        if (optionsBuilder_ == null) {
          optionsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.google.protobuf.DescriptorProtos.FileOptions, com.google.protobuf.DescriptorProtos.FileOptions.Builder, com.google.protobuf.DescriptorProtos.FileOptionsOrBuilder>(
                  options_,
                  getParentForChildren(),
                  isClean());
          options_ = null;
        }
        return optionsBuilder_;
      }

      private com.google.protobuf.DescriptorProtos.SourceCodeInfo sourceCodeInfo_ = com.google.protobuf.DescriptorProtos.SourceCodeInfo.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.SourceCodeInfo, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Builder, com.google.protobuf.DescriptorProtos.SourceCodeInfoOrBuilder> sourceCodeInfoBuilder_;

      public boolean hasSourceCodeInfo() {
        return ((bitField0_ & 0x00000400) == 0x00000400);
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo getSourceCodeInfo() {
        if (sourceCodeInfoBuilder_ == null) {
          return sourceCodeInfo_;
        } else {
          return sourceCodeInfoBuilder_.getMessage();
        }
      }

      public Builder setSourceCodeInfo(com.google.protobuf.DescriptorProtos.SourceCodeInfo value) {
        if (sourceCodeInfoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          sourceCodeInfo_ = value;
          onChanged();
        } else {
          sourceCodeInfoBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000400;
        return this;
      }

      public Builder setSourceCodeInfo(
          com.google.protobuf.DescriptorProtos.SourceCodeInfo.Builder builderForValue) {
        if (sourceCodeInfoBuilder_ == null) {
          sourceCodeInfo_ = builderForValue.build();
          onChanged();
        } else {
          sourceCodeInfoBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000400;
        return this;
      }

      public Builder mergeSourceCodeInfo(com.google.protobuf.DescriptorProtos.SourceCodeInfo value) {
        if (sourceCodeInfoBuilder_ == null) {
          if (((bitField0_ & 0x00000400) == 0x00000400) &&
              sourceCodeInfo_ != com.google.protobuf.DescriptorProtos.SourceCodeInfo.getDefaultInstance()) {
            sourceCodeInfo_ =
              com.google.protobuf.DescriptorProtos.SourceCodeInfo.newBuilder(sourceCodeInfo_).mergeFrom(value).buildPartial();
          } else {
            sourceCodeInfo_ = value;
          }
          onChanged();
        } else {
          sourceCodeInfoBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000400;
        return this;
      }

      public Builder clearSourceCodeInfo() {
        if (sourceCodeInfoBuilder_ == null) {
          sourceCodeInfo_ = com.google.protobuf.DescriptorProtos.SourceCodeInfo.getDefaultInstance();
          onChanged();
        } else {
          sourceCodeInfoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000400);
        return this;
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo.Builder getSourceCodeInfoBuilder() {
        bitField0_ |= 0x00000400;
        onChanged();
        return getSourceCodeInfoFieldBuilder().getBuilder();
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfoOrBuilder getSourceCodeInfoOrBuilder() {
        if (sourceCodeInfoBuilder_ != null) {
          return sourceCodeInfoBuilder_.getMessageOrBuilder();
        } else {
          return sourceCodeInfo_;
        }
      }

      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.SourceCodeInfo, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Builder, com.google.protobuf.DescriptorProtos.SourceCodeInfoOrBuilder> 
          getSourceCodeInfoFieldBuilder() {
        if (sourceCodeInfoBuilder_ == null) {
          sourceCodeInfoBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.google.protobuf.DescriptorProtos.SourceCodeInfo, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Builder, com.google.protobuf.DescriptorProtos.SourceCodeInfoOrBuilder>(
                  sourceCodeInfo_,
                  getParentForChildren(),
                  isClean());
          sourceCodeInfo_ = null;
        }
        return sourceCodeInfoBuilder_;
      }

    }

    static {
      defaultInstance = new FileDescriptorProto(true);
      defaultInstance.initFields();
    }

  }

  public interface DescriptorProtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    boolean hasName();

    java.lang.String getName();

    com.google.protobuf.ByteString
        getNameBytes();

    java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> 
        getFieldList();

    com.google.protobuf.DescriptorProtos.FieldDescriptorProto getField(int index);

    int getFieldCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
        getFieldOrBuilderList();

    com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder getFieldOrBuilder(
        int index);

    java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> 
        getExtensionList();

    com.google.protobuf.DescriptorProtos.FieldDescriptorProto getExtension(int index);

    int getExtensionCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
        getExtensionOrBuilderList();

    com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(
        int index);

    java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> 
        getNestedTypeList();

    com.google.protobuf.DescriptorProtos.DescriptorProto getNestedType(int index);

    int getNestedTypeCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> 
        getNestedTypeOrBuilderList();

    com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder getNestedTypeOrBuilder(
        int index);

    java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> 
        getEnumTypeList();

    com.google.protobuf.DescriptorProtos.EnumDescriptorProto getEnumType(int index);

    int getEnumTypeCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> 
        getEnumTypeOrBuilderList();

    com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(
        int index);

    java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange> 
        getExtensionRangeList();

    com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange getExtensionRange(int index);

    int getExtensionRangeCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder> 
        getExtensionRangeOrBuilderList();

    com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder getExtensionRangeOrBuilder(
        int index);

    boolean hasOptions();

    com.google.protobuf.DescriptorProtos.MessageOptions getOptions();

    com.google.protobuf.DescriptorProtos.MessageOptionsOrBuilder getOptionsOrBuilder();
  }

  public static final class DescriptorProto extends
      com.google.protobuf.GeneratedMessage
      implements DescriptorProtoOrBuilder {

    private DescriptorProto(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private DescriptorProto(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final DescriptorProto defaultInstance;
    public static DescriptorProto getDefaultInstance() {
      return defaultInstance;
    }

    public DescriptorProto getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private DescriptorProto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              name_ = input.readBytes();
              break;
            }
            case 18: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                field_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.FieldDescriptorProto>();
                mutable_bitField0_ |= 0x00000002;
              }
              field_.add(input.readMessage(com.google.protobuf.DescriptorProtos.FieldDescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 26: {
              if (!((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
                nestedType_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.DescriptorProto>();
                mutable_bitField0_ |= 0x00000008;
              }
              nestedType_.add(input.readMessage(com.google.protobuf.DescriptorProtos.DescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 34: {
              if (!((mutable_bitField0_ & 0x00000010) == 0x00000010)) {
                enumType_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.EnumDescriptorProto>();
                mutable_bitField0_ |= 0x00000010;
              }
              enumType_.add(input.readMessage(com.google.protobuf.DescriptorProtos.EnumDescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 42: {
              if (!((mutable_bitField0_ & 0x00000020) == 0x00000020)) {
                extensionRange_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange>();
                mutable_bitField0_ |= 0x00000020;
              }
              extensionRange_.add(input.readMessage(com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.PARSER, extensionRegistry));
              break;
            }
            case 50: {
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
                extension_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.FieldDescriptorProto>();
                mutable_bitField0_ |= 0x00000004;
              }
              extension_.add(input.readMessage(com.google.protobuf.DescriptorProtos.FieldDescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 58: {
              com.google.protobuf.DescriptorProtos.MessageOptions.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = options_.toBuilder();
              }
              options_ = input.readMessage(com.google.protobuf.DescriptorProtos.MessageOptions.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(options_);
                options_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          field_ = java.util.Collections.unmodifiableList(field_);
        }
        if (((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
          nestedType_ = java.util.Collections.unmodifiableList(nestedType_);
        }
        if (((mutable_bitField0_ & 0x00000010) == 0x00000010)) {
          enumType_ = java.util.Collections.unmodifiableList(enumType_);
        }
        if (((mutable_bitField0_ & 0x00000020) == 0x00000020)) {
          extensionRange_ = java.util.Collections.unmodifiableList(extensionRange_);
        }
        if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
          extension_ = java.util.Collections.unmodifiableList(extension_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.DescriptorProto.class, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder.class);
    }

    public static com.google.protobuf.Parser<DescriptorProto> PARSER =
        new com.google.protobuf.AbstractParser<DescriptorProto>() {
      public DescriptorProto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new DescriptorProto(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<DescriptorProto> getParserForType() {
      return PARSER;
    }

    public interface ExtensionRangeOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      boolean hasStart();

      int getStart();

      boolean hasEnd();

      int getEnd();
    }

    public static final class ExtensionRange extends
        com.google.protobuf.GeneratedMessage
        implements ExtensionRangeOrBuilder {

      private ExtensionRange(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private ExtensionRange(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final ExtensionRange defaultInstance;
      public static ExtensionRange getDefaultInstance() {
        return defaultInstance;
      }

      public ExtensionRange getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private ExtensionRange(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        initFields();
        int mutable_bitField0_ = 0;
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
            com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              default: {
                if (!parseUnknownField(input, unknownFields,
                                       extensionRegistry, tag)) {
                  done = true;
                }
                break;
              }
              case 8: {
                bitField0_ |= 0x00000001;
                start_ = input.readInt32();
                break;
              }
              case 16: {
                bitField0_ |= 0x00000002;
                end_ = input.readInt32();
                break;
              }
            }
          }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this);
        } finally {
          this.unknownFields = unknownFields.build();
          makeExtensionsImmutable();
        }
      }
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.class, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder.class);
      }

      public static com.google.protobuf.Parser<ExtensionRange> PARSER =
          new com.google.protobuf.AbstractParser<ExtensionRange>() {
        public ExtensionRange parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new ExtensionRange(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<ExtensionRange> getParserForType() {
        return PARSER;
      }

      private int bitField0_;

      public static final int START_FIELD_NUMBER = 1;
      private int start_;

      public boolean hasStart() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public int getStart() {
        return start_;
      }

      public static final int END_FIELD_NUMBER = 2;
      private int end_;

      public boolean hasEnd() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public int getEnd() {
        return end_;
      }

      private void initFields() {
        start_ = 0;
        end_ = 0;
      }
      private byte memoizedIsInitialized = -1;
      public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized != -1) return isInitialized == 1;

        memoizedIsInitialized = 1;
        return true;
      }

      public void writeTo(com.google.protobuf.CodedOutputStream output)
                          throws java.io.IOException {
        getSerializedSize();
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          output.writeInt32(1, start_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeInt32(2, end_);
        }
        getUnknownFields().writeTo(output);
      }

      private int memoizedSerializedSize = -1;
      public int getSerializedSize() {
        int size = memoizedSerializedSize;
        if (size != -1) return size;

        size = 0;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          size += com.google.protobuf.CodedOutputStream
            .computeInt32Size(1, start_);
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeInt32Size(2, end_);
        }
        size += getUnknownFields().getSerializedSize();
        memoizedSerializedSize = size;
        return size;
      }

      private static final long serialVersionUID = 0L;
      @java.lang.Override
      protected java.lang.Object writeReplace()
          throws java.io.ObjectStreamException {
        return super.writeReplace();
      }

      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange prototype) {
        return newBuilder().mergeFrom(prototype);
      }
      public Builder toBuilder() { return newBuilder(this); }

      @java.lang.Override
      protected Builder newBuilderForType(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
      }

      public static final class Builder extends
          com.google.protobuf.GeneratedMessage.Builder<Builder>
         implements com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.class, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder.class);
        }

        private Builder() {
          maybeForceBuilderInitialization();
        }

        private Builder(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
          super(parent);
          maybeForceBuilderInitialization();
        }
        private void maybeForceBuilderInitialization() {
          if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          }
        }
        private static Builder create() {
          return new Builder();
        }

        public Builder clear() {
          super.clear();
          start_ = 0;
          bitField0_ = (bitField0_ & ~0x00000001);
          end_ = 0;
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
        }

        public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange getDefaultInstanceForType() {
          return com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.getDefaultInstance();
        }

        public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange build() {
          com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange buildPartial() {
          com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange result = new com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.start_ = start_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.end_ = end_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange) {
            return mergeFrom((com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange other) {
          if (other == com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.getDefaultInstance()) return this;
          if (other.hasStart()) {
            setStart(other.getStart());
          }
          if (other.hasEnd()) {
            setEnd(other.getEnd());
          }
          this.mergeUnknownFields(other.getUnknownFields());
          return this;
        }

        public final boolean isInitialized() {
          return true;
        }

        public Builder mergeFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
          com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private int start_ ;

        public boolean hasStart() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        public int getStart() {
          return start_;
        }

        public Builder setStart(int value) {
          bitField0_ |= 0x00000001;
          start_ = value;
          onChanged();
          return this;
        }

        public Builder clearStart() {
          bitField0_ = (bitField0_ & ~0x00000001);
          start_ = 0;
          onChanged();
          return this;
        }

        private int end_ ;

        public boolean hasEnd() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }

        public int getEnd() {
          return end_;
        }

        public Builder setEnd(int value) {
          bitField0_ |= 0x00000002;
          end_ = value;
          onChanged();
          return this;
        }

        public Builder clearEnd() {
          bitField0_ = (bitField0_ & ~0x00000002);
          end_ = 0;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new ExtensionRange(true);
        defaultInstance.initFields();
      }

    }

    private int bitField0_;

    public static final int NAME_FIELD_NUMBER = 1;
    private java.lang.Object name_;

    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int FIELD_FIELD_NUMBER = 2;
    private java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> field_;

    public java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> getFieldList() {
      return field_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
        getFieldOrBuilderList() {
      return field_;
    }

    public int getFieldCount() {
      return field_.size();
    }

    public com.google.protobuf.DescriptorProtos.FieldDescriptorProto getField(int index) {
      return field_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder getFieldOrBuilder(
        int index) {
      return field_.get(index);
    }

    public static final int EXTENSION_FIELD_NUMBER = 6;
    private java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> extension_;

    public java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> getExtensionList() {
      return extension_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
        getExtensionOrBuilderList() {
      return extension_;
    }

    public int getExtensionCount() {
      return extension_.size();
    }

    public com.google.protobuf.DescriptorProtos.FieldDescriptorProto getExtension(int index) {
      return extension_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(
        int index) {
      return extension_.get(index);
    }

    public static final int NESTED_TYPE_FIELD_NUMBER = 3;
    private java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> nestedType_;

    public java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> getNestedTypeList() {
      return nestedType_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> 
        getNestedTypeOrBuilderList() {
      return nestedType_;
    }

    public int getNestedTypeCount() {
      return nestedType_.size();
    }

    public com.google.protobuf.DescriptorProtos.DescriptorProto getNestedType(int index) {
      return nestedType_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder getNestedTypeOrBuilder(
        int index) {
      return nestedType_.get(index);
    }

    public static final int ENUM_TYPE_FIELD_NUMBER = 4;
    private java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> enumType_;

    public java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> getEnumTypeList() {
      return enumType_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> 
        getEnumTypeOrBuilderList() {
      return enumType_;
    }

    public int getEnumTypeCount() {
      return enumType_.size();
    }

    public com.google.protobuf.DescriptorProtos.EnumDescriptorProto getEnumType(int index) {
      return enumType_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(
        int index) {
      return enumType_.get(index);
    }

    public static final int EXTENSION_RANGE_FIELD_NUMBER = 5;
    private java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange> extensionRange_;

    public java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange> getExtensionRangeList() {
      return extensionRange_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder> 
        getExtensionRangeOrBuilderList() {
      return extensionRange_;
    }

    public int getExtensionRangeCount() {
      return extensionRange_.size();
    }

    public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange getExtensionRange(int index) {
      return extensionRange_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder getExtensionRangeOrBuilder(
        int index) {
      return extensionRange_.get(index);
    }

    public static final int OPTIONS_FIELD_NUMBER = 7;
    private com.google.protobuf.DescriptorProtos.MessageOptions options_;

    public boolean hasOptions() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public com.google.protobuf.DescriptorProtos.MessageOptions getOptions() {
      return options_;
    }

    public com.google.protobuf.DescriptorProtos.MessageOptionsOrBuilder getOptionsOrBuilder() {
      return options_;
    }

    private void initFields() {
      name_ = "";
      field_ = java.util.Collections.emptyList();
      extension_ = java.util.Collections.emptyList();
      nestedType_ = java.util.Collections.emptyList();
      enumType_ = java.util.Collections.emptyList();
      extensionRange_ = java.util.Collections.emptyList();
      options_ = com.google.protobuf.DescriptorProtos.MessageOptions.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getFieldCount(); i++) {
        if (!getField(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getExtensionCount(); i++) {
        if (!getExtension(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getNestedTypeCount(); i++) {
        if (!getNestedType(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      for (int i = 0; i < getEnumTypeCount(); i++) {
        if (!getEnumType(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (hasOptions()) {
        if (!getOptions().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getNameBytes());
      }
      for (int i = 0; i < field_.size(); i++) {
        output.writeMessage(2, field_.get(i));
      }
      for (int i = 0; i < nestedType_.size(); i++) {
        output.writeMessage(3, nestedType_.get(i));
      }
      for (int i = 0; i < enumType_.size(); i++) {
        output.writeMessage(4, enumType_.get(i));
      }
      for (int i = 0; i < extensionRange_.size(); i++) {
        output.writeMessage(5, extensionRange_.get(i));
      }
      for (int i = 0; i < extension_.size(); i++) {
        output.writeMessage(6, extension_.get(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(7, options_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getNameBytes());
      }
      for (int i = 0; i < field_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, field_.get(i));
      }
      for (int i = 0; i < nestedType_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, nestedType_.get(i));
      }
      for (int i = 0; i < enumType_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, enumType_.get(i));
      }
      for (int i = 0; i < extensionRange_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(5, extensionRange_.get(i));
      }
      for (int i = 0; i < extension_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, extension_.get(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(7, options_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.DescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.DescriptorProto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.DescriptorProto.class, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getFieldFieldBuilder();
          getExtensionFieldBuilder();
          getNestedTypeFieldBuilder();
          getEnumTypeFieldBuilder();
          getExtensionRangeFieldBuilder();
          getOptionsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (fieldBuilder_ == null) {
          field_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          fieldBuilder_.clear();
        }
        if (extensionBuilder_ == null) {
          extension_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
        } else {
          extensionBuilder_.clear();
        }
        if (nestedTypeBuilder_ == null) {
          nestedType_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
        } else {
          nestedTypeBuilder_.clear();
        }
        if (enumTypeBuilder_ == null) {
          enumType_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000010);
        } else {
          enumTypeBuilder_.clear();
        }
        if (extensionRangeBuilder_ == null) {
          extensionRange_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
        } else {
          extensionRangeBuilder_.clear();
        }
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.MessageOptions.getDefaultInstance();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_DescriptorProto_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.DescriptorProto.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto build() {
        com.google.protobuf.DescriptorProtos.DescriptorProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto buildPartial() {
        com.google.protobuf.DescriptorProtos.DescriptorProto result = new com.google.protobuf.DescriptorProtos.DescriptorProto(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (fieldBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            field_ = java.util.Collections.unmodifiableList(field_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.field_ = field_;
        } else {
          result.field_ = fieldBuilder_.build();
        }
        if (extensionBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004)) {
            extension_ = java.util.Collections.unmodifiableList(extension_);
            bitField0_ = (bitField0_ & ~0x00000004);
          }
          result.extension_ = extension_;
        } else {
          result.extension_ = extensionBuilder_.build();
        }
        if (nestedTypeBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008)) {
            nestedType_ = java.util.Collections.unmodifiableList(nestedType_);
            bitField0_ = (bitField0_ & ~0x00000008);
          }
          result.nestedType_ = nestedType_;
        } else {
          result.nestedType_ = nestedTypeBuilder_.build();
        }
        if (enumTypeBuilder_ == null) {
          if (((bitField0_ & 0x00000010) == 0x00000010)) {
            enumType_ = java.util.Collections.unmodifiableList(enumType_);
            bitField0_ = (bitField0_ & ~0x00000010);
          }
          result.enumType_ = enumType_;
        } else {
          result.enumType_ = enumTypeBuilder_.build();
        }
        if (extensionRangeBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020)) {
            extensionRange_ = java.util.Collections.unmodifiableList(extensionRange_);
            bitField0_ = (bitField0_ & ~0x00000020);
          }
          result.extensionRange_ = extensionRange_;
        } else {
          result.extensionRange_ = extensionRangeBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000002;
        }
        if (optionsBuilder_ == null) {
          result.options_ = options_;
        } else {
          result.options_ = optionsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.DescriptorProto) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.DescriptorProto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.DescriptorProto other) {
        if (other == com.google.protobuf.DescriptorProtos.DescriptorProto.getDefaultInstance()) return this;
        if (other.hasName()) {
          bitField0_ |= 0x00000001;
          name_ = other.name_;
          onChanged();
        }
        if (fieldBuilder_ == null) {
          if (!other.field_.isEmpty()) {
            if (field_.isEmpty()) {
              field_ = other.field_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureFieldIsMutable();
              field_.addAll(other.field_);
            }
            onChanged();
          }
        } else {
          if (!other.field_.isEmpty()) {
            if (fieldBuilder_.isEmpty()) {
              fieldBuilder_.dispose();
              fieldBuilder_ = null;
              field_ = other.field_;
              bitField0_ = (bitField0_ & ~0x00000002);
              fieldBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getFieldFieldBuilder() : null;
            } else {
              fieldBuilder_.addAllMessages(other.field_);
            }
          }
        }
        if (extensionBuilder_ == null) {
          if (!other.extension_.isEmpty()) {
            if (extension_.isEmpty()) {
              extension_ = other.extension_;
              bitField0_ = (bitField0_ & ~0x00000004);
            } else {
              ensureExtensionIsMutable();
              extension_.addAll(other.extension_);
            }
            onChanged();
          }
        } else {
          if (!other.extension_.isEmpty()) {
            if (extensionBuilder_.isEmpty()) {
              extensionBuilder_.dispose();
              extensionBuilder_ = null;
              extension_ = other.extension_;
              bitField0_ = (bitField0_ & ~0x00000004);
              extensionBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getExtensionFieldBuilder() : null;
            } else {
              extensionBuilder_.addAllMessages(other.extension_);
            }
          }
        }
        if (nestedTypeBuilder_ == null) {
          if (!other.nestedType_.isEmpty()) {
            if (nestedType_.isEmpty()) {
              nestedType_ = other.nestedType_;
              bitField0_ = (bitField0_ & ~0x00000008);
            } else {
              ensureNestedTypeIsMutable();
              nestedType_.addAll(other.nestedType_);
            }
            onChanged();
          }
        } else {
          if (!other.nestedType_.isEmpty()) {
            if (nestedTypeBuilder_.isEmpty()) {
              nestedTypeBuilder_.dispose();
              nestedTypeBuilder_ = null;
              nestedType_ = other.nestedType_;
              bitField0_ = (bitField0_ & ~0x00000008);
              nestedTypeBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getNestedTypeFieldBuilder() : null;
            } else {
              nestedTypeBuilder_.addAllMessages(other.nestedType_);
            }
          }
        }
        if (enumTypeBuilder_ == null) {
          if (!other.enumType_.isEmpty()) {
            if (enumType_.isEmpty()) {
              enumType_ = other.enumType_;
              bitField0_ = (bitField0_ & ~0x00000010);
            } else {
              ensureEnumTypeIsMutable();
              enumType_.addAll(other.enumType_);
            }
            onChanged();
          }
        } else {
          if (!other.enumType_.isEmpty()) {
            if (enumTypeBuilder_.isEmpty()) {
              enumTypeBuilder_.dispose();
              enumTypeBuilder_ = null;
              enumType_ = other.enumType_;
              bitField0_ = (bitField0_ & ~0x00000010);
              enumTypeBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getEnumTypeFieldBuilder() : null;
            } else {
              enumTypeBuilder_.addAllMessages(other.enumType_);
            }
          }
        }
        if (extensionRangeBuilder_ == null) {
          if (!other.extensionRange_.isEmpty()) {
            if (extensionRange_.isEmpty()) {
              extensionRange_ = other.extensionRange_;
              bitField0_ = (bitField0_ & ~0x00000020);
            } else {
              ensureExtensionRangeIsMutable();
              extensionRange_.addAll(other.extensionRange_);
            }
            onChanged();
          }
        } else {
          if (!other.extensionRange_.isEmpty()) {
            if (extensionRangeBuilder_.isEmpty()) {
              extensionRangeBuilder_.dispose();
              extensionRangeBuilder_ = null;
              extensionRange_ = other.extensionRange_;
              bitField0_ = (bitField0_ & ~0x00000020);
              extensionRangeBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getExtensionRangeFieldBuilder() : null;
            } else {
              extensionRangeBuilder_.addAllMessages(other.extensionRange_);
            }
          }
        }
        if (other.hasOptions()) {
          mergeOptions(other.getOptions());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getFieldCount(); i++) {
          if (!getField(i).isInitialized()) {

            return false;
          }
        }
        for (int i = 0; i < getExtensionCount(); i++) {
          if (!getExtension(i).isInitialized()) {

            return false;
          }
        }
        for (int i = 0; i < getNestedTypeCount(); i++) {
          if (!getNestedType(i).isInitialized()) {

            return false;
          }
        }
        for (int i = 0; i < getEnumTypeCount(); i++) {
          if (!getEnumType(i).isInitialized()) {

            return false;
          }
        }
        if (hasOptions()) {
          if (!getOptions().isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.DescriptorProto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.DescriptorProto) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object name_ = "";

      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }

      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> field_ =
        java.util.Collections.emptyList();
      private void ensureFieldIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          field_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.FieldDescriptorProto>(field_);
          bitField0_ |= 0x00000002;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.FieldDescriptorProto, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> fieldBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> getFieldList() {
        if (fieldBuilder_ == null) {
          return java.util.Collections.unmodifiableList(field_);
        } else {
          return fieldBuilder_.getMessageList();
        }
      }

      public int getFieldCount() {
        if (fieldBuilder_ == null) {
          return field_.size();
        } else {
          return fieldBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto getField(int index) {
        if (fieldBuilder_ == null) {
          return field_.get(index);
        } else {
          return fieldBuilder_.getMessage(index);
        }
      }

      public Builder setField(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto value) {
        if (fieldBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureFieldIsMutable();
          field_.set(index, value);
          onChanged();
        } else {
          fieldBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setField(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder builderForValue) {
        if (fieldBuilder_ == null) {
          ensureFieldIsMutable();
          field_.set(index, builderForValue.build());
          onChanged();
        } else {
          fieldBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addField(com.google.protobuf.DescriptorProtos.FieldDescriptorProto value) {
        if (fieldBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureFieldIsMutable();
          field_.add(value);
          onChanged();
        } else {
          fieldBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addField(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto value) {
        if (fieldBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureFieldIsMutable();
          field_.add(index, value);
          onChanged();
        } else {
          fieldBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addField(
          com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder builderForValue) {
        if (fieldBuilder_ == null) {
          ensureFieldIsMutable();
          field_.add(builderForValue.build());
          onChanged();
        } else {
          fieldBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addField(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder builderForValue) {
        if (fieldBuilder_ == null) {
          ensureFieldIsMutable();
          field_.add(index, builderForValue.build());
          onChanged();
        } else {
          fieldBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllField(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProto> values) {
        if (fieldBuilder_ == null) {
          ensureFieldIsMutable();
          super.addAll(values, field_);
          onChanged();
        } else {
          fieldBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearField() {
        if (fieldBuilder_ == null) {
          field_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          fieldBuilder_.clear();
        }
        return this;
      }

      public Builder removeField(int index) {
        if (fieldBuilder_ == null) {
          ensureFieldIsMutable();
          field_.remove(index);
          onChanged();
        } else {
          fieldBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder getFieldBuilder(
          int index) {
        return getFieldFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder getFieldOrBuilder(
          int index) {
        if (fieldBuilder_ == null) {
          return field_.get(index);  } else {
          return fieldBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
           getFieldOrBuilderList() {
        if (fieldBuilder_ != null) {
          return fieldBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(field_);
        }
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder addFieldBuilder() {
        return getFieldFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder addFieldBuilder(
          int index) {
        return getFieldFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder> 
           getFieldBuilderList() {
        return getFieldFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.FieldDescriptorProto, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
          getFieldFieldBuilder() {
        if (fieldBuilder_ == null) {
          fieldBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.FieldDescriptorProto, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder>(
                  field_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          field_ = null;
        }
        return fieldBuilder_;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> extension_ =
        java.util.Collections.emptyList();
      private void ensureExtensionIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          extension_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.FieldDescriptorProto>(extension_);
          bitField0_ |= 0x00000004;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.FieldDescriptorProto, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> extensionBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto> getExtensionList() {
        if (extensionBuilder_ == null) {
          return java.util.Collections.unmodifiableList(extension_);
        } else {
          return extensionBuilder_.getMessageList();
        }
      }

      public int getExtensionCount() {
        if (extensionBuilder_ == null) {
          return extension_.size();
        } else {
          return extensionBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto getExtension(int index) {
        if (extensionBuilder_ == null) {
          return extension_.get(index);
        } else {
          return extensionBuilder_.getMessage(index);
        }
      }

      public Builder setExtension(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto value) {
        if (extensionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtensionIsMutable();
          extension_.set(index, value);
          onChanged();
        } else {
          extensionBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setExtension(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder builderForValue) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          extension_.set(index, builderForValue.build());
          onChanged();
        } else {
          extensionBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addExtension(com.google.protobuf.DescriptorProtos.FieldDescriptorProto value) {
        if (extensionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtensionIsMutable();
          extension_.add(value);
          onChanged();
        } else {
          extensionBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addExtension(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto value) {
        if (extensionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtensionIsMutable();
          extension_.add(index, value);
          onChanged();
        } else {
          extensionBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addExtension(
          com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder builderForValue) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          extension_.add(builderForValue.build());
          onChanged();
        } else {
          extensionBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addExtension(
          int index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder builderForValue) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          extension_.add(index, builderForValue.build());
          onChanged();
        } else {
          extensionBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllExtension(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProto> values) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          super.addAll(values, extension_);
          onChanged();
        } else {
          extensionBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearExtension() {
        if (extensionBuilder_ == null) {
          extension_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
          onChanged();
        } else {
          extensionBuilder_.clear();
        }
        return this;
      }

      public Builder removeExtension(int index) {
        if (extensionBuilder_ == null) {
          ensureExtensionIsMutable();
          extension_.remove(index);
          onChanged();
        } else {
          extensionBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder getExtensionBuilder(
          int index) {
        return getExtensionFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder getExtensionOrBuilder(
          int index) {
        if (extensionBuilder_ == null) {
          return extension_.get(index);  } else {
          return extensionBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
           getExtensionOrBuilderList() {
        if (extensionBuilder_ != null) {
          return extensionBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(extension_);
        }
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder addExtensionBuilder() {
        return getExtensionFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder addExtensionBuilder(
          int index) {
        return getExtensionFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder> 
           getExtensionBuilderList() {
        return getExtensionFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.FieldDescriptorProto, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder> 
          getExtensionFieldBuilder() {
        if (extensionBuilder_ == null) {
          extensionBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.FieldDescriptorProto, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder>(
                  extension_,
                  ((bitField0_ & 0x00000004) == 0x00000004),
                  getParentForChildren(),
                  isClean());
          extension_ = null;
        }
        return extensionBuilder_;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> nestedType_ =
        java.util.Collections.emptyList();
      private void ensureNestedTypeIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          nestedType_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.DescriptorProto>(nestedType_);
          bitField0_ |= 0x00000008;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.DescriptorProto, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder, com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> nestedTypeBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto> getNestedTypeList() {
        if (nestedTypeBuilder_ == null) {
          return java.util.Collections.unmodifiableList(nestedType_);
        } else {
          return nestedTypeBuilder_.getMessageList();
        }
      }

      public int getNestedTypeCount() {
        if (nestedTypeBuilder_ == null) {
          return nestedType_.size();
        } else {
          return nestedTypeBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto getNestedType(int index) {
        if (nestedTypeBuilder_ == null) {
          return nestedType_.get(index);
        } else {
          return nestedTypeBuilder_.getMessage(index);
        }
      }

      public Builder setNestedType(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto value) {
        if (nestedTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureNestedTypeIsMutable();
          nestedType_.set(index, value);
          onChanged();
        } else {
          nestedTypeBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setNestedType(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder builderForValue) {
        if (nestedTypeBuilder_ == null) {
          ensureNestedTypeIsMutable();
          nestedType_.set(index, builderForValue.build());
          onChanged();
        } else {
          nestedTypeBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addNestedType(com.google.protobuf.DescriptorProtos.DescriptorProto value) {
        if (nestedTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureNestedTypeIsMutable();
          nestedType_.add(value);
          onChanged();
        } else {
          nestedTypeBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addNestedType(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto value) {
        if (nestedTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureNestedTypeIsMutable();
          nestedType_.add(index, value);
          onChanged();
        } else {
          nestedTypeBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addNestedType(
          com.google.protobuf.DescriptorProtos.DescriptorProto.Builder builderForValue) {
        if (nestedTypeBuilder_ == null) {
          ensureNestedTypeIsMutable();
          nestedType_.add(builderForValue.build());
          onChanged();
        } else {
          nestedTypeBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addNestedType(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder builderForValue) {
        if (nestedTypeBuilder_ == null) {
          ensureNestedTypeIsMutable();
          nestedType_.add(index, builderForValue.build());
          onChanged();
        } else {
          nestedTypeBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllNestedType(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.DescriptorProto> values) {
        if (nestedTypeBuilder_ == null) {
          ensureNestedTypeIsMutable();
          super.addAll(values, nestedType_);
          onChanged();
        } else {
          nestedTypeBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearNestedType() {
        if (nestedTypeBuilder_ == null) {
          nestedType_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
          onChanged();
        } else {
          nestedTypeBuilder_.clear();
        }
        return this;
      }

      public Builder removeNestedType(int index) {
        if (nestedTypeBuilder_ == null) {
          ensureNestedTypeIsMutable();
          nestedType_.remove(index);
          onChanged();
        } else {
          nestedTypeBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.Builder getNestedTypeBuilder(
          int index) {
        return getNestedTypeFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder getNestedTypeOrBuilder(
          int index) {
        if (nestedTypeBuilder_ == null) {
          return nestedType_.get(index);  } else {
          return nestedTypeBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> 
           getNestedTypeOrBuilderList() {
        if (nestedTypeBuilder_ != null) {
          return nestedTypeBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(nestedType_);
        }
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.Builder addNestedTypeBuilder() {
        return getNestedTypeFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.DescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.Builder addNestedTypeBuilder(
          int index) {
        return getNestedTypeFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.DescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto.Builder> 
           getNestedTypeBuilderList() {
        return getNestedTypeFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.DescriptorProto, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder, com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder> 
          getNestedTypeFieldBuilder() {
        if (nestedTypeBuilder_ == null) {
          nestedTypeBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.DescriptorProto, com.google.protobuf.DescriptorProtos.DescriptorProto.Builder, com.google.protobuf.DescriptorProtos.DescriptorProtoOrBuilder>(
                  nestedType_,
                  ((bitField0_ & 0x00000008) == 0x00000008),
                  getParentForChildren(),
                  isClean());
          nestedType_ = null;
        }
        return nestedTypeBuilder_;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> enumType_ =
        java.util.Collections.emptyList();
      private void ensureEnumTypeIsMutable() {
        if (!((bitField0_ & 0x00000010) == 0x00000010)) {
          enumType_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.EnumDescriptorProto>(enumType_);
          bitField0_ |= 0x00000010;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumDescriptorProto, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> enumTypeBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto> getEnumTypeList() {
        if (enumTypeBuilder_ == null) {
          return java.util.Collections.unmodifiableList(enumType_);
        } else {
          return enumTypeBuilder_.getMessageList();
        }
      }

      public int getEnumTypeCount() {
        if (enumTypeBuilder_ == null) {
          return enumType_.size();
        } else {
          return enumTypeBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto getEnumType(int index) {
        if (enumTypeBuilder_ == null) {
          return enumType_.get(index);
        } else {
          return enumTypeBuilder_.getMessage(index);
        }
      }

      public Builder setEnumType(
          int index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto value) {
        if (enumTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureEnumTypeIsMutable();
          enumType_.set(index, value);
          onChanged();
        } else {
          enumTypeBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setEnumType(
          int index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder builderForValue) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          enumType_.set(index, builderForValue.build());
          onChanged();
        } else {
          enumTypeBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addEnumType(com.google.protobuf.DescriptorProtos.EnumDescriptorProto value) {
        if (enumTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureEnumTypeIsMutable();
          enumType_.add(value);
          onChanged();
        } else {
          enumTypeBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addEnumType(
          int index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto value) {
        if (enumTypeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureEnumTypeIsMutable();
          enumType_.add(index, value);
          onChanged();
        } else {
          enumTypeBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addEnumType(
          com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder builderForValue) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          enumType_.add(builderForValue.build());
          onChanged();
        } else {
          enumTypeBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addEnumType(
          int index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder builderForValue) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          enumType_.add(index, builderForValue.build());
          onChanged();
        } else {
          enumTypeBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllEnumType(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.EnumDescriptorProto> values) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          super.addAll(values, enumType_);
          onChanged();
        } else {
          enumTypeBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearEnumType() {
        if (enumTypeBuilder_ == null) {
          enumType_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000010);
          onChanged();
        } else {
          enumTypeBuilder_.clear();
        }
        return this;
      }

      public Builder removeEnumType(int index) {
        if (enumTypeBuilder_ == null) {
          ensureEnumTypeIsMutable();
          enumType_.remove(index);
          onChanged();
        } else {
          enumTypeBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder getEnumTypeBuilder(
          int index) {
        return getEnumTypeFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder getEnumTypeOrBuilder(
          int index) {
        if (enumTypeBuilder_ == null) {
          return enumType_.get(index);  } else {
          return enumTypeBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> 
           getEnumTypeOrBuilderList() {
        if (enumTypeBuilder_ != null) {
          return enumTypeBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(enumType_);
        }
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder addEnumTypeBuilder() {
        return getEnumTypeFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.EnumDescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder addEnumTypeBuilder(
          int index) {
        return getEnumTypeFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder> 
           getEnumTypeBuilderList() {
        return getEnumTypeFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumDescriptorProto, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder> 
          getEnumTypeFieldBuilder() {
        if (enumTypeBuilder_ == null) {
          enumTypeBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.EnumDescriptorProto, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder>(
                  enumType_,
                  ((bitField0_ & 0x00000010) == 0x00000010),
                  getParentForChildren(),
                  isClean());
          enumType_ = null;
        }
        return enumTypeBuilder_;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange> extensionRange_ =
        java.util.Collections.emptyList();
      private void ensureExtensionRangeIsMutable() {
        if (!((bitField0_ & 0x00000020) == 0x00000020)) {
          extensionRange_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange>(extensionRange_);
          bitField0_ |= 0x00000020;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder> extensionRangeBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange> getExtensionRangeList() {
        if (extensionRangeBuilder_ == null) {
          return java.util.Collections.unmodifiableList(extensionRange_);
        } else {
          return extensionRangeBuilder_.getMessageList();
        }
      }

      public int getExtensionRangeCount() {
        if (extensionRangeBuilder_ == null) {
          return extensionRange_.size();
        } else {
          return extensionRangeBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange getExtensionRange(int index) {
        if (extensionRangeBuilder_ == null) {
          return extensionRange_.get(index);
        } else {
          return extensionRangeBuilder_.getMessage(index);
        }
      }

      public Builder setExtensionRange(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange value) {
        if (extensionRangeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtensionRangeIsMutable();
          extensionRange_.set(index, value);
          onChanged();
        } else {
          extensionRangeBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setExtensionRange(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder builderForValue) {
        if (extensionRangeBuilder_ == null) {
          ensureExtensionRangeIsMutable();
          extensionRange_.set(index, builderForValue.build());
          onChanged();
        } else {
          extensionRangeBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addExtensionRange(com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange value) {
        if (extensionRangeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtensionRangeIsMutable();
          extensionRange_.add(value);
          onChanged();
        } else {
          extensionRangeBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addExtensionRange(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange value) {
        if (extensionRangeBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureExtensionRangeIsMutable();
          extensionRange_.add(index, value);
          onChanged();
        } else {
          extensionRangeBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addExtensionRange(
          com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder builderForValue) {
        if (extensionRangeBuilder_ == null) {
          ensureExtensionRangeIsMutable();
          extensionRange_.add(builderForValue.build());
          onChanged();
        } else {
          extensionRangeBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addExtensionRange(
          int index, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder builderForValue) {
        if (extensionRangeBuilder_ == null) {
          ensureExtensionRangeIsMutable();
          extensionRange_.add(index, builderForValue.build());
          onChanged();
        } else {
          extensionRangeBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllExtensionRange(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange> values) {
        if (extensionRangeBuilder_ == null) {
          ensureExtensionRangeIsMutable();
          super.addAll(values, extensionRange_);
          onChanged();
        } else {
          extensionRangeBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearExtensionRange() {
        if (extensionRangeBuilder_ == null) {
          extensionRange_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000020);
          onChanged();
        } else {
          extensionRangeBuilder_.clear();
        }
        return this;
      }

      public Builder removeExtensionRange(int index) {
        if (extensionRangeBuilder_ == null) {
          ensureExtensionRangeIsMutable();
          extensionRange_.remove(index);
          onChanged();
        } else {
          extensionRangeBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder getExtensionRangeBuilder(
          int index) {
        return getExtensionRangeFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder getExtensionRangeOrBuilder(
          int index) {
        if (extensionRangeBuilder_ == null) {
          return extensionRange_.get(index);  } else {
          return extensionRangeBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder> 
           getExtensionRangeOrBuilderList() {
        if (extensionRangeBuilder_ != null) {
          return extensionRangeBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(extensionRange_);
        }
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder addExtensionRangeBuilder() {
        return getExtensionRangeFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder addExtensionRangeBuilder(
          int index) {
        return getExtensionRangeFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder> 
           getExtensionRangeBuilderList() {
        return getExtensionRangeFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder> 
          getExtensionRangeFieldBuilder() {
        if (extensionRangeBuilder_ == null) {
          extensionRangeBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRange.Builder, com.google.protobuf.DescriptorProtos.DescriptorProto.ExtensionRangeOrBuilder>(
                  extensionRange_,
                  ((bitField0_ & 0x00000020) == 0x00000020),
                  getParentForChildren(),
                  isClean());
          extensionRange_ = null;
        }
        return extensionRangeBuilder_;
      }

      private com.google.protobuf.DescriptorProtos.MessageOptions options_ = com.google.protobuf.DescriptorProtos.MessageOptions.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.MessageOptions, com.google.protobuf.DescriptorProtos.MessageOptions.Builder, com.google.protobuf.DescriptorProtos.MessageOptionsOrBuilder> optionsBuilder_;

      public boolean hasOptions() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }

      public com.google.protobuf.DescriptorProtos.MessageOptions getOptions() {
        if (optionsBuilder_ == null) {
          return options_;
        } else {
          return optionsBuilder_.getMessage();
        }
      }

      public Builder setOptions(com.google.protobuf.DescriptorProtos.MessageOptions value) {
        if (optionsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          options_ = value;
          onChanged();
        } else {
          optionsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000040;
        return this;
      }

      public Builder setOptions(
          com.google.protobuf.DescriptorProtos.MessageOptions.Builder builderForValue) {
        if (optionsBuilder_ == null) {
          options_ = builderForValue.build();
          onChanged();
        } else {
          optionsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000040;
        return this;
      }

      public Builder mergeOptions(com.google.protobuf.DescriptorProtos.MessageOptions value) {
        if (optionsBuilder_ == null) {
          if (((bitField0_ & 0x00000040) == 0x00000040) &&
              options_ != com.google.protobuf.DescriptorProtos.MessageOptions.getDefaultInstance()) {
            options_ =
              com.google.protobuf.DescriptorProtos.MessageOptions.newBuilder(options_).mergeFrom(value).buildPartial();
          } else {
            options_ = value;
          }
          onChanged();
        } else {
          optionsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000040;
        return this;
      }

      public Builder clearOptions() {
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.MessageOptions.getDefaultInstance();
          onChanged();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }

      public com.google.protobuf.DescriptorProtos.MessageOptions.Builder getOptionsBuilder() {
        bitField0_ |= 0x00000040;
        onChanged();
        return getOptionsFieldBuilder().getBuilder();
      }

      public com.google.protobuf.DescriptorProtos.MessageOptionsOrBuilder getOptionsOrBuilder() {
        if (optionsBuilder_ != null) {
          return optionsBuilder_.getMessageOrBuilder();
        } else {
          return options_;
        }
      }

      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.MessageOptions, com.google.protobuf.DescriptorProtos.MessageOptions.Builder, com.google.protobuf.DescriptorProtos.MessageOptionsOrBuilder> 
          getOptionsFieldBuilder() {
        if (optionsBuilder_ == null) {
          optionsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.google.protobuf.DescriptorProtos.MessageOptions, com.google.protobuf.DescriptorProtos.MessageOptions.Builder, com.google.protobuf.DescriptorProtos.MessageOptionsOrBuilder>(
                  options_,
                  getParentForChildren(),
                  isClean());
          options_ = null;
        }
        return optionsBuilder_;
      }

    }

    static {
      defaultInstance = new DescriptorProto(true);
      defaultInstance.initFields();
    }

  }

  public interface FieldDescriptorProtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    boolean hasName();

    java.lang.String getName();

    com.google.protobuf.ByteString
        getNameBytes();

    boolean hasNumber();

    int getNumber();

    boolean hasLabel();

    com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label getLabel();

    boolean hasType();

    com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type getType();

    boolean hasTypeName();

    java.lang.String getTypeName();

    com.google.protobuf.ByteString
        getTypeNameBytes();

    boolean hasExtendee();

    java.lang.String getExtendee();

    com.google.protobuf.ByteString
        getExtendeeBytes();

    boolean hasDefaultValue();

    java.lang.String getDefaultValue();

    com.google.protobuf.ByteString
        getDefaultValueBytes();

    boolean hasOptions();

    com.google.protobuf.DescriptorProtos.FieldOptions getOptions();

    com.google.protobuf.DescriptorProtos.FieldOptionsOrBuilder getOptionsOrBuilder();
  }

  public static final class FieldDescriptorProto extends
      com.google.protobuf.GeneratedMessage
      implements FieldDescriptorProtoOrBuilder {

    private FieldDescriptorProto(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private FieldDescriptorProto(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final FieldDescriptorProto defaultInstance;
    public static FieldDescriptorProto getDefaultInstance() {
      return defaultInstance;
    }

    public FieldDescriptorProto getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private FieldDescriptorProto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              name_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000020;
              extendee_ = input.readBytes();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000002;
              number_ = input.readInt32();
              break;
            }
            case 32: {
              int rawValue = input.readEnum();
              com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label value = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(4, rawValue);
              } else {
                bitField0_ |= 0x00000004;
                label_ = value;
              }
              break;
            }
            case 40: {
              int rawValue = input.readEnum();
              com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type value = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(5, rawValue);
              } else {
                bitField0_ |= 0x00000008;
                type_ = value;
              }
              break;
            }
            case 50: {
              bitField0_ |= 0x00000010;
              typeName_ = input.readBytes();
              break;
            }
            case 58: {
              bitField0_ |= 0x00000040;
              defaultValue_ = input.readBytes();
              break;
            }
            case 66: {
              com.google.protobuf.DescriptorProtos.FieldOptions.Builder subBuilder = null;
              if (((bitField0_ & 0x00000080) == 0x00000080)) {
                subBuilder = options_.toBuilder();
              }
              options_ = input.readMessage(com.google.protobuf.DescriptorProtos.FieldOptions.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(options_);
                options_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000080;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.FieldDescriptorProto.class, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder.class);
    }

    public static com.google.protobuf.Parser<FieldDescriptorProto> PARSER =
        new com.google.protobuf.AbstractParser<FieldDescriptorProto>() {
      public FieldDescriptorProto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new FieldDescriptorProto(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<FieldDescriptorProto> getParserForType() {
      return PARSER;
    }

    public enum Type
        implements com.google.protobuf.ProtocolMessageEnum {

      TYPE_DOUBLE(0, 1),

      TYPE_FLOAT(1, 2),

      TYPE_INT64(2, 3),

      TYPE_UINT64(3, 4),

      TYPE_INT32(4, 5),

      TYPE_FIXED64(5, 6),

      TYPE_FIXED32(6, 7),

      TYPE_BOOL(7, 8),

      TYPE_STRING(8, 9),

      TYPE_GROUP(9, 10),

      TYPE_MESSAGE(10, 11),

      TYPE_BYTES(11, 12),

      TYPE_UINT32(12, 13),

      TYPE_ENUM(13, 14),

      TYPE_SFIXED32(14, 15),

      TYPE_SFIXED64(15, 16),

      TYPE_SINT32(16, 17),

      TYPE_SINT64(17, 18),
      ;

      public static final int TYPE_DOUBLE_VALUE = 1;

      public static final int TYPE_FLOAT_VALUE = 2;

      public static final int TYPE_INT64_VALUE = 3;

      public static final int TYPE_UINT64_VALUE = 4;

      public static final int TYPE_INT32_VALUE = 5;

      public static final int TYPE_FIXED64_VALUE = 6;

      public static final int TYPE_FIXED32_VALUE = 7;

      public static final int TYPE_BOOL_VALUE = 8;

      public static final int TYPE_STRING_VALUE = 9;

      public static final int TYPE_GROUP_VALUE = 10;

      public static final int TYPE_MESSAGE_VALUE = 11;

      public static final int TYPE_BYTES_VALUE = 12;

      public static final int TYPE_UINT32_VALUE = 13;

      public static final int TYPE_ENUM_VALUE = 14;

      public static final int TYPE_SFIXED32_VALUE = 15;

      public static final int TYPE_SFIXED64_VALUE = 16;

      public static final int TYPE_SINT32_VALUE = 17;

      public static final int TYPE_SINT64_VALUE = 18;

      public final int getNumber() { return value; }

      public static Type valueOf(int value) {
        switch (value) {
          case 1: return TYPE_DOUBLE;
          case 2: return TYPE_FLOAT;
          case 3: return TYPE_INT64;
          case 4: return TYPE_UINT64;
          case 5: return TYPE_INT32;
          case 6: return TYPE_FIXED64;
          case 7: return TYPE_FIXED32;
          case 8: return TYPE_BOOL;
          case 9: return TYPE_STRING;
          case 10: return TYPE_GROUP;
          case 11: return TYPE_MESSAGE;
          case 12: return TYPE_BYTES;
          case 13: return TYPE_UINT32;
          case 14: return TYPE_ENUM;
          case 15: return TYPE_SFIXED32;
          case 16: return TYPE_SFIXED64;
          case 17: return TYPE_SINT32;
          case 18: return TYPE_SINT64;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<Type>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<Type>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<Type>() {
              public Type findValueByNumber(int number) {
                return Type.valueOf(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDescriptor().getEnumTypes().get(0);
      }

      private static final Type[] VALUES = values();

      public static Type valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private Type(int index, int value) {
        this.index = index;
        this.value = value;
      }

    }

    public enum Label
        implements com.google.protobuf.ProtocolMessageEnum {

      LABEL_OPTIONAL(0, 1),

      LABEL_REQUIRED(1, 2),

      LABEL_REPEATED(2, 3),
      ;

      public static final int LABEL_OPTIONAL_VALUE = 1;

      public static final int LABEL_REQUIRED_VALUE = 2;

      public static final int LABEL_REPEATED_VALUE = 3;

      public final int getNumber() { return value; }

      public static Label valueOf(int value) {
        switch (value) {
          case 1: return LABEL_OPTIONAL;
          case 2: return LABEL_REQUIRED;
          case 3: return LABEL_REPEATED;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<Label>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<Label>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<Label>() {
              public Label findValueByNumber(int number) {
                return Label.valueOf(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDescriptor().getEnumTypes().get(1);
      }

      private static final Label[] VALUES = values();

      public static Label valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private Label(int index, int value) {
        this.index = index;
        this.value = value;
      }

    }

    private int bitField0_;

    public static final int NAME_FIELD_NUMBER = 1;
    private java.lang.Object name_;

    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int NUMBER_FIELD_NUMBER = 3;
    private int number_;

    public boolean hasNumber() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public int getNumber() {
      return number_;
    }

    public static final int LABEL_FIELD_NUMBER = 4;
    private com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label label_;

    public boolean hasLabel() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }

    public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label getLabel() {
      return label_;
    }

    public static final int TYPE_FIELD_NUMBER = 5;
    private com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type type_;

    public boolean hasType() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }

    public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type getType() {
      return type_;
    }

    public static final int TYPE_NAME_FIELD_NUMBER = 6;
    private java.lang.Object typeName_;

    public boolean hasTypeName() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }

    public java.lang.String getTypeName() {
      java.lang.Object ref = typeName_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          typeName_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getTypeNameBytes() {
      java.lang.Object ref = typeName_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        typeName_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int EXTENDEE_FIELD_NUMBER = 2;
    private java.lang.Object extendee_;

    public boolean hasExtendee() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }

    public java.lang.String getExtendee() {
      java.lang.Object ref = extendee_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          extendee_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getExtendeeBytes() {
      java.lang.Object ref = extendee_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        extendee_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int DEFAULT_VALUE_FIELD_NUMBER = 7;
    private java.lang.Object defaultValue_;

    public boolean hasDefaultValue() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }

    public java.lang.String getDefaultValue() {
      java.lang.Object ref = defaultValue_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          defaultValue_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getDefaultValueBytes() {
      java.lang.Object ref = defaultValue_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        defaultValue_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int OPTIONS_FIELD_NUMBER = 8;
    private com.google.protobuf.DescriptorProtos.FieldOptions options_;

    public boolean hasOptions() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }

    public com.google.protobuf.DescriptorProtos.FieldOptions getOptions() {
      return options_;
    }

    public com.google.protobuf.DescriptorProtos.FieldOptionsOrBuilder getOptionsOrBuilder() {
      return options_;
    }

    private void initFields() {
      name_ = "";
      number_ = 0;
      label_ = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
      type_ = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
      typeName_ = "";
      extendee_ = "";
      defaultValue_ = "";
      options_ = com.google.protobuf.DescriptorProtos.FieldOptions.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (hasOptions()) {
        if (!getOptions().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(2, getExtendeeBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(3, number_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeEnum(4, label_.getNumber());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeEnum(5, type_.getNumber());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(6, getTypeNameBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(7, getDefaultValueBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeMessage(8, options_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getExtendeeBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, number_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(4, label_.getNumber());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(5, type_.getNumber());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, getTypeNameBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, getDefaultValueBytes());
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(8, options_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FieldDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.FieldDescriptorProto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.FieldDescriptorProtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.FieldDescriptorProto.class, com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getOptionsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        number_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        label_ = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
        bitField0_ = (bitField0_ & ~0x00000004);
        type_ = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
        bitField0_ = (bitField0_ & ~0x00000008);
        typeName_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        extendee_ = "";
        bitField0_ = (bitField0_ & ~0x00000020);
        defaultValue_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.FieldOptions.getDefaultInstance();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000080);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldDescriptorProto_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto build() {
        com.google.protobuf.DescriptorProtos.FieldDescriptorProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto buildPartial() {
        com.google.protobuf.DescriptorProtos.FieldDescriptorProto result = new com.google.protobuf.DescriptorProtos.FieldDescriptorProto(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.number_ = number_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.label_ = label_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.type_ = type_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.typeName_ = typeName_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.extendee_ = extendee_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.defaultValue_ = defaultValue_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        if (optionsBuilder_ == null) {
          result.options_ = options_;
        } else {
          result.options_ = optionsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.FieldDescriptorProto) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.FieldDescriptorProto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.FieldDescriptorProto other) {
        if (other == com.google.protobuf.DescriptorProtos.FieldDescriptorProto.getDefaultInstance()) return this;
        if (other.hasName()) {
          bitField0_ |= 0x00000001;
          name_ = other.name_;
          onChanged();
        }
        if (other.hasNumber()) {
          setNumber(other.getNumber());
        }
        if (other.hasLabel()) {
          setLabel(other.getLabel());
        }
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasTypeName()) {
          bitField0_ |= 0x00000010;
          typeName_ = other.typeName_;
          onChanged();
        }
        if (other.hasExtendee()) {
          bitField0_ |= 0x00000020;
          extendee_ = other.extendee_;
          onChanged();
        }
        if (other.hasDefaultValue()) {
          bitField0_ |= 0x00000040;
          defaultValue_ = other.defaultValue_;
          onChanged();
        }
        if (other.hasOptions()) {
          mergeOptions(other.getOptions());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (hasOptions()) {
          if (!getOptions().isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.FieldDescriptorProto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.FieldDescriptorProto) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object name_ = "";

      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }

      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      private int number_ ;

      public boolean hasNumber() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public int getNumber() {
        return number_;
      }

      public Builder setNumber(int value) {
        bitField0_ |= 0x00000002;
        number_ = value;
        onChanged();
        return this;
      }

      public Builder clearNumber() {
        bitField0_ = (bitField0_ & ~0x00000002);
        number_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label label_ = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;

      public boolean hasLabel() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label getLabel() {
        return label_;
      }

      public Builder setLabel(com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000004;
        label_ = value;
        onChanged();
        return this;
      }

      public Builder clearLabel() {
        bitField0_ = (bitField0_ & ~0x00000004);
        label_ = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label.LABEL_OPTIONAL;
        onChanged();
        return this;
      }

      private com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type type_ = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;

      public boolean hasType() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }

      public com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type getType() {
        return type_;
      }

      public Builder setType(com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000008;
        type_ = value;
        onChanged();
        return this;
      }

      public Builder clearType() {
        bitField0_ = (bitField0_ & ~0x00000008);
        type_ = com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Type.TYPE_DOUBLE;
        onChanged();
        return this;
      }

      private java.lang.Object typeName_ = "";

      public boolean hasTypeName() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }

      public java.lang.String getTypeName() {
        java.lang.Object ref = typeName_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          typeName_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getTypeNameBytes() {
        java.lang.Object ref = typeName_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          typeName_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setTypeName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        typeName_ = value;
        onChanged();
        return this;
      }

      public Builder clearTypeName() {
        bitField0_ = (bitField0_ & ~0x00000010);
        typeName_ = getDefaultInstance().getTypeName();
        onChanged();
        return this;
      }

      public Builder setTypeNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        typeName_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object extendee_ = "";

      public boolean hasExtendee() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }

      public java.lang.String getExtendee() {
        java.lang.Object ref = extendee_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          extendee_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getExtendeeBytes() {
        java.lang.Object ref = extendee_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          extendee_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setExtendee(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        extendee_ = value;
        onChanged();
        return this;
      }

      public Builder clearExtendee() {
        bitField0_ = (bitField0_ & ~0x00000020);
        extendee_ = getDefaultInstance().getExtendee();
        onChanged();
        return this;
      }

      public Builder setExtendeeBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        extendee_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object defaultValue_ = "";

      public boolean hasDefaultValue() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }

      public java.lang.String getDefaultValue() {
        java.lang.Object ref = defaultValue_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          defaultValue_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getDefaultValueBytes() {
        java.lang.Object ref = defaultValue_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          defaultValue_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setDefaultValue(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        defaultValue_ = value;
        onChanged();
        return this;
      }

      public Builder clearDefaultValue() {
        bitField0_ = (bitField0_ & ~0x00000040);
        defaultValue_ = getDefaultInstance().getDefaultValue();
        onChanged();
        return this;
      }

      public Builder setDefaultValueBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        defaultValue_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.DescriptorProtos.FieldOptions options_ = com.google.protobuf.DescriptorProtos.FieldOptions.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.FieldOptions, com.google.protobuf.DescriptorProtos.FieldOptions.Builder, com.google.protobuf.DescriptorProtos.FieldOptionsOrBuilder> optionsBuilder_;

      public boolean hasOptions() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }

      public com.google.protobuf.DescriptorProtos.FieldOptions getOptions() {
        if (optionsBuilder_ == null) {
          return options_;
        } else {
          return optionsBuilder_.getMessage();
        }
      }

      public Builder setOptions(com.google.protobuf.DescriptorProtos.FieldOptions value) {
        if (optionsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          options_ = value;
          onChanged();
        } else {
          optionsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000080;
        return this;
      }

      public Builder setOptions(
          com.google.protobuf.DescriptorProtos.FieldOptions.Builder builderForValue) {
        if (optionsBuilder_ == null) {
          options_ = builderForValue.build();
          onChanged();
        } else {
          optionsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000080;
        return this;
      }

      public Builder mergeOptions(com.google.protobuf.DescriptorProtos.FieldOptions value) {
        if (optionsBuilder_ == null) {
          if (((bitField0_ & 0x00000080) == 0x00000080) &&
              options_ != com.google.protobuf.DescriptorProtos.FieldOptions.getDefaultInstance()) {
            options_ =
              com.google.protobuf.DescriptorProtos.FieldOptions.newBuilder(options_).mergeFrom(value).buildPartial();
          } else {
            options_ = value;
          }
          onChanged();
        } else {
          optionsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000080;
        return this;
      }

      public Builder clearOptions() {
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.FieldOptions.getDefaultInstance();
          onChanged();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000080);
        return this;
      }

      public com.google.protobuf.DescriptorProtos.FieldOptions.Builder getOptionsBuilder() {
        bitField0_ |= 0x00000080;
        onChanged();
        return getOptionsFieldBuilder().getBuilder();
      }

      public com.google.protobuf.DescriptorProtos.FieldOptionsOrBuilder getOptionsOrBuilder() {
        if (optionsBuilder_ != null) {
          return optionsBuilder_.getMessageOrBuilder();
        } else {
          return options_;
        }
      }

      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.FieldOptions, com.google.protobuf.DescriptorProtos.FieldOptions.Builder, com.google.protobuf.DescriptorProtos.FieldOptionsOrBuilder> 
          getOptionsFieldBuilder() {
        if (optionsBuilder_ == null) {
          optionsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.google.protobuf.DescriptorProtos.FieldOptions, com.google.protobuf.DescriptorProtos.FieldOptions.Builder, com.google.protobuf.DescriptorProtos.FieldOptionsOrBuilder>(
                  options_,
                  getParentForChildren(),
                  isClean());
          options_ = null;
        }
        return optionsBuilder_;
      }

    }

    static {
      defaultInstance = new FieldDescriptorProto(true);
      defaultInstance.initFields();
    }

  }

  public interface EnumDescriptorProtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    boolean hasName();

    java.lang.String getName();

    com.google.protobuf.ByteString
        getNameBytes();

    java.util.List<com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto> 
        getValueList();

    com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto getValue(int index);

    int getValueCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder> 
        getValueOrBuilderList();

    com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder getValueOrBuilder(
        int index);

    boolean hasOptions();

    com.google.protobuf.DescriptorProtos.EnumOptions getOptions();

    com.google.protobuf.DescriptorProtos.EnumOptionsOrBuilder getOptionsOrBuilder();
  }

  public static final class EnumDescriptorProto extends
      com.google.protobuf.GeneratedMessage
      implements EnumDescriptorProtoOrBuilder {

    private EnumDescriptorProto(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private EnumDescriptorProto(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final EnumDescriptorProto defaultInstance;
    public static EnumDescriptorProto getDefaultInstance() {
      return defaultInstance;
    }

    public EnumDescriptorProto getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private EnumDescriptorProto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              name_ = input.readBytes();
              break;
            }
            case 18: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                value_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto>();
                mutable_bitField0_ |= 0x00000002;
              }
              value_.add(input.readMessage(com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 26: {
              com.google.protobuf.DescriptorProtos.EnumOptions.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = options_.toBuilder();
              }
              options_ = input.readMessage(com.google.protobuf.DescriptorProtos.EnumOptions.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(options_);
                options_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          value_ = java.util.Collections.unmodifiableList(value_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.EnumDescriptorProto.class, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder.class);
    }

    public static com.google.protobuf.Parser<EnumDescriptorProto> PARSER =
        new com.google.protobuf.AbstractParser<EnumDescriptorProto>() {
      public EnumDescriptorProto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new EnumDescriptorProto(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<EnumDescriptorProto> getParserForType() {
      return PARSER;
    }

    private int bitField0_;

    public static final int NAME_FIELD_NUMBER = 1;
    private java.lang.Object name_;

    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int VALUE_FIELD_NUMBER = 2;
    private java.util.List<com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto> value_;

    public java.util.List<com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto> getValueList() {
      return value_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder> 
        getValueOrBuilderList() {
      return value_;
    }

    public int getValueCount() {
      return value_.size();
    }

    public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto getValue(int index) {
      return value_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder getValueOrBuilder(
        int index) {
      return value_.get(index);
    }

    public static final int OPTIONS_FIELD_NUMBER = 3;
    private com.google.protobuf.DescriptorProtos.EnumOptions options_;

    public boolean hasOptions() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public com.google.protobuf.DescriptorProtos.EnumOptions getOptions() {
      return options_;
    }

    public com.google.protobuf.DescriptorProtos.EnumOptionsOrBuilder getOptionsOrBuilder() {
      return options_;
    }

    private void initFields() {
      name_ = "";
      value_ = java.util.Collections.emptyList();
      options_ = com.google.protobuf.DescriptorProtos.EnumOptions.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getValueCount(); i++) {
        if (!getValue(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (hasOptions()) {
        if (!getOptions().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getNameBytes());
      }
      for (int i = 0; i < value_.size(); i++) {
        output.writeMessage(2, value_.get(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(3, options_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getNameBytes());
      }
      for (int i = 0; i < value_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, value_.get(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, options_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.EnumDescriptorProto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.EnumDescriptorProtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.EnumDescriptorProto.class, com.google.protobuf.DescriptorProtos.EnumDescriptorProto.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getValueFieldBuilder();
          getOptionsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (valueBuilder_ == null) {
          value_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          valueBuilder_.clear();
        }
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.EnumOptions.getDefaultInstance();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumDescriptorProto_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.EnumDescriptorProto.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto build() {
        com.google.protobuf.DescriptorProtos.EnumDescriptorProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.EnumDescriptorProto buildPartial() {
        com.google.protobuf.DescriptorProtos.EnumDescriptorProto result = new com.google.protobuf.DescriptorProtos.EnumDescriptorProto(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (valueBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            value_ = java.util.Collections.unmodifiableList(value_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.value_ = value_;
        } else {
          result.value_ = valueBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000002;
        }
        if (optionsBuilder_ == null) {
          result.options_ = options_;
        } else {
          result.options_ = optionsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.EnumDescriptorProto) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.EnumDescriptorProto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.EnumDescriptorProto other) {
        if (other == com.google.protobuf.DescriptorProtos.EnumDescriptorProto.getDefaultInstance()) return this;
        if (other.hasName()) {
          bitField0_ |= 0x00000001;
          name_ = other.name_;
          onChanged();
        }
        if (valueBuilder_ == null) {
          if (!other.value_.isEmpty()) {
            if (value_.isEmpty()) {
              value_ = other.value_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureValueIsMutable();
              value_.addAll(other.value_);
            }
            onChanged();
          }
        } else {
          if (!other.value_.isEmpty()) {
            if (valueBuilder_.isEmpty()) {
              valueBuilder_.dispose();
              valueBuilder_ = null;
              value_ = other.value_;
              bitField0_ = (bitField0_ & ~0x00000002);
              valueBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getValueFieldBuilder() : null;
            } else {
              valueBuilder_.addAllMessages(other.value_);
            }
          }
        }
        if (other.hasOptions()) {
          mergeOptions(other.getOptions());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getValueCount(); i++) {
          if (!getValue(i).isInitialized()) {

            return false;
          }
        }
        if (hasOptions()) {
          if (!getOptions().isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.EnumDescriptorProto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.EnumDescriptorProto) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object name_ = "";

      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }

      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto> value_ =
        java.util.Collections.emptyList();
      private void ensureValueIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          value_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto>(value_);
          bitField0_ |= 0x00000002;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder> valueBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto> getValueList() {
        if (valueBuilder_ == null) {
          return java.util.Collections.unmodifiableList(value_);
        } else {
          return valueBuilder_.getMessageList();
        }
      }

      public int getValueCount() {
        if (valueBuilder_ == null) {
          return value_.size();
        } else {
          return valueBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto getValue(int index) {
        if (valueBuilder_ == null) {
          return value_.get(index);
        } else {
          return valueBuilder_.getMessage(index);
        }
      }

      public Builder setValue(
          int index, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto value) {
        if (valueBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureValueIsMutable();
          value_.set(index, value);
          onChanged();
        } else {
          valueBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setValue(
          int index, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder builderForValue) {
        if (valueBuilder_ == null) {
          ensureValueIsMutable();
          value_.set(index, builderForValue.build());
          onChanged();
        } else {
          valueBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addValue(com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto value) {
        if (valueBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureValueIsMutable();
          value_.add(value);
          onChanged();
        } else {
          valueBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addValue(
          int index, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto value) {
        if (valueBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureValueIsMutable();
          value_.add(index, value);
          onChanged();
        } else {
          valueBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addValue(
          com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder builderForValue) {
        if (valueBuilder_ == null) {
          ensureValueIsMutable();
          value_.add(builderForValue.build());
          onChanged();
        } else {
          valueBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addValue(
          int index, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder builderForValue) {
        if (valueBuilder_ == null) {
          ensureValueIsMutable();
          value_.add(index, builderForValue.build());
          onChanged();
        } else {
          valueBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllValue(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto> values) {
        if (valueBuilder_ == null) {
          ensureValueIsMutable();
          super.addAll(values, value_);
          onChanged();
        } else {
          valueBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearValue() {
        if (valueBuilder_ == null) {
          value_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          valueBuilder_.clear();
        }
        return this;
      }

      public Builder removeValue(int index) {
        if (valueBuilder_ == null) {
          ensureValueIsMutable();
          value_.remove(index);
          onChanged();
        } else {
          valueBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder getValueBuilder(
          int index) {
        return getValueFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder getValueOrBuilder(
          int index) {
        if (valueBuilder_ == null) {
          return value_.get(index);  } else {
          return valueBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder> 
           getValueOrBuilderList() {
        if (valueBuilder_ != null) {
          return valueBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(value_);
        }
      }

      public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder addValueBuilder() {
        return getValueFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder addValueBuilder(
          int index) {
        return getValueFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder> 
           getValueBuilderList() {
        return getValueFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder> 
          getValueFieldBuilder() {
        if (valueBuilder_ == null) {
          valueBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder>(
                  value_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          value_ = null;
        }
        return valueBuilder_;
      }

      private com.google.protobuf.DescriptorProtos.EnumOptions options_ = com.google.protobuf.DescriptorProtos.EnumOptions.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumOptions, com.google.protobuf.DescriptorProtos.EnumOptions.Builder, com.google.protobuf.DescriptorProtos.EnumOptionsOrBuilder> optionsBuilder_;

      public boolean hasOptions() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      public com.google.protobuf.DescriptorProtos.EnumOptions getOptions() {
        if (optionsBuilder_ == null) {
          return options_;
        } else {
          return optionsBuilder_.getMessage();
        }
      }

      public Builder setOptions(com.google.protobuf.DescriptorProtos.EnumOptions value) {
        if (optionsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          options_ = value;
          onChanged();
        } else {
          optionsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      public Builder setOptions(
          com.google.protobuf.DescriptorProtos.EnumOptions.Builder builderForValue) {
        if (optionsBuilder_ == null) {
          options_ = builderForValue.build();
          onChanged();
        } else {
          optionsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      public Builder mergeOptions(com.google.protobuf.DescriptorProtos.EnumOptions value) {
        if (optionsBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              options_ != com.google.protobuf.DescriptorProtos.EnumOptions.getDefaultInstance()) {
            options_ =
              com.google.protobuf.DescriptorProtos.EnumOptions.newBuilder(options_).mergeFrom(value).buildPartial();
          } else {
            options_ = value;
          }
          onChanged();
        } else {
          optionsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      public Builder clearOptions() {
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.EnumOptions.getDefaultInstance();
          onChanged();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public com.google.protobuf.DescriptorProtos.EnumOptions.Builder getOptionsBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getOptionsFieldBuilder().getBuilder();
      }

      public com.google.protobuf.DescriptorProtos.EnumOptionsOrBuilder getOptionsOrBuilder() {
        if (optionsBuilder_ != null) {
          return optionsBuilder_.getMessageOrBuilder();
        } else {
          return options_;
        }
      }

      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumOptions, com.google.protobuf.DescriptorProtos.EnumOptions.Builder, com.google.protobuf.DescriptorProtos.EnumOptionsOrBuilder> 
          getOptionsFieldBuilder() {
        if (optionsBuilder_ == null) {
          optionsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.google.protobuf.DescriptorProtos.EnumOptions, com.google.protobuf.DescriptorProtos.EnumOptions.Builder, com.google.protobuf.DescriptorProtos.EnumOptionsOrBuilder>(
                  options_,
                  getParentForChildren(),
                  isClean());
          options_ = null;
        }
        return optionsBuilder_;
      }

    }

    static {
      defaultInstance = new EnumDescriptorProto(true);
      defaultInstance.initFields();
    }

  }

  public interface EnumValueDescriptorProtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    boolean hasName();

    java.lang.String getName();

    com.google.protobuf.ByteString
        getNameBytes();

    boolean hasNumber();

    int getNumber();

    boolean hasOptions();

    com.google.protobuf.DescriptorProtos.EnumValueOptions getOptions();

    com.google.protobuf.DescriptorProtos.EnumValueOptionsOrBuilder getOptionsOrBuilder();
  }

  public static final class EnumValueDescriptorProto extends
      com.google.protobuf.GeneratedMessage
      implements EnumValueDescriptorProtoOrBuilder {

    private EnumValueDescriptorProto(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private EnumValueDescriptorProto(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final EnumValueDescriptorProto defaultInstance;
    public static EnumValueDescriptorProto getDefaultInstance() {
      return defaultInstance;
    }

    public EnumValueDescriptorProto getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private EnumValueDescriptorProto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              name_ = input.readBytes();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              number_ = input.readInt32();
              break;
            }
            case 26: {
              com.google.protobuf.DescriptorProtos.EnumValueOptions.Builder subBuilder = null;
              if (((bitField0_ & 0x00000004) == 0x00000004)) {
                subBuilder = options_.toBuilder();
              }
              options_ = input.readMessage(com.google.protobuf.DescriptorProtos.EnumValueOptions.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(options_);
                options_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000004;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.class, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder.class);
    }

    public static com.google.protobuf.Parser<EnumValueDescriptorProto> PARSER =
        new com.google.protobuf.AbstractParser<EnumValueDescriptorProto>() {
      public EnumValueDescriptorProto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new EnumValueDescriptorProto(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<EnumValueDescriptorProto> getParserForType() {
      return PARSER;
    }

    private int bitField0_;

    public static final int NAME_FIELD_NUMBER = 1;
    private java.lang.Object name_;

    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int NUMBER_FIELD_NUMBER = 2;
    private int number_;

    public boolean hasNumber() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public int getNumber() {
      return number_;
    }

    public static final int OPTIONS_FIELD_NUMBER = 3;
    private com.google.protobuf.DescriptorProtos.EnumValueOptions options_;

    public boolean hasOptions() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }

    public com.google.protobuf.DescriptorProtos.EnumValueOptions getOptions() {
      return options_;
    }

    public com.google.protobuf.DescriptorProtos.EnumValueOptionsOrBuilder getOptionsOrBuilder() {
      return options_;
    }

    private void initFields() {
      name_ = "";
      number_ = 0;
      options_ = com.google.protobuf.DescriptorProtos.EnumValueOptions.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (hasOptions()) {
        if (!getOptions().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, number_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, options_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, number_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, options_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.EnumValueDescriptorProtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.class, com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getOptionsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        number_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.EnumValueOptions.getDefaultInstance();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto build() {
        com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto buildPartial() {
        com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto result = new com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.number_ = number_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (optionsBuilder_ == null) {
          result.options_ = options_;
        } else {
          result.options_ = optionsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto other) {
        if (other == com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto.getDefaultInstance()) return this;
        if (other.hasName()) {
          bitField0_ |= 0x00000001;
          name_ = other.name_;
          onChanged();
        }
        if (other.hasNumber()) {
          setNumber(other.getNumber());
        }
        if (other.hasOptions()) {
          mergeOptions(other.getOptions());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (hasOptions()) {
          if (!getOptions().isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.EnumValueDescriptorProto) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object name_ = "";

      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }

      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      private int number_ ;

      public boolean hasNumber() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public int getNumber() {
        return number_;
      }

      public Builder setNumber(int value) {
        bitField0_ |= 0x00000002;
        number_ = value;
        onChanged();
        return this;
      }

      public Builder clearNumber() {
        bitField0_ = (bitField0_ & ~0x00000002);
        number_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.DescriptorProtos.EnumValueOptions options_ = com.google.protobuf.DescriptorProtos.EnumValueOptions.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumValueOptions, com.google.protobuf.DescriptorProtos.EnumValueOptions.Builder, com.google.protobuf.DescriptorProtos.EnumValueOptionsOrBuilder> optionsBuilder_;

      public boolean hasOptions() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      public com.google.protobuf.DescriptorProtos.EnumValueOptions getOptions() {
        if (optionsBuilder_ == null) {
          return options_;
        } else {
          return optionsBuilder_.getMessage();
        }
      }

      public Builder setOptions(com.google.protobuf.DescriptorProtos.EnumValueOptions value) {
        if (optionsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          options_ = value;
          onChanged();
        } else {
          optionsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      public Builder setOptions(
          com.google.protobuf.DescriptorProtos.EnumValueOptions.Builder builderForValue) {
        if (optionsBuilder_ == null) {
          options_ = builderForValue.build();
          onChanged();
        } else {
          optionsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      public Builder mergeOptions(com.google.protobuf.DescriptorProtos.EnumValueOptions value) {
        if (optionsBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              options_ != com.google.protobuf.DescriptorProtos.EnumValueOptions.getDefaultInstance()) {
            options_ =
              com.google.protobuf.DescriptorProtos.EnumValueOptions.newBuilder(options_).mergeFrom(value).buildPartial();
          } else {
            options_ = value;
          }
          onChanged();
        } else {
          optionsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      public Builder clearOptions() {
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.EnumValueOptions.getDefaultInstance();
          onChanged();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public com.google.protobuf.DescriptorProtos.EnumValueOptions.Builder getOptionsBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getOptionsFieldBuilder().getBuilder();
      }

      public com.google.protobuf.DescriptorProtos.EnumValueOptionsOrBuilder getOptionsOrBuilder() {
        if (optionsBuilder_ != null) {
          return optionsBuilder_.getMessageOrBuilder();
        } else {
          return options_;
        }
      }

      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.EnumValueOptions, com.google.protobuf.DescriptorProtos.EnumValueOptions.Builder, com.google.protobuf.DescriptorProtos.EnumValueOptionsOrBuilder> 
          getOptionsFieldBuilder() {
        if (optionsBuilder_ == null) {
          optionsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.google.protobuf.DescriptorProtos.EnumValueOptions, com.google.protobuf.DescriptorProtos.EnumValueOptions.Builder, com.google.protobuf.DescriptorProtos.EnumValueOptionsOrBuilder>(
                  options_,
                  getParentForChildren(),
                  isClean());
          options_ = null;
        }
        return optionsBuilder_;
      }

    }

    static {
      defaultInstance = new EnumValueDescriptorProto(true);
      defaultInstance.initFields();
    }

  }

  public interface ServiceDescriptorProtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    boolean hasName();

    java.lang.String getName();

    com.google.protobuf.ByteString
        getNameBytes();

    java.util.List<com.google.protobuf.DescriptorProtos.MethodDescriptorProto> 
        getMethodList();

    com.google.protobuf.DescriptorProtos.MethodDescriptorProto getMethod(int index);

    int getMethodCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder> 
        getMethodOrBuilderList();

    com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder getMethodOrBuilder(
        int index);

    boolean hasOptions();

    com.google.protobuf.DescriptorProtos.ServiceOptions getOptions();

    com.google.protobuf.DescriptorProtos.ServiceOptionsOrBuilder getOptionsOrBuilder();
  }

  public static final class ServiceDescriptorProto extends
      com.google.protobuf.GeneratedMessage
      implements ServiceDescriptorProtoOrBuilder {

    private ServiceDescriptorProto(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private ServiceDescriptorProto(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final ServiceDescriptorProto defaultInstance;
    public static ServiceDescriptorProto getDefaultInstance() {
      return defaultInstance;
    }

    public ServiceDescriptorProto getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private ServiceDescriptorProto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              name_ = input.readBytes();
              break;
            }
            case 18: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                method_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.MethodDescriptorProto>();
                mutable_bitField0_ |= 0x00000002;
              }
              method_.add(input.readMessage(com.google.protobuf.DescriptorProtos.MethodDescriptorProto.PARSER, extensionRegistry));
              break;
            }
            case 26: {
              com.google.protobuf.DescriptorProtos.ServiceOptions.Builder subBuilder = null;
              if (((bitField0_ & 0x00000002) == 0x00000002)) {
                subBuilder = options_.toBuilder();
              }
              options_ = input.readMessage(com.google.protobuf.DescriptorProtos.ServiceOptions.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(options_);
                options_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000002;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          method_ = java.util.Collections.unmodifiableList(method_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.class, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder.class);
    }

    public static com.google.protobuf.Parser<ServiceDescriptorProto> PARSER =
        new com.google.protobuf.AbstractParser<ServiceDescriptorProto>() {
      public ServiceDescriptorProto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ServiceDescriptorProto(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<ServiceDescriptorProto> getParserForType() {
      return PARSER;
    }

    private int bitField0_;

    public static final int NAME_FIELD_NUMBER = 1;
    private java.lang.Object name_;

    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int METHOD_FIELD_NUMBER = 2;
    private java.util.List<com.google.protobuf.DescriptorProtos.MethodDescriptorProto> method_;

    public java.util.List<com.google.protobuf.DescriptorProtos.MethodDescriptorProto> getMethodList() {
      return method_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder> 
        getMethodOrBuilderList() {
      return method_;
    }

    public int getMethodCount() {
      return method_.size();
    }

    public com.google.protobuf.DescriptorProtos.MethodDescriptorProto getMethod(int index) {
      return method_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder getMethodOrBuilder(
        int index) {
      return method_.get(index);
    }

    public static final int OPTIONS_FIELD_NUMBER = 3;
    private com.google.protobuf.DescriptorProtos.ServiceOptions options_;

    public boolean hasOptions() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public com.google.protobuf.DescriptorProtos.ServiceOptions getOptions() {
      return options_;
    }

    public com.google.protobuf.DescriptorProtos.ServiceOptionsOrBuilder getOptionsOrBuilder() {
      return options_;
    }

    private void initFields() {
      name_ = "";
      method_ = java.util.Collections.emptyList();
      options_ = com.google.protobuf.DescriptorProtos.ServiceOptions.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getMethodCount(); i++) {
        if (!getMethod(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (hasOptions()) {
        if (!getOptions().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getNameBytes());
      }
      for (int i = 0; i < method_.size(); i++) {
        output.writeMessage(2, method_.get(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(3, options_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getNameBytes());
      }
      for (int i = 0; i < method_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, method_.get(i));
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, options_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.ServiceDescriptorProto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.ServiceDescriptorProtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.class, com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getMethodFieldBuilder();
          getOptionsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        if (methodBuilder_ == null) {
          method_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          methodBuilder_.clear();
        }
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.ServiceOptions.getDefaultInstance();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.ServiceDescriptorProto getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.ServiceDescriptorProto build() {
        com.google.protobuf.DescriptorProtos.ServiceDescriptorProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.ServiceDescriptorProto buildPartial() {
        com.google.protobuf.DescriptorProtos.ServiceDescriptorProto result = new com.google.protobuf.DescriptorProtos.ServiceDescriptorProto(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (methodBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            method_ = java.util.Collections.unmodifiableList(method_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.method_ = method_;
        } else {
          result.method_ = methodBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000002;
        }
        if (optionsBuilder_ == null) {
          result.options_ = options_;
        } else {
          result.options_ = optionsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.ServiceDescriptorProto) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.ServiceDescriptorProto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.ServiceDescriptorProto other) {
        if (other == com.google.protobuf.DescriptorProtos.ServiceDescriptorProto.getDefaultInstance()) return this;
        if (other.hasName()) {
          bitField0_ |= 0x00000001;
          name_ = other.name_;
          onChanged();
        }
        if (methodBuilder_ == null) {
          if (!other.method_.isEmpty()) {
            if (method_.isEmpty()) {
              method_ = other.method_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureMethodIsMutable();
              method_.addAll(other.method_);
            }
            onChanged();
          }
        } else {
          if (!other.method_.isEmpty()) {
            if (methodBuilder_.isEmpty()) {
              methodBuilder_.dispose();
              methodBuilder_ = null;
              method_ = other.method_;
              bitField0_ = (bitField0_ & ~0x00000002);
              methodBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getMethodFieldBuilder() : null;
            } else {
              methodBuilder_.addAllMessages(other.method_);
            }
          }
        }
        if (other.hasOptions()) {
          mergeOptions(other.getOptions());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getMethodCount(); i++) {
          if (!getMethod(i).isInitialized()) {

            return false;
          }
        }
        if (hasOptions()) {
          if (!getOptions().isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.ServiceDescriptorProto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.ServiceDescriptorProto) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object name_ = "";

      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }

      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.MethodDescriptorProto> method_ =
        java.util.Collections.emptyList();
      private void ensureMethodIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          method_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.MethodDescriptorProto>(method_);
          bitField0_ |= 0x00000002;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.MethodDescriptorProto, com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder> methodBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.MethodDescriptorProto> getMethodList() {
        if (methodBuilder_ == null) {
          return java.util.Collections.unmodifiableList(method_);
        } else {
          return methodBuilder_.getMessageList();
        }
      }

      public int getMethodCount() {
        if (methodBuilder_ == null) {
          return method_.size();
        } else {
          return methodBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.MethodDescriptorProto getMethod(int index) {
        if (methodBuilder_ == null) {
          return method_.get(index);
        } else {
          return methodBuilder_.getMessage(index);
        }
      }

      public Builder setMethod(
          int index, com.google.protobuf.DescriptorProtos.MethodDescriptorProto value) {
        if (methodBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureMethodIsMutable();
          method_.set(index, value);
          onChanged();
        } else {
          methodBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setMethod(
          int index, com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder builderForValue) {
        if (methodBuilder_ == null) {
          ensureMethodIsMutable();
          method_.set(index, builderForValue.build());
          onChanged();
        } else {
          methodBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addMethod(com.google.protobuf.DescriptorProtos.MethodDescriptorProto value) {
        if (methodBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureMethodIsMutable();
          method_.add(value);
          onChanged();
        } else {
          methodBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addMethod(
          int index, com.google.protobuf.DescriptorProtos.MethodDescriptorProto value) {
        if (methodBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureMethodIsMutable();
          method_.add(index, value);
          onChanged();
        } else {
          methodBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addMethod(
          com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder builderForValue) {
        if (methodBuilder_ == null) {
          ensureMethodIsMutable();
          method_.add(builderForValue.build());
          onChanged();
        } else {
          methodBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addMethod(
          int index, com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder builderForValue) {
        if (methodBuilder_ == null) {
          ensureMethodIsMutable();
          method_.add(index, builderForValue.build());
          onChanged();
        } else {
          methodBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllMethod(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.MethodDescriptorProto> values) {
        if (methodBuilder_ == null) {
          ensureMethodIsMutable();
          super.addAll(values, method_);
          onChanged();
        } else {
          methodBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearMethod() {
        if (methodBuilder_ == null) {
          method_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          methodBuilder_.clear();
        }
        return this;
      }

      public Builder removeMethod(int index) {
        if (methodBuilder_ == null) {
          ensureMethodIsMutable();
          method_.remove(index);
          onChanged();
        } else {
          methodBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder getMethodBuilder(
          int index) {
        return getMethodFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder getMethodOrBuilder(
          int index) {
        if (methodBuilder_ == null) {
          return method_.get(index);  } else {
          return methodBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder> 
           getMethodOrBuilderList() {
        if (methodBuilder_ != null) {
          return methodBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(method_);
        }
      }

      public com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder addMethodBuilder() {
        return getMethodFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.MethodDescriptorProto.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder addMethodBuilder(
          int index) {
        return getMethodFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.MethodDescriptorProto.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder> 
           getMethodBuilderList() {
        return getMethodFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.MethodDescriptorProto, com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder> 
          getMethodFieldBuilder() {
        if (methodBuilder_ == null) {
          methodBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.MethodDescriptorProto, com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder, com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder>(
                  method_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          method_ = null;
        }
        return methodBuilder_;
      }

      private com.google.protobuf.DescriptorProtos.ServiceOptions options_ = com.google.protobuf.DescriptorProtos.ServiceOptions.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.ServiceOptions, com.google.protobuf.DescriptorProtos.ServiceOptions.Builder, com.google.protobuf.DescriptorProtos.ServiceOptionsOrBuilder> optionsBuilder_;

      public boolean hasOptions() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      public com.google.protobuf.DescriptorProtos.ServiceOptions getOptions() {
        if (optionsBuilder_ == null) {
          return options_;
        } else {
          return optionsBuilder_.getMessage();
        }
      }

      public Builder setOptions(com.google.protobuf.DescriptorProtos.ServiceOptions value) {
        if (optionsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          options_ = value;
          onChanged();
        } else {
          optionsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      public Builder setOptions(
          com.google.protobuf.DescriptorProtos.ServiceOptions.Builder builderForValue) {
        if (optionsBuilder_ == null) {
          options_ = builderForValue.build();
          onChanged();
        } else {
          optionsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      public Builder mergeOptions(com.google.protobuf.DescriptorProtos.ServiceOptions value) {
        if (optionsBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
              options_ != com.google.protobuf.DescriptorProtos.ServiceOptions.getDefaultInstance()) {
            options_ =
              com.google.protobuf.DescriptorProtos.ServiceOptions.newBuilder(options_).mergeFrom(value).buildPartial();
          } else {
            options_ = value;
          }
          onChanged();
        } else {
          optionsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000004;
        return this;
      }

      public Builder clearOptions() {
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.ServiceOptions.getDefaultInstance();
          onChanged();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }

      public com.google.protobuf.DescriptorProtos.ServiceOptions.Builder getOptionsBuilder() {
        bitField0_ |= 0x00000004;
        onChanged();
        return getOptionsFieldBuilder().getBuilder();
      }

      public com.google.protobuf.DescriptorProtos.ServiceOptionsOrBuilder getOptionsOrBuilder() {
        if (optionsBuilder_ != null) {
          return optionsBuilder_.getMessageOrBuilder();
        } else {
          return options_;
        }
      }

      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.ServiceOptions, com.google.protobuf.DescriptorProtos.ServiceOptions.Builder, com.google.protobuf.DescriptorProtos.ServiceOptionsOrBuilder> 
          getOptionsFieldBuilder() {
        if (optionsBuilder_ == null) {
          optionsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.google.protobuf.DescriptorProtos.ServiceOptions, com.google.protobuf.DescriptorProtos.ServiceOptions.Builder, com.google.protobuf.DescriptorProtos.ServiceOptionsOrBuilder>(
                  options_,
                  getParentForChildren(),
                  isClean());
          options_ = null;
        }
        return optionsBuilder_;
      }

    }

    static {
      defaultInstance = new ServiceDescriptorProto(true);
      defaultInstance.initFields();
    }

  }

  public interface MethodDescriptorProtoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    boolean hasName();

    java.lang.String getName();

    com.google.protobuf.ByteString
        getNameBytes();

    boolean hasInputType();

    java.lang.String getInputType();

    com.google.protobuf.ByteString
        getInputTypeBytes();

    boolean hasOutputType();

    java.lang.String getOutputType();

    com.google.protobuf.ByteString
        getOutputTypeBytes();

    boolean hasOptions();

    com.google.protobuf.DescriptorProtos.MethodOptions getOptions();

    com.google.protobuf.DescriptorProtos.MethodOptionsOrBuilder getOptionsOrBuilder();
  }

  public static final class MethodDescriptorProto extends
      com.google.protobuf.GeneratedMessage
      implements MethodDescriptorProtoOrBuilder {

    private MethodDescriptorProto(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MethodDescriptorProto(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MethodDescriptorProto defaultInstance;
    public static MethodDescriptorProto getDefaultInstance() {
      return defaultInstance;
    }

    public MethodDescriptorProto getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MethodDescriptorProto(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              name_ = input.readBytes();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              inputType_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              outputType_ = input.readBytes();
              break;
            }
            case 34: {
              com.google.protobuf.DescriptorProtos.MethodOptions.Builder subBuilder = null;
              if (((bitField0_ & 0x00000008) == 0x00000008)) {
                subBuilder = options_.toBuilder();
              }
              options_ = input.readMessage(com.google.protobuf.DescriptorProtos.MethodOptions.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(options_);
                options_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000008;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.MethodDescriptorProto.class, com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder.class);
    }

    public static com.google.protobuf.Parser<MethodDescriptorProto> PARSER =
        new com.google.protobuf.AbstractParser<MethodDescriptorProto>() {
      public MethodDescriptorProto parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MethodDescriptorProto(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MethodDescriptorProto> getParserForType() {
      return PARSER;
    }

    private int bitField0_;

    public static final int NAME_FIELD_NUMBER = 1;
    private java.lang.Object name_;

    public boolean hasName() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public java.lang.String getName() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          name_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getNameBytes() {
      java.lang.Object ref = name_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        name_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int INPUT_TYPE_FIELD_NUMBER = 2;
    private java.lang.Object inputType_;

    public boolean hasInputType() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public java.lang.String getInputType() {
      java.lang.Object ref = inputType_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          inputType_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getInputTypeBytes() {
      java.lang.Object ref = inputType_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        inputType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int OUTPUT_TYPE_FIELD_NUMBER = 3;
    private java.lang.Object outputType_;

    public boolean hasOutputType() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }

    public java.lang.String getOutputType() {
      java.lang.Object ref = outputType_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          outputType_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getOutputTypeBytes() {
      java.lang.Object ref = outputType_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        outputType_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int OPTIONS_FIELD_NUMBER = 4;
    private com.google.protobuf.DescriptorProtos.MethodOptions options_;

    public boolean hasOptions() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }

    public com.google.protobuf.DescriptorProtos.MethodOptions getOptions() {
      return options_;
    }

    public com.google.protobuf.DescriptorProtos.MethodOptionsOrBuilder getOptionsOrBuilder() {
      return options_;
    }

    private void initFields() {
      name_ = "";
      inputType_ = "";
      outputType_ = "";
      options_ = com.google.protobuf.DescriptorProtos.MethodOptions.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (hasOptions()) {
        if (!getOptions().isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getInputTypeBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getOutputTypeBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(4, options_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getNameBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getInputTypeBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getOutputTypeBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, options_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.MethodDescriptorProto parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.MethodDescriptorProto prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.MethodDescriptorProtoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.MethodDescriptorProto.class, com.google.protobuf.DescriptorProtos.MethodDescriptorProto.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getOptionsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        name_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        inputType_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        outputType_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.MethodOptions.getDefaultInstance();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodDescriptorProto_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.MethodDescriptorProto getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.MethodDescriptorProto.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.MethodDescriptorProto build() {
        com.google.protobuf.DescriptorProtos.MethodDescriptorProto result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.MethodDescriptorProto buildPartial() {
        com.google.protobuf.DescriptorProtos.MethodDescriptorProto result = new com.google.protobuf.DescriptorProtos.MethodDescriptorProto(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.name_ = name_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.inputType_ = inputType_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.outputType_ = outputType_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (optionsBuilder_ == null) {
          result.options_ = options_;
        } else {
          result.options_ = optionsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.MethodDescriptorProto) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.MethodDescriptorProto)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.MethodDescriptorProto other) {
        if (other == com.google.protobuf.DescriptorProtos.MethodDescriptorProto.getDefaultInstance()) return this;
        if (other.hasName()) {
          bitField0_ |= 0x00000001;
          name_ = other.name_;
          onChanged();
        }
        if (other.hasInputType()) {
          bitField0_ |= 0x00000002;
          inputType_ = other.inputType_;
          onChanged();
        }
        if (other.hasOutputType()) {
          bitField0_ |= 0x00000004;
          outputType_ = other.outputType_;
          onChanged();
        }
        if (other.hasOptions()) {
          mergeOptions(other.getOptions());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (hasOptions()) {
          if (!getOptions().isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.MethodDescriptorProto parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.MethodDescriptorProto) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object name_ = "";

      public boolean hasName() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getName() {
        java.lang.Object ref = name_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          name_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getNameBytes() {
        java.lang.Object ref = name_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          name_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setName(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      public Builder clearName() {
        bitField0_ = (bitField0_ & ~0x00000001);
        name_ = getDefaultInstance().getName();
        onChanged();
        return this;
      }

      public Builder setNameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        name_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object inputType_ = "";

      public boolean hasInputType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public java.lang.String getInputType() {
        java.lang.Object ref = inputType_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          inputType_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getInputTypeBytes() {
        java.lang.Object ref = inputType_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          inputType_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setInputType(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        inputType_ = value;
        onChanged();
        return this;
      }

      public Builder clearInputType() {
        bitField0_ = (bitField0_ & ~0x00000002);
        inputType_ = getDefaultInstance().getInputType();
        onChanged();
        return this;
      }

      public Builder setInputTypeBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        inputType_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object outputType_ = "";

      public boolean hasOutputType() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      public java.lang.String getOutputType() {
        java.lang.Object ref = outputType_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          outputType_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getOutputTypeBytes() {
        java.lang.Object ref = outputType_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          outputType_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setOutputType(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        outputType_ = value;
        onChanged();
        return this;
      }

      public Builder clearOutputType() {
        bitField0_ = (bitField0_ & ~0x00000004);
        outputType_ = getDefaultInstance().getOutputType();
        onChanged();
        return this;
      }

      public Builder setOutputTypeBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        outputType_ = value;
        onChanged();
        return this;
      }

      private com.google.protobuf.DescriptorProtos.MethodOptions options_ = com.google.protobuf.DescriptorProtos.MethodOptions.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.MethodOptions, com.google.protobuf.DescriptorProtos.MethodOptions.Builder, com.google.protobuf.DescriptorProtos.MethodOptionsOrBuilder> optionsBuilder_;

      public boolean hasOptions() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }

      public com.google.protobuf.DescriptorProtos.MethodOptions getOptions() {
        if (optionsBuilder_ == null) {
          return options_;
        } else {
          return optionsBuilder_.getMessage();
        }
      }

      public Builder setOptions(com.google.protobuf.DescriptorProtos.MethodOptions value) {
        if (optionsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          options_ = value;
          onChanged();
        } else {
          optionsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }

      public Builder setOptions(
          com.google.protobuf.DescriptorProtos.MethodOptions.Builder builderForValue) {
        if (optionsBuilder_ == null) {
          options_ = builderForValue.build();
          onChanged();
        } else {
          optionsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }

      public Builder mergeOptions(com.google.protobuf.DescriptorProtos.MethodOptions value) {
        if (optionsBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              options_ != com.google.protobuf.DescriptorProtos.MethodOptions.getDefaultInstance()) {
            options_ =
              com.google.protobuf.DescriptorProtos.MethodOptions.newBuilder(options_).mergeFrom(value).buildPartial();
          } else {
            options_ = value;
          }
          onChanged();
        } else {
          optionsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }

      public Builder clearOptions() {
        if (optionsBuilder_ == null) {
          options_ = com.google.protobuf.DescriptorProtos.MethodOptions.getDefaultInstance();
          onChanged();
        } else {
          optionsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public com.google.protobuf.DescriptorProtos.MethodOptions.Builder getOptionsBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getOptionsFieldBuilder().getBuilder();
      }

      public com.google.protobuf.DescriptorProtos.MethodOptionsOrBuilder getOptionsOrBuilder() {
        if (optionsBuilder_ != null) {
          return optionsBuilder_.getMessageOrBuilder();
        } else {
          return options_;
        }
      }

      private com.google.protobuf.SingleFieldBuilder<
          com.google.protobuf.DescriptorProtos.MethodOptions, com.google.protobuf.DescriptorProtos.MethodOptions.Builder, com.google.protobuf.DescriptorProtos.MethodOptionsOrBuilder> 
          getOptionsFieldBuilder() {
        if (optionsBuilder_ == null) {
          optionsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.google.protobuf.DescriptorProtos.MethodOptions, com.google.protobuf.DescriptorProtos.MethodOptions.Builder, com.google.protobuf.DescriptorProtos.MethodOptionsOrBuilder>(
                  options_,
                  getParentForChildren(),
                  isClean());
          options_ = null;
        }
        return optionsBuilder_;
      }

    }

    static {
      defaultInstance = new MethodDescriptorProto(true);
      defaultInstance.initFields();
    }

  }

  public interface FileOptionsOrBuilder extends
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<FileOptions> {

    boolean hasJavaPackage();

    java.lang.String getJavaPackage();

    com.google.protobuf.ByteString
        getJavaPackageBytes();

    boolean hasJavaOuterClassname();

    java.lang.String getJavaOuterClassname();

    com.google.protobuf.ByteString
        getJavaOuterClassnameBytes();

    boolean hasJavaMultipleFiles();

    boolean getJavaMultipleFiles();

    boolean hasJavaGenerateEqualsAndHash();

    boolean getJavaGenerateEqualsAndHash();

    boolean hasOptimizeFor();

    com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode getOptimizeFor();

    boolean hasGoPackage();

    java.lang.String getGoPackage();

    com.google.protobuf.ByteString
        getGoPackageBytes();

    boolean hasCcGenericServices();

    boolean getCcGenericServices();

    boolean hasJavaGenericServices();

    boolean getJavaGenericServices();

    boolean hasPyGenericServices();

    boolean getPyGenericServices();

    java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> 
        getUninterpretedOptionList();

    com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index);

    int getUninterpretedOptionCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList();

    com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index);
  }

  public static final class FileOptions extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        FileOptions> implements FileOptionsOrBuilder {

    private FileOptions(com.google.protobuf.GeneratedMessage.ExtendableBuilder<com.google.protobuf.DescriptorProtos.FileOptions, ?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private FileOptions(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final FileOptions defaultInstance;
    public static FileOptions getDefaultInstance() {
      return defaultInstance;
    }

    public FileOptions getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private FileOptions(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              javaPackage_ = input.readBytes();
              break;
            }
            case 66: {
              bitField0_ |= 0x00000002;
              javaOuterClassname_ = input.readBytes();
              break;
            }
            case 72: {
              int rawValue = input.readEnum();
              com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode value = com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(9, rawValue);
              } else {
                bitField0_ |= 0x00000010;
                optimizeFor_ = value;
              }
              break;
            }
            case 80: {
              bitField0_ |= 0x00000004;
              javaMultipleFiles_ = input.readBool();
              break;
            }
            case 90: {
              bitField0_ |= 0x00000020;
              goPackage_ = input.readBytes();
              break;
            }
            case 128: {
              bitField0_ |= 0x00000040;
              ccGenericServices_ = input.readBool();
              break;
            }
            case 136: {
              bitField0_ |= 0x00000080;
              javaGenericServices_ = input.readBool();
              break;
            }
            case 144: {
              bitField0_ |= 0x00000100;
              pyGenericServices_ = input.readBool();
              break;
            }
            case 160: {
              bitField0_ |= 0x00000008;
              javaGenerateEqualsAndHash_ = input.readBool();
              break;
            }
            case 7994: {
              if (!((mutable_bitField0_ & 0x00000200) == 0x00000200)) {
                uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>();
                mutable_bitField0_ |= 0x00000200;
              }
              uninterpretedOption_.add(input.readMessage(com.google.protobuf.DescriptorProtos.UninterpretedOption.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000200) == 0x00000200)) {
          uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileOptions_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.FileOptions.class, com.google.protobuf.DescriptorProtos.FileOptions.Builder.class);
    }

    public static com.google.protobuf.Parser<FileOptions> PARSER =
        new com.google.protobuf.AbstractParser<FileOptions>() {
      public FileOptions parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new FileOptions(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<FileOptions> getParserForType() {
      return PARSER;
    }

    public enum OptimizeMode
        implements com.google.protobuf.ProtocolMessageEnum {

      SPEED(0, 1),

      CODE_SIZE(1, 2),

      LITE_RUNTIME(2, 3),
      ;

      public static final int SPEED_VALUE = 1;

      public static final int CODE_SIZE_VALUE = 2;

      public static final int LITE_RUNTIME_VALUE = 3;

      public final int getNumber() { return value; }

      public static OptimizeMode valueOf(int value) {
        switch (value) {
          case 1: return SPEED;
          case 2: return CODE_SIZE;
          case 3: return LITE_RUNTIME;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<OptimizeMode>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<OptimizeMode>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<OptimizeMode>() {
              public OptimizeMode findValueByNumber(int number) {
                return OptimizeMode.valueOf(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.FileOptions.getDescriptor().getEnumTypes().get(0);
      }

      private static final OptimizeMode[] VALUES = values();

      public static OptimizeMode valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private OptimizeMode(int index, int value) {
        this.index = index;
        this.value = value;
      }

    }

    private int bitField0_;

    public static final int JAVA_PACKAGE_FIELD_NUMBER = 1;
    private java.lang.Object javaPackage_;

    public boolean hasJavaPackage() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public java.lang.String getJavaPackage() {
      java.lang.Object ref = javaPackage_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          javaPackage_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getJavaPackageBytes() {
      java.lang.Object ref = javaPackage_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        javaPackage_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int JAVA_OUTER_CLASSNAME_FIELD_NUMBER = 8;
    private java.lang.Object javaOuterClassname_;

    public boolean hasJavaOuterClassname() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public java.lang.String getJavaOuterClassname() {
      java.lang.Object ref = javaOuterClassname_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          javaOuterClassname_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getJavaOuterClassnameBytes() {
      java.lang.Object ref = javaOuterClassname_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        javaOuterClassname_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int JAVA_MULTIPLE_FILES_FIELD_NUMBER = 10;
    private boolean javaMultipleFiles_;

    public boolean hasJavaMultipleFiles() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }

    public boolean getJavaMultipleFiles() {
      return javaMultipleFiles_;
    }

    public static final int JAVA_GENERATE_EQUALS_AND_HASH_FIELD_NUMBER = 20;
    private boolean javaGenerateEqualsAndHash_;

    public boolean hasJavaGenerateEqualsAndHash() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }

    public boolean getJavaGenerateEqualsAndHash() {
      return javaGenerateEqualsAndHash_;
    }

    public static final int OPTIMIZE_FOR_FIELD_NUMBER = 9;
    private com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode optimizeFor_;

    public boolean hasOptimizeFor() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }

    public com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode getOptimizeFor() {
      return optimizeFor_;
    }

    public static final int GO_PACKAGE_FIELD_NUMBER = 11;
    private java.lang.Object goPackage_;

    public boolean hasGoPackage() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }

    public java.lang.String getGoPackage() {
      java.lang.Object ref = goPackage_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          goPackage_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getGoPackageBytes() {
      java.lang.Object ref = goPackage_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        goPackage_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int CC_GENERIC_SERVICES_FIELD_NUMBER = 16;
    private boolean ccGenericServices_;

    public boolean hasCcGenericServices() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }

    public boolean getCcGenericServices() {
      return ccGenericServices_;
    }

    public static final int JAVA_GENERIC_SERVICES_FIELD_NUMBER = 17;
    private boolean javaGenericServices_;

    public boolean hasJavaGenericServices() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }

    public boolean getJavaGenericServices() {
      return javaGenericServices_;
    }

    public static final int PY_GENERIC_SERVICES_FIELD_NUMBER = 18;
    private boolean pyGenericServices_;

    public boolean hasPyGenericServices() {
      return ((bitField0_ & 0x00000100) == 0x00000100);
    }

    public boolean getPyGenericServices() {
      return pyGenericServices_;
    }

    public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
    private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_;

    public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
      return uninterpretedOption_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList() {
      return uninterpretedOption_;
    }

    public int getUninterpretedOptionCount() {
      return uninterpretedOption_.size();
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
      return uninterpretedOption_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index) {
      return uninterpretedOption_.get(index);
    }

    private void initFields() {
      javaPackage_ = "";
      javaOuterClassname_ = "";
      javaMultipleFiles_ = false;
      javaGenerateEqualsAndHash_ = false;
      optimizeFor_ = com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode.SPEED;
      goPackage_ = "";
      ccGenericServices_ = false;
      javaGenericServices_ = false;
      pyGenericServices_ = false;
      uninterpretedOption_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getUninterpretedOptionCount(); i++) {
        if (!getUninterpretedOption(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (!extensionsAreInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      com.google.protobuf.GeneratedMessage
        .ExtendableMessage<com.google.protobuf.DescriptorProtos.FileOptions>.ExtensionWriter extensionWriter =
          newExtensionWriter();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getJavaPackageBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(8, getJavaOuterClassnameBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeEnum(9, optimizeFor_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBool(10, javaMultipleFiles_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(11, getGoPackageBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBool(16, ccGenericServices_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeBool(17, javaGenericServices_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        output.writeBool(18, pyGenericServices_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBool(20, javaGenerateEqualsAndHash_);
      }
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        output.writeMessage(999, uninterpretedOption_.get(i));
      }
      extensionWriter.writeUntil(536870912, output);
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getJavaPackageBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(8, getJavaOuterClassnameBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(9, optimizeFor_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(10, javaMultipleFiles_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(11, getGoPackageBytes());
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(16, ccGenericServices_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(17, javaGenericServices_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(18, pyGenericServices_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(20, javaGenerateEqualsAndHash_);
      }
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(999, uninterpretedOption_.get(i));
      }
      size += extensionsSerializedSize();
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.FileOptions parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FileOptions parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileOptions parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FileOptions parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileOptions parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FileOptions parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileOptions parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FileOptions parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FileOptions parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FileOptions parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.FileOptions prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.ExtendableBuilder<
          com.google.protobuf.DescriptorProtos.FileOptions, Builder> implements com.google.protobuf.DescriptorProtos.FileOptionsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileOptions_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.FileOptions.class, com.google.protobuf.DescriptorProtos.FileOptions.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getUninterpretedOptionFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        javaPackage_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        javaOuterClassname_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        javaMultipleFiles_ = false;
        bitField0_ = (bitField0_ & ~0x00000004);
        javaGenerateEqualsAndHash_ = false;
        bitField0_ = (bitField0_ & ~0x00000008);
        optimizeFor_ = com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode.SPEED;
        bitField0_ = (bitField0_ & ~0x00000010);
        goPackage_ = "";
        bitField0_ = (bitField0_ & ~0x00000020);
        ccGenericServices_ = false;
        bitField0_ = (bitField0_ & ~0x00000040);
        javaGenericServices_ = false;
        bitField0_ = (bitField0_ & ~0x00000080);
        pyGenericServices_ = false;
        bitField0_ = (bitField0_ & ~0x00000100);
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000200);
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FileOptions_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.FileOptions getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.FileOptions.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.FileOptions build() {
        com.google.protobuf.DescriptorProtos.FileOptions result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.FileOptions buildPartial() {
        com.google.protobuf.DescriptorProtos.FileOptions result = new com.google.protobuf.DescriptorProtos.FileOptions(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.javaPackage_ = javaPackage_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.javaOuterClassname_ = javaOuterClassname_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.javaMultipleFiles_ = javaMultipleFiles_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.javaGenerateEqualsAndHash_ = javaGenerateEqualsAndHash_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.optimizeFor_ = optimizeFor_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.goPackage_ = goPackage_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.ccGenericServices_ = ccGenericServices_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.javaGenericServices_ = javaGenericServices_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000100;
        }
        result.pyGenericServices_ = pyGenericServices_;
        if (uninterpretedOptionBuilder_ == null) {
          if (((bitField0_ & 0x00000200) == 0x00000200)) {
            uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
            bitField0_ = (bitField0_ & ~0x00000200);
          }
          result.uninterpretedOption_ = uninterpretedOption_;
        } else {
          result.uninterpretedOption_ = uninterpretedOptionBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.FileOptions) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.FileOptions)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.FileOptions other) {
        if (other == com.google.protobuf.DescriptorProtos.FileOptions.getDefaultInstance()) return this;
        if (other.hasJavaPackage()) {
          bitField0_ |= 0x00000001;
          javaPackage_ = other.javaPackage_;
          onChanged();
        }
        if (other.hasJavaOuterClassname()) {
          bitField0_ |= 0x00000002;
          javaOuterClassname_ = other.javaOuterClassname_;
          onChanged();
        }
        if (other.hasJavaMultipleFiles()) {
          setJavaMultipleFiles(other.getJavaMultipleFiles());
        }
        if (other.hasJavaGenerateEqualsAndHash()) {
          setJavaGenerateEqualsAndHash(other.getJavaGenerateEqualsAndHash());
        }
        if (other.hasOptimizeFor()) {
          setOptimizeFor(other.getOptimizeFor());
        }
        if (other.hasGoPackage()) {
          bitField0_ |= 0x00000020;
          goPackage_ = other.goPackage_;
          onChanged();
        }
        if (other.hasCcGenericServices()) {
          setCcGenericServices(other.getCcGenericServices());
        }
        if (other.hasJavaGenericServices()) {
          setJavaGenericServices(other.getJavaGenericServices());
        }
        if (other.hasPyGenericServices()) {
          setPyGenericServices(other.getPyGenericServices());
        }
        if (uninterpretedOptionBuilder_ == null) {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOption_.isEmpty()) {
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000200);
            } else {
              ensureUninterpretedOptionIsMutable();
              uninterpretedOption_.addAll(other.uninterpretedOption_);
            }
            onChanged();
          }
        } else {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOptionBuilder_.isEmpty()) {
              uninterpretedOptionBuilder_.dispose();
              uninterpretedOptionBuilder_ = null;
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000200);
              uninterpretedOptionBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getUninterpretedOptionFieldBuilder() : null;
            } else {
              uninterpretedOptionBuilder_.addAllMessages(other.uninterpretedOption_);
            }
          }
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getUninterpretedOptionCount(); i++) {
          if (!getUninterpretedOption(i).isInitialized()) {

            return false;
          }
        }
        if (!extensionsAreInitialized()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.FileOptions parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.FileOptions) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.lang.Object javaPackage_ = "";

      public boolean hasJavaPackage() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getJavaPackage() {
        java.lang.Object ref = javaPackage_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          javaPackage_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getJavaPackageBytes() {
        java.lang.Object ref = javaPackage_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          javaPackage_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setJavaPackage(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        javaPackage_ = value;
        onChanged();
        return this;
      }

      public Builder clearJavaPackage() {
        bitField0_ = (bitField0_ & ~0x00000001);
        javaPackage_ = getDefaultInstance().getJavaPackage();
        onChanged();
        return this;
      }

      public Builder setJavaPackageBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        javaPackage_ = value;
        onChanged();
        return this;
      }

      private java.lang.Object javaOuterClassname_ = "";

      public boolean hasJavaOuterClassname() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public java.lang.String getJavaOuterClassname() {
        java.lang.Object ref = javaOuterClassname_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          javaOuterClassname_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getJavaOuterClassnameBytes() {
        java.lang.Object ref = javaOuterClassname_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          javaOuterClassname_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setJavaOuterClassname(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        javaOuterClassname_ = value;
        onChanged();
        return this;
      }

      public Builder clearJavaOuterClassname() {
        bitField0_ = (bitField0_ & ~0x00000002);
        javaOuterClassname_ = getDefaultInstance().getJavaOuterClassname();
        onChanged();
        return this;
      }

      public Builder setJavaOuterClassnameBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        javaOuterClassname_ = value;
        onChanged();
        return this;
      }

      private boolean javaMultipleFiles_ ;

      public boolean hasJavaMultipleFiles() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      public boolean getJavaMultipleFiles() {
        return javaMultipleFiles_;
      }

      public Builder setJavaMultipleFiles(boolean value) {
        bitField0_ |= 0x00000004;
        javaMultipleFiles_ = value;
        onChanged();
        return this;
      }

      public Builder clearJavaMultipleFiles() {
        bitField0_ = (bitField0_ & ~0x00000004);
        javaMultipleFiles_ = false;
        onChanged();
        return this;
      }

      private boolean javaGenerateEqualsAndHash_ ;

      public boolean hasJavaGenerateEqualsAndHash() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }

      public boolean getJavaGenerateEqualsAndHash() {
        return javaGenerateEqualsAndHash_;
      }

      public Builder setJavaGenerateEqualsAndHash(boolean value) {
        bitField0_ |= 0x00000008;
        javaGenerateEqualsAndHash_ = value;
        onChanged();
        return this;
      }

      public Builder clearJavaGenerateEqualsAndHash() {
        bitField0_ = (bitField0_ & ~0x00000008);
        javaGenerateEqualsAndHash_ = false;
        onChanged();
        return this;
      }

      private com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode optimizeFor_ = com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode.SPEED;

      public boolean hasOptimizeFor() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }

      public com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode getOptimizeFor() {
        return optimizeFor_;
      }

      public Builder setOptimizeFor(com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000010;
        optimizeFor_ = value;
        onChanged();
        return this;
      }

      public Builder clearOptimizeFor() {
        bitField0_ = (bitField0_ & ~0x00000010);
        optimizeFor_ = com.google.protobuf.DescriptorProtos.FileOptions.OptimizeMode.SPEED;
        onChanged();
        return this;
      }

      private java.lang.Object goPackage_ = "";

      public boolean hasGoPackage() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }

      public java.lang.String getGoPackage() {
        java.lang.Object ref = goPackage_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          goPackage_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getGoPackageBytes() {
        java.lang.Object ref = goPackage_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          goPackage_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setGoPackage(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        goPackage_ = value;
        onChanged();
        return this;
      }

      public Builder clearGoPackage() {
        bitField0_ = (bitField0_ & ~0x00000020);
        goPackage_ = getDefaultInstance().getGoPackage();
        onChanged();
        return this;
      }

      public Builder setGoPackageBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        goPackage_ = value;
        onChanged();
        return this;
      }

      private boolean ccGenericServices_ ;

      public boolean hasCcGenericServices() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }

      public boolean getCcGenericServices() {
        return ccGenericServices_;
      }

      public Builder setCcGenericServices(boolean value) {
        bitField0_ |= 0x00000040;
        ccGenericServices_ = value;
        onChanged();
        return this;
      }

      public Builder clearCcGenericServices() {
        bitField0_ = (bitField0_ & ~0x00000040);
        ccGenericServices_ = false;
        onChanged();
        return this;
      }

      private boolean javaGenericServices_ ;

      public boolean hasJavaGenericServices() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }

      public boolean getJavaGenericServices() {
        return javaGenericServices_;
      }

      public Builder setJavaGenericServices(boolean value) {
        bitField0_ |= 0x00000080;
        javaGenericServices_ = value;
        onChanged();
        return this;
      }

      public Builder clearJavaGenericServices() {
        bitField0_ = (bitField0_ & ~0x00000080);
        javaGenericServices_ = false;
        onChanged();
        return this;
      }

      private boolean pyGenericServices_ ;

      public boolean hasPyGenericServices() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }

      public boolean getPyGenericServices() {
        return pyGenericServices_;
      }

      public Builder setPyGenericServices(boolean value) {
        bitField0_ |= 0x00000100;
        pyGenericServices_ = value;
        onChanged();
        return this;
      }

      public Builder clearPyGenericServices() {
        bitField0_ = (bitField0_ & ~0x00000100);
        pyGenericServices_ = false;
        onChanged();
        return this;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_ =
        java.util.Collections.emptyList();
      private void ensureUninterpretedOptionIsMutable() {
        if (!((bitField0_ & 0x00000200) == 0x00000200)) {
          uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>(uninterpretedOption_);
          bitField0_ |= 0x00000200;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
        if (uninterpretedOptionBuilder_ == null) {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        } else {
          return uninterpretedOptionBuilder_.getMessageList();
        }
      }

      public int getUninterpretedOptionCount() {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.size();
        } else {
          return uninterpretedOptionBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);
        } else {
          return uninterpretedOptionBuilder_.getMessage(index);
        }
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllUninterpretedOption(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption> values) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          super.addAll(values, uninterpretedOption_);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearUninterpretedOption() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000200);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder removeUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.remove(index);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
          int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);  } else {
          return uninterpretedOptionBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
           getUninterpretedOptionOrBuilderList() {
        if (uninterpretedOptionBuilder_ != null) {
          return uninterpretedOptionBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder> 
           getUninterpretedOptionBuilderList() {
        return getUninterpretedOptionFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
          getUninterpretedOptionFieldBuilder() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOptionBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder>(
                  uninterpretedOption_,
                  ((bitField0_ & 0x00000200) == 0x00000200),
                  getParentForChildren(),
                  isClean());
          uninterpretedOption_ = null;
        }
        return uninterpretedOptionBuilder_;
      }

    }

    static {
      defaultInstance = new FileOptions(true);
      defaultInstance.initFields();
    }

  }

  public interface MessageOptionsOrBuilder extends
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<MessageOptions> {

    boolean hasMessageSetWireFormat();

    boolean getMessageSetWireFormat();

    boolean hasNoStandardDescriptorAccessor();

    boolean getNoStandardDescriptorAccessor();

    java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> 
        getUninterpretedOptionList();

    com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index);

    int getUninterpretedOptionCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList();

    com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index);
  }

  public static final class MessageOptions extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        MessageOptions> implements MessageOptionsOrBuilder {

    private MessageOptions(com.google.protobuf.GeneratedMessage.ExtendableBuilder<com.google.protobuf.DescriptorProtos.MessageOptions, ?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MessageOptions(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MessageOptions defaultInstance;
    public static MessageOptions getDefaultInstance() {
      return defaultInstance;
    }

    public MessageOptions getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MessageOptions(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              messageSetWireFormat_ = input.readBool();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              noStandardDescriptorAccessor_ = input.readBool();
              break;
            }
            case 7994: {
              if (!((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
                uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>();
                mutable_bitField0_ |= 0x00000004;
              }
              uninterpretedOption_.add(input.readMessage(com.google.protobuf.DescriptorProtos.UninterpretedOption.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000004) == 0x00000004)) {
          uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MessageOptions_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.MessageOptions.class, com.google.protobuf.DescriptorProtos.MessageOptions.Builder.class);
    }

    public static com.google.protobuf.Parser<MessageOptions> PARSER =
        new com.google.protobuf.AbstractParser<MessageOptions>() {
      public MessageOptions parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MessageOptions(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MessageOptions> getParserForType() {
      return PARSER;
    }

    private int bitField0_;

    public static final int MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER = 1;
    private boolean messageSetWireFormat_;

    public boolean hasMessageSetWireFormat() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public boolean getMessageSetWireFormat() {
      return messageSetWireFormat_;
    }

    public static final int NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER = 2;
    private boolean noStandardDescriptorAccessor_;

    public boolean hasNoStandardDescriptorAccessor() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public boolean getNoStandardDescriptorAccessor() {
      return noStandardDescriptorAccessor_;
    }

    public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
    private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_;

    public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
      return uninterpretedOption_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList() {
      return uninterpretedOption_;
    }

    public int getUninterpretedOptionCount() {
      return uninterpretedOption_.size();
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
      return uninterpretedOption_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index) {
      return uninterpretedOption_.get(index);
    }

    private void initFields() {
      messageSetWireFormat_ = false;
      noStandardDescriptorAccessor_ = false;
      uninterpretedOption_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getUninterpretedOptionCount(); i++) {
        if (!getUninterpretedOption(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (!extensionsAreInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      com.google.protobuf.GeneratedMessage
        .ExtendableMessage<com.google.protobuf.DescriptorProtos.MessageOptions>.ExtensionWriter extensionWriter =
          newExtensionWriter();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBool(1, messageSetWireFormat_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBool(2, noStandardDescriptorAccessor_);
      }
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        output.writeMessage(999, uninterpretedOption_.get(i));
      }
      extensionWriter.writeUntil(536870912, output);
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(1, messageSetWireFormat_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(2, noStandardDescriptorAccessor_);
      }
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(999, uninterpretedOption_.get(i));
      }
      size += extensionsSerializedSize();
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.MessageOptions parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.MessageOptions parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MessageOptions parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.MessageOptions parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MessageOptions parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.MessageOptions parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MessageOptions parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.MessageOptions parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MessageOptions parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.MessageOptions parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.MessageOptions prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.ExtendableBuilder<
          com.google.protobuf.DescriptorProtos.MessageOptions, Builder> implements com.google.protobuf.DescriptorProtos.MessageOptionsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MessageOptions_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.MessageOptions.class, com.google.protobuf.DescriptorProtos.MessageOptions.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getUninterpretedOptionFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        messageSetWireFormat_ = false;
        bitField0_ = (bitField0_ & ~0x00000001);
        noStandardDescriptorAccessor_ = false;
        bitField0_ = (bitField0_ & ~0x00000002);
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MessageOptions_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.MessageOptions getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.MessageOptions.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.MessageOptions build() {
        com.google.protobuf.DescriptorProtos.MessageOptions result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.MessageOptions buildPartial() {
        com.google.protobuf.DescriptorProtos.MessageOptions result = new com.google.protobuf.DescriptorProtos.MessageOptions(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.messageSetWireFormat_ = messageSetWireFormat_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.noStandardDescriptorAccessor_ = noStandardDescriptorAccessor_;
        if (uninterpretedOptionBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004)) {
            uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
            bitField0_ = (bitField0_ & ~0x00000004);
          }
          result.uninterpretedOption_ = uninterpretedOption_;
        } else {
          result.uninterpretedOption_ = uninterpretedOptionBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.MessageOptions) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.MessageOptions)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.MessageOptions other) {
        if (other == com.google.protobuf.DescriptorProtos.MessageOptions.getDefaultInstance()) return this;
        if (other.hasMessageSetWireFormat()) {
          setMessageSetWireFormat(other.getMessageSetWireFormat());
        }
        if (other.hasNoStandardDescriptorAccessor()) {
          setNoStandardDescriptorAccessor(other.getNoStandardDescriptorAccessor());
        }
        if (uninterpretedOptionBuilder_ == null) {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOption_.isEmpty()) {
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000004);
            } else {
              ensureUninterpretedOptionIsMutable();
              uninterpretedOption_.addAll(other.uninterpretedOption_);
            }
            onChanged();
          }
        } else {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOptionBuilder_.isEmpty()) {
              uninterpretedOptionBuilder_.dispose();
              uninterpretedOptionBuilder_ = null;
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000004);
              uninterpretedOptionBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getUninterpretedOptionFieldBuilder() : null;
            } else {
              uninterpretedOptionBuilder_.addAllMessages(other.uninterpretedOption_);
            }
          }
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getUninterpretedOptionCount(); i++) {
          if (!getUninterpretedOption(i).isInitialized()) {

            return false;
          }
        }
        if (!extensionsAreInitialized()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.MessageOptions parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.MessageOptions) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private boolean messageSetWireFormat_ ;

      public boolean hasMessageSetWireFormat() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public boolean getMessageSetWireFormat() {
        return messageSetWireFormat_;
      }

      public Builder setMessageSetWireFormat(boolean value) {
        bitField0_ |= 0x00000001;
        messageSetWireFormat_ = value;
        onChanged();
        return this;
      }

      public Builder clearMessageSetWireFormat() {
        bitField0_ = (bitField0_ & ~0x00000001);
        messageSetWireFormat_ = false;
        onChanged();
        return this;
      }

      private boolean noStandardDescriptorAccessor_ ;

      public boolean hasNoStandardDescriptorAccessor() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public boolean getNoStandardDescriptorAccessor() {
        return noStandardDescriptorAccessor_;
      }

      public Builder setNoStandardDescriptorAccessor(boolean value) {
        bitField0_ |= 0x00000002;
        noStandardDescriptorAccessor_ = value;
        onChanged();
        return this;
      }

      public Builder clearNoStandardDescriptorAccessor() {
        bitField0_ = (bitField0_ & ~0x00000002);
        noStandardDescriptorAccessor_ = false;
        onChanged();
        return this;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_ =
        java.util.Collections.emptyList();
      private void ensureUninterpretedOptionIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>(uninterpretedOption_);
          bitField0_ |= 0x00000004;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
        if (uninterpretedOptionBuilder_ == null) {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        } else {
          return uninterpretedOptionBuilder_.getMessageList();
        }
      }

      public int getUninterpretedOptionCount() {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.size();
        } else {
          return uninterpretedOptionBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);
        } else {
          return uninterpretedOptionBuilder_.getMessage(index);
        }
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllUninterpretedOption(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption> values) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          super.addAll(values, uninterpretedOption_);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearUninterpretedOption() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder removeUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.remove(index);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
          int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);  } else {
          return uninterpretedOptionBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
           getUninterpretedOptionOrBuilderList() {
        if (uninterpretedOptionBuilder_ != null) {
          return uninterpretedOptionBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder> 
           getUninterpretedOptionBuilderList() {
        return getUninterpretedOptionFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
          getUninterpretedOptionFieldBuilder() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOptionBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder>(
                  uninterpretedOption_,
                  ((bitField0_ & 0x00000004) == 0x00000004),
                  getParentForChildren(),
                  isClean());
          uninterpretedOption_ = null;
        }
        return uninterpretedOptionBuilder_;
      }

    }

    static {
      defaultInstance = new MessageOptions(true);
      defaultInstance.initFields();
    }

  }

  public interface FieldOptionsOrBuilder extends
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<FieldOptions> {

    boolean hasCtype();

    com.google.protobuf.DescriptorProtos.FieldOptions.CType getCtype();

    boolean hasPacked();

    boolean getPacked();

    boolean hasLazy();

    boolean getLazy();

    boolean hasDeprecated();

    boolean getDeprecated();

    boolean hasExperimentalMapKey();

    java.lang.String getExperimentalMapKey();

    com.google.protobuf.ByteString
        getExperimentalMapKeyBytes();

    boolean hasWeak();

    boolean getWeak();

    java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> 
        getUninterpretedOptionList();

    com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index);

    int getUninterpretedOptionCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList();

    com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index);
  }

  public static final class FieldOptions extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        FieldOptions> implements FieldOptionsOrBuilder {

    private FieldOptions(com.google.protobuf.GeneratedMessage.ExtendableBuilder<com.google.protobuf.DescriptorProtos.FieldOptions, ?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private FieldOptions(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final FieldOptions defaultInstance;
    public static FieldOptions getDefaultInstance() {
      return defaultInstance;
    }

    public FieldOptions getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private FieldOptions(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              int rawValue = input.readEnum();
              com.google.protobuf.DescriptorProtos.FieldOptions.CType value = com.google.protobuf.DescriptorProtos.FieldOptions.CType.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                ctype_ = value;
              }
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              packed_ = input.readBool();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000008;
              deprecated_ = input.readBool();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000004;
              lazy_ = input.readBool();
              break;
            }
            case 74: {
              bitField0_ |= 0x00000010;
              experimentalMapKey_ = input.readBytes();
              break;
            }
            case 80: {
              bitField0_ |= 0x00000020;
              weak_ = input.readBool();
              break;
            }
            case 7994: {
              if (!((mutable_bitField0_ & 0x00000040) == 0x00000040)) {
                uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>();
                mutable_bitField0_ |= 0x00000040;
              }
              uninterpretedOption_.add(input.readMessage(com.google.protobuf.DescriptorProtos.UninterpretedOption.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000040) == 0x00000040)) {
          uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldOptions_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.FieldOptions.class, com.google.protobuf.DescriptorProtos.FieldOptions.Builder.class);
    }

    public static com.google.protobuf.Parser<FieldOptions> PARSER =
        new com.google.protobuf.AbstractParser<FieldOptions>() {
      public FieldOptions parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new FieldOptions(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<FieldOptions> getParserForType() {
      return PARSER;
    }

    public enum CType
        implements com.google.protobuf.ProtocolMessageEnum {

      STRING(0, 0),

      CORD(1, 1),

      STRING_PIECE(2, 2),
      ;

      public static final int STRING_VALUE = 0;

      public static final int CORD_VALUE = 1;

      public static final int STRING_PIECE_VALUE = 2;

      public final int getNumber() { return value; }

      public static CType valueOf(int value) {
        switch (value) {
          case 0: return STRING;
          case 1: return CORD;
          case 2: return STRING_PIECE;
          default: return null;
        }
      }

      public static com.google.protobuf.Internal.EnumLiteMap<CType>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<CType>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<CType>() {
              public CType findValueByNumber(int number) {
                return CType.valueOf(number);
              }
            };

      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.FieldOptions.getDescriptor().getEnumTypes().get(0);
      }

      private static final CType[] VALUES = values();

      public static CType valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }

      private final int index;
      private final int value;

      private CType(int index, int value) {
        this.index = index;
        this.value = value;
      }

    }

    private int bitField0_;

    public static final int CTYPE_FIELD_NUMBER = 1;
    private com.google.protobuf.DescriptorProtos.FieldOptions.CType ctype_;

    public boolean hasCtype() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public com.google.protobuf.DescriptorProtos.FieldOptions.CType getCtype() {
      return ctype_;
    }

    public static final int PACKED_FIELD_NUMBER = 2;
    private boolean packed_;

    public boolean hasPacked() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public boolean getPacked() {
      return packed_;
    }

    public static final int LAZY_FIELD_NUMBER = 5;
    private boolean lazy_;

    public boolean hasLazy() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }

    public boolean getLazy() {
      return lazy_;
    }

    public static final int DEPRECATED_FIELD_NUMBER = 3;
    private boolean deprecated_;

    public boolean hasDeprecated() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }

    public boolean getDeprecated() {
      return deprecated_;
    }

    public static final int EXPERIMENTAL_MAP_KEY_FIELD_NUMBER = 9;
    private java.lang.Object experimentalMapKey_;

    public boolean hasExperimentalMapKey() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }

    public java.lang.String getExperimentalMapKey() {
      java.lang.Object ref = experimentalMapKey_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          experimentalMapKey_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getExperimentalMapKeyBytes() {
      java.lang.Object ref = experimentalMapKey_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        experimentalMapKey_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int WEAK_FIELD_NUMBER = 10;
    private boolean weak_;

    public boolean hasWeak() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }

    public boolean getWeak() {
      return weak_;
    }

    public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
    private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_;

    public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
      return uninterpretedOption_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList() {
      return uninterpretedOption_;
    }

    public int getUninterpretedOptionCount() {
      return uninterpretedOption_.size();
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
      return uninterpretedOption_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index) {
      return uninterpretedOption_.get(index);
    }

    private void initFields() {
      ctype_ = com.google.protobuf.DescriptorProtos.FieldOptions.CType.STRING;
      packed_ = false;
      lazy_ = false;
      deprecated_ = false;
      experimentalMapKey_ = "";
      weak_ = false;
      uninterpretedOption_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getUninterpretedOptionCount(); i++) {
        if (!getUninterpretedOption(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (!extensionsAreInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      com.google.protobuf.GeneratedMessage
        .ExtendableMessage<com.google.protobuf.DescriptorProtos.FieldOptions>.ExtensionWriter extensionWriter =
          newExtensionWriter();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeEnum(1, ctype_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBool(2, packed_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBool(3, deprecated_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBool(5, lazy_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(9, getExperimentalMapKeyBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBool(10, weak_);
      }
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        output.writeMessage(999, uninterpretedOption_.get(i));
      }
      extensionWriter.writeUntil(536870912, output);
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(1, ctype_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(2, packed_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(3, deprecated_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(5, lazy_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(9, getExperimentalMapKeyBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(10, weak_);
      }
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(999, uninterpretedOption_.get(i));
      }
      size += extensionsSerializedSize();
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.FieldOptions parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FieldOptions parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FieldOptions parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.FieldOptions parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FieldOptions parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FieldOptions parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FieldOptions parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FieldOptions parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.FieldOptions parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.FieldOptions parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.FieldOptions prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.ExtendableBuilder<
          com.google.protobuf.DescriptorProtos.FieldOptions, Builder> implements com.google.protobuf.DescriptorProtos.FieldOptionsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldOptions_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.FieldOptions.class, com.google.protobuf.DescriptorProtos.FieldOptions.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getUninterpretedOptionFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        ctype_ = com.google.protobuf.DescriptorProtos.FieldOptions.CType.STRING;
        bitField0_ = (bitField0_ & ~0x00000001);
        packed_ = false;
        bitField0_ = (bitField0_ & ~0x00000002);
        lazy_ = false;
        bitField0_ = (bitField0_ & ~0x00000004);
        deprecated_ = false;
        bitField0_ = (bitField0_ & ~0x00000008);
        experimentalMapKey_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        weak_ = false;
        bitField0_ = (bitField0_ & ~0x00000020);
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_FieldOptions_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.FieldOptions getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.FieldOptions.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.FieldOptions build() {
        com.google.protobuf.DescriptorProtos.FieldOptions result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.FieldOptions buildPartial() {
        com.google.protobuf.DescriptorProtos.FieldOptions result = new com.google.protobuf.DescriptorProtos.FieldOptions(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.ctype_ = ctype_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.packed_ = packed_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.lazy_ = lazy_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.deprecated_ = deprecated_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.experimentalMapKey_ = experimentalMapKey_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.weak_ = weak_;
        if (uninterpretedOptionBuilder_ == null) {
          if (((bitField0_ & 0x00000040) == 0x00000040)) {
            uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
            bitField0_ = (bitField0_ & ~0x00000040);
          }
          result.uninterpretedOption_ = uninterpretedOption_;
        } else {
          result.uninterpretedOption_ = uninterpretedOptionBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.FieldOptions) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.FieldOptions)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.FieldOptions other) {
        if (other == com.google.protobuf.DescriptorProtos.FieldOptions.getDefaultInstance()) return this;
        if (other.hasCtype()) {
          setCtype(other.getCtype());
        }
        if (other.hasPacked()) {
          setPacked(other.getPacked());
        }
        if (other.hasLazy()) {
          setLazy(other.getLazy());
        }
        if (other.hasDeprecated()) {
          setDeprecated(other.getDeprecated());
        }
        if (other.hasExperimentalMapKey()) {
          bitField0_ |= 0x00000010;
          experimentalMapKey_ = other.experimentalMapKey_;
          onChanged();
        }
        if (other.hasWeak()) {
          setWeak(other.getWeak());
        }
        if (uninterpretedOptionBuilder_ == null) {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOption_.isEmpty()) {
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000040);
            } else {
              ensureUninterpretedOptionIsMutable();
              uninterpretedOption_.addAll(other.uninterpretedOption_);
            }
            onChanged();
          }
        } else {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOptionBuilder_.isEmpty()) {
              uninterpretedOptionBuilder_.dispose();
              uninterpretedOptionBuilder_ = null;
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000040);
              uninterpretedOptionBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getUninterpretedOptionFieldBuilder() : null;
            } else {
              uninterpretedOptionBuilder_.addAllMessages(other.uninterpretedOption_);
            }
          }
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getUninterpretedOptionCount(); i++) {
          if (!getUninterpretedOption(i).isInitialized()) {

            return false;
          }
        }
        if (!extensionsAreInitialized()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.FieldOptions parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.FieldOptions) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private com.google.protobuf.DescriptorProtos.FieldOptions.CType ctype_ = com.google.protobuf.DescriptorProtos.FieldOptions.CType.STRING;

      public boolean hasCtype() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public com.google.protobuf.DescriptorProtos.FieldOptions.CType getCtype() {
        return ctype_;
      }

      public Builder setCtype(com.google.protobuf.DescriptorProtos.FieldOptions.CType value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        ctype_ = value;
        onChanged();
        return this;
      }

      public Builder clearCtype() {
        bitField0_ = (bitField0_ & ~0x00000001);
        ctype_ = com.google.protobuf.DescriptorProtos.FieldOptions.CType.STRING;
        onChanged();
        return this;
      }

      private boolean packed_ ;

      public boolean hasPacked() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public boolean getPacked() {
        return packed_;
      }

      public Builder setPacked(boolean value) {
        bitField0_ |= 0x00000002;
        packed_ = value;
        onChanged();
        return this;
      }

      public Builder clearPacked() {
        bitField0_ = (bitField0_ & ~0x00000002);
        packed_ = false;
        onChanged();
        return this;
      }

      private boolean lazy_ ;

      public boolean hasLazy() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      public boolean getLazy() {
        return lazy_;
      }

      public Builder setLazy(boolean value) {
        bitField0_ |= 0x00000004;
        lazy_ = value;
        onChanged();
        return this;
      }

      public Builder clearLazy() {
        bitField0_ = (bitField0_ & ~0x00000004);
        lazy_ = false;
        onChanged();
        return this;
      }

      private boolean deprecated_ ;

      public boolean hasDeprecated() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }

      public boolean getDeprecated() {
        return deprecated_;
      }

      public Builder setDeprecated(boolean value) {
        bitField0_ |= 0x00000008;
        deprecated_ = value;
        onChanged();
        return this;
      }

      public Builder clearDeprecated() {
        bitField0_ = (bitField0_ & ~0x00000008);
        deprecated_ = false;
        onChanged();
        return this;
      }

      private java.lang.Object experimentalMapKey_ = "";

      public boolean hasExperimentalMapKey() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }

      public java.lang.String getExperimentalMapKey() {
        java.lang.Object ref = experimentalMapKey_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          experimentalMapKey_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getExperimentalMapKeyBytes() {
        java.lang.Object ref = experimentalMapKey_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          experimentalMapKey_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setExperimentalMapKey(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        experimentalMapKey_ = value;
        onChanged();
        return this;
      }

      public Builder clearExperimentalMapKey() {
        bitField0_ = (bitField0_ & ~0x00000010);
        experimentalMapKey_ = getDefaultInstance().getExperimentalMapKey();
        onChanged();
        return this;
      }

      public Builder setExperimentalMapKeyBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        experimentalMapKey_ = value;
        onChanged();
        return this;
      }

      private boolean weak_ ;

      public boolean hasWeak() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }

      public boolean getWeak() {
        return weak_;
      }

      public Builder setWeak(boolean value) {
        bitField0_ |= 0x00000020;
        weak_ = value;
        onChanged();
        return this;
      }

      public Builder clearWeak() {
        bitField0_ = (bitField0_ & ~0x00000020);
        weak_ = false;
        onChanged();
        return this;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_ =
        java.util.Collections.emptyList();
      private void ensureUninterpretedOptionIsMutable() {
        if (!((bitField0_ & 0x00000040) == 0x00000040)) {
          uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>(uninterpretedOption_);
          bitField0_ |= 0x00000040;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
        if (uninterpretedOptionBuilder_ == null) {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        } else {
          return uninterpretedOptionBuilder_.getMessageList();
        }
      }

      public int getUninterpretedOptionCount() {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.size();
        } else {
          return uninterpretedOptionBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);
        } else {
          return uninterpretedOptionBuilder_.getMessage(index);
        }
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllUninterpretedOption(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption> values) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          super.addAll(values, uninterpretedOption_);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearUninterpretedOption() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000040);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder removeUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.remove(index);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
          int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);  } else {
          return uninterpretedOptionBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
           getUninterpretedOptionOrBuilderList() {
        if (uninterpretedOptionBuilder_ != null) {
          return uninterpretedOptionBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder> 
           getUninterpretedOptionBuilderList() {
        return getUninterpretedOptionFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
          getUninterpretedOptionFieldBuilder() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOptionBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder>(
                  uninterpretedOption_,
                  ((bitField0_ & 0x00000040) == 0x00000040),
                  getParentForChildren(),
                  isClean());
          uninterpretedOption_ = null;
        }
        return uninterpretedOptionBuilder_;
      }

    }

    static {
      defaultInstance = new FieldOptions(true);
      defaultInstance.initFields();
    }

  }

  public interface EnumOptionsOrBuilder extends
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<EnumOptions> {

    boolean hasAllowAlias();

    boolean getAllowAlias();

    java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> 
        getUninterpretedOptionList();

    com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index);

    int getUninterpretedOptionCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList();

    com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index);
  }

  public static final class EnumOptions extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        EnumOptions> implements EnumOptionsOrBuilder {

    private EnumOptions(com.google.protobuf.GeneratedMessage.ExtendableBuilder<com.google.protobuf.DescriptorProtos.EnumOptions, ?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private EnumOptions(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final EnumOptions defaultInstance;
    public static EnumOptions getDefaultInstance() {
      return defaultInstance;
    }

    public EnumOptions getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private EnumOptions(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 16: {
              bitField0_ |= 0x00000001;
              allowAlias_ = input.readBool();
              break;
            }
            case 7994: {
              if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>();
                mutable_bitField0_ |= 0x00000002;
              }
              uninterpretedOption_.add(input.readMessage(com.google.protobuf.DescriptorProtos.UninterpretedOption.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
          uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumOptions_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.EnumOptions.class, com.google.protobuf.DescriptorProtos.EnumOptions.Builder.class);
    }

    public static com.google.protobuf.Parser<EnumOptions> PARSER =
        new com.google.protobuf.AbstractParser<EnumOptions>() {
      public EnumOptions parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new EnumOptions(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<EnumOptions> getParserForType() {
      return PARSER;
    }

    private int bitField0_;

    public static final int ALLOW_ALIAS_FIELD_NUMBER = 2;
    private boolean allowAlias_;

    public boolean hasAllowAlias() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public boolean getAllowAlias() {
      return allowAlias_;
    }

    public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
    private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_;

    public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
      return uninterpretedOption_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList() {
      return uninterpretedOption_;
    }

    public int getUninterpretedOptionCount() {
      return uninterpretedOption_.size();
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
      return uninterpretedOption_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index) {
      return uninterpretedOption_.get(index);
    }

    private void initFields() {
      allowAlias_ = true;
      uninterpretedOption_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getUninterpretedOptionCount(); i++) {
        if (!getUninterpretedOption(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (!extensionsAreInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      com.google.protobuf.GeneratedMessage
        .ExtendableMessage<com.google.protobuf.DescriptorProtos.EnumOptions>.ExtensionWriter extensionWriter =
          newExtensionWriter();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBool(2, allowAlias_);
      }
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        output.writeMessage(999, uninterpretedOption_.get(i));
      }
      extensionWriter.writeUntil(536870912, output);
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(2, allowAlias_);
      }
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(999, uninterpretedOption_.get(i));
      }
      size += extensionsSerializedSize();
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.EnumOptions parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.EnumOptions parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumOptions parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.EnumOptions parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumOptions parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumOptions parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumOptions parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumOptions parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumOptions parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumOptions parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.EnumOptions prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.ExtendableBuilder<
          com.google.protobuf.DescriptorProtos.EnumOptions, Builder> implements com.google.protobuf.DescriptorProtos.EnumOptionsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumOptions_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.EnumOptions.class, com.google.protobuf.DescriptorProtos.EnumOptions.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getUninterpretedOptionFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        allowAlias_ = true;
        bitField0_ = (bitField0_ & ~0x00000001);
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumOptions_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.EnumOptions getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.EnumOptions.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.EnumOptions build() {
        com.google.protobuf.DescriptorProtos.EnumOptions result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.EnumOptions buildPartial() {
        com.google.protobuf.DescriptorProtos.EnumOptions result = new com.google.protobuf.DescriptorProtos.EnumOptions(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.allowAlias_ = allowAlias_;
        if (uninterpretedOptionBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.uninterpretedOption_ = uninterpretedOption_;
        } else {
          result.uninterpretedOption_ = uninterpretedOptionBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.EnumOptions) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.EnumOptions)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.EnumOptions other) {
        if (other == com.google.protobuf.DescriptorProtos.EnumOptions.getDefaultInstance()) return this;
        if (other.hasAllowAlias()) {
          setAllowAlias(other.getAllowAlias());
        }
        if (uninterpretedOptionBuilder_ == null) {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOption_.isEmpty()) {
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureUninterpretedOptionIsMutable();
              uninterpretedOption_.addAll(other.uninterpretedOption_);
            }
            onChanged();
          }
        } else {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOptionBuilder_.isEmpty()) {
              uninterpretedOptionBuilder_.dispose();
              uninterpretedOptionBuilder_ = null;
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000002);
              uninterpretedOptionBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getUninterpretedOptionFieldBuilder() : null;
            } else {
              uninterpretedOptionBuilder_.addAllMessages(other.uninterpretedOption_);
            }
          }
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getUninterpretedOptionCount(); i++) {
          if (!getUninterpretedOption(i).isInitialized()) {

            return false;
          }
        }
        if (!extensionsAreInitialized()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.EnumOptions parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.EnumOptions) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private boolean allowAlias_ = true;

      public boolean hasAllowAlias() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public boolean getAllowAlias() {
        return allowAlias_;
      }

      public Builder setAllowAlias(boolean value) {
        bitField0_ |= 0x00000001;
        allowAlias_ = value;
        onChanged();
        return this;
      }

      public Builder clearAllowAlias() {
        bitField0_ = (bitField0_ & ~0x00000001);
        allowAlias_ = true;
        onChanged();
        return this;
      }

      private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_ =
        java.util.Collections.emptyList();
      private void ensureUninterpretedOptionIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>(uninterpretedOption_);
          bitField0_ |= 0x00000002;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
        if (uninterpretedOptionBuilder_ == null) {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        } else {
          return uninterpretedOptionBuilder_.getMessageList();
        }
      }

      public int getUninterpretedOptionCount() {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.size();
        } else {
          return uninterpretedOptionBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);
        } else {
          return uninterpretedOptionBuilder_.getMessage(index);
        }
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllUninterpretedOption(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption> values) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          super.addAll(values, uninterpretedOption_);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearUninterpretedOption() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder removeUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.remove(index);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
          int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);  } else {
          return uninterpretedOptionBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
           getUninterpretedOptionOrBuilderList() {
        if (uninterpretedOptionBuilder_ != null) {
          return uninterpretedOptionBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder> 
           getUninterpretedOptionBuilderList() {
        return getUninterpretedOptionFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
          getUninterpretedOptionFieldBuilder() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOptionBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder>(
                  uninterpretedOption_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          uninterpretedOption_ = null;
        }
        return uninterpretedOptionBuilder_;
      }

    }

    static {
      defaultInstance = new EnumOptions(true);
      defaultInstance.initFields();
    }

  }

  public interface EnumValueOptionsOrBuilder extends
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<EnumValueOptions> {

    java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> 
        getUninterpretedOptionList();

    com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index);

    int getUninterpretedOptionCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList();

    com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index);
  }

  public static final class EnumValueOptions extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        EnumValueOptions> implements EnumValueOptionsOrBuilder {

    private EnumValueOptions(com.google.protobuf.GeneratedMessage.ExtendableBuilder<com.google.protobuf.DescriptorProtos.EnumValueOptions, ?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private EnumValueOptions(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final EnumValueOptions defaultInstance;
    public static EnumValueOptions getDefaultInstance() {
      return defaultInstance;
    }

    public EnumValueOptions getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private EnumValueOptions(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 7994: {
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>();
                mutable_bitField0_ |= 0x00000001;
              }
              uninterpretedOption_.add(input.readMessage(com.google.protobuf.DescriptorProtos.UninterpretedOption.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.EnumValueOptions.class, com.google.protobuf.DescriptorProtos.EnumValueOptions.Builder.class);
    }

    public static com.google.protobuf.Parser<EnumValueOptions> PARSER =
        new com.google.protobuf.AbstractParser<EnumValueOptions>() {
      public EnumValueOptions parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new EnumValueOptions(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<EnumValueOptions> getParserForType() {
      return PARSER;
    }

    public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
    private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_;

    public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
      return uninterpretedOption_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList() {
      return uninterpretedOption_;
    }

    public int getUninterpretedOptionCount() {
      return uninterpretedOption_.size();
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
      return uninterpretedOption_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index) {
      return uninterpretedOption_.get(index);
    }

    private void initFields() {
      uninterpretedOption_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getUninterpretedOptionCount(); i++) {
        if (!getUninterpretedOption(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (!extensionsAreInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      com.google.protobuf.GeneratedMessage
        .ExtendableMessage<com.google.protobuf.DescriptorProtos.EnumValueOptions>.ExtensionWriter extensionWriter =
          newExtensionWriter();
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        output.writeMessage(999, uninterpretedOption_.get(i));
      }
      extensionWriter.writeUntil(536870912, output);
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(999, uninterpretedOption_.get(i));
      }
      size += extensionsSerializedSize();
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.EnumValueOptions parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.EnumValueOptions prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.ExtendableBuilder<
          com.google.protobuf.DescriptorProtos.EnumValueOptions, Builder> implements com.google.protobuf.DescriptorProtos.EnumValueOptionsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.EnumValueOptions.class, com.google.protobuf.DescriptorProtos.EnumValueOptions.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getUninterpretedOptionFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_EnumValueOptions_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.EnumValueOptions getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.EnumValueOptions.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.EnumValueOptions build() {
        com.google.protobuf.DescriptorProtos.EnumValueOptions result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.EnumValueOptions buildPartial() {
        com.google.protobuf.DescriptorProtos.EnumValueOptions result = new com.google.protobuf.DescriptorProtos.EnumValueOptions(this);
        int from_bitField0_ = bitField0_;
        if (uninterpretedOptionBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.uninterpretedOption_ = uninterpretedOption_;
        } else {
          result.uninterpretedOption_ = uninterpretedOptionBuilder_.build();
        }
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.EnumValueOptions) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.EnumValueOptions)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.EnumValueOptions other) {
        if (other == com.google.protobuf.DescriptorProtos.EnumValueOptions.getDefaultInstance()) return this;
        if (uninterpretedOptionBuilder_ == null) {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOption_.isEmpty()) {
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureUninterpretedOptionIsMutable();
              uninterpretedOption_.addAll(other.uninterpretedOption_);
            }
            onChanged();
          }
        } else {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOptionBuilder_.isEmpty()) {
              uninterpretedOptionBuilder_.dispose();
              uninterpretedOptionBuilder_ = null;
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000001);
              uninterpretedOptionBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getUninterpretedOptionFieldBuilder() : null;
            } else {
              uninterpretedOptionBuilder_.addAllMessages(other.uninterpretedOption_);
            }
          }
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getUninterpretedOptionCount(); i++) {
          if (!getUninterpretedOption(i).isInitialized()) {

            return false;
          }
        }
        if (!extensionsAreInitialized()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.EnumValueOptions parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.EnumValueOptions) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_ =
        java.util.Collections.emptyList();
      private void ensureUninterpretedOptionIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>(uninterpretedOption_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
        if (uninterpretedOptionBuilder_ == null) {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        } else {
          return uninterpretedOptionBuilder_.getMessageList();
        }
      }

      public int getUninterpretedOptionCount() {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.size();
        } else {
          return uninterpretedOptionBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);
        } else {
          return uninterpretedOptionBuilder_.getMessage(index);
        }
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllUninterpretedOption(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption> values) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          super.addAll(values, uninterpretedOption_);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearUninterpretedOption() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder removeUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.remove(index);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
          int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);  } else {
          return uninterpretedOptionBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
           getUninterpretedOptionOrBuilderList() {
        if (uninterpretedOptionBuilder_ != null) {
          return uninterpretedOptionBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder> 
           getUninterpretedOptionBuilderList() {
        return getUninterpretedOptionFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
          getUninterpretedOptionFieldBuilder() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOptionBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder>(
                  uninterpretedOption_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          uninterpretedOption_ = null;
        }
        return uninterpretedOptionBuilder_;
      }

    }

    static {
      defaultInstance = new EnumValueOptions(true);
      defaultInstance.initFields();
    }

  }

  public interface ServiceOptionsOrBuilder extends
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<ServiceOptions> {

    java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> 
        getUninterpretedOptionList();

    com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index);

    int getUninterpretedOptionCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList();

    com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index);
  }

  public static final class ServiceOptions extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        ServiceOptions> implements ServiceOptionsOrBuilder {

    private ServiceOptions(com.google.protobuf.GeneratedMessage.ExtendableBuilder<com.google.protobuf.DescriptorProtos.ServiceOptions, ?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private ServiceOptions(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final ServiceOptions defaultInstance;
    public static ServiceOptions getDefaultInstance() {
      return defaultInstance;
    }

    public ServiceOptions getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private ServiceOptions(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 7994: {
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>();
                mutable_bitField0_ |= 0x00000001;
              }
              uninterpretedOption_.add(input.readMessage(com.google.protobuf.DescriptorProtos.UninterpretedOption.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceOptions_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.ServiceOptions.class, com.google.protobuf.DescriptorProtos.ServiceOptions.Builder.class);
    }

    public static com.google.protobuf.Parser<ServiceOptions> PARSER =
        new com.google.protobuf.AbstractParser<ServiceOptions>() {
      public ServiceOptions parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new ServiceOptions(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<ServiceOptions> getParserForType() {
      return PARSER;
    }

    public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
    private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_;

    public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
      return uninterpretedOption_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList() {
      return uninterpretedOption_;
    }

    public int getUninterpretedOptionCount() {
      return uninterpretedOption_.size();
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
      return uninterpretedOption_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index) {
      return uninterpretedOption_.get(index);
    }

    private void initFields() {
      uninterpretedOption_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getUninterpretedOptionCount(); i++) {
        if (!getUninterpretedOption(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (!extensionsAreInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      com.google.protobuf.GeneratedMessage
        .ExtendableMessage<com.google.protobuf.DescriptorProtos.ServiceOptions>.ExtensionWriter extensionWriter =
          newExtensionWriter();
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        output.writeMessage(999, uninterpretedOption_.get(i));
      }
      extensionWriter.writeUntil(536870912, output);
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(999, uninterpretedOption_.get(i));
      }
      size += extensionsSerializedSize();
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.ServiceOptions parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.ServiceOptions prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.ExtendableBuilder<
          com.google.protobuf.DescriptorProtos.ServiceOptions, Builder> implements com.google.protobuf.DescriptorProtos.ServiceOptionsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceOptions_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.ServiceOptions.class, com.google.protobuf.DescriptorProtos.ServiceOptions.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getUninterpretedOptionFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_ServiceOptions_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.ServiceOptions getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.ServiceOptions.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.ServiceOptions build() {
        com.google.protobuf.DescriptorProtos.ServiceOptions result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.ServiceOptions buildPartial() {
        com.google.protobuf.DescriptorProtos.ServiceOptions result = new com.google.protobuf.DescriptorProtos.ServiceOptions(this);
        int from_bitField0_ = bitField0_;
        if (uninterpretedOptionBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.uninterpretedOption_ = uninterpretedOption_;
        } else {
          result.uninterpretedOption_ = uninterpretedOptionBuilder_.build();
        }
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.ServiceOptions) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.ServiceOptions)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.ServiceOptions other) {
        if (other == com.google.protobuf.DescriptorProtos.ServiceOptions.getDefaultInstance()) return this;
        if (uninterpretedOptionBuilder_ == null) {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOption_.isEmpty()) {
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureUninterpretedOptionIsMutable();
              uninterpretedOption_.addAll(other.uninterpretedOption_);
            }
            onChanged();
          }
        } else {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOptionBuilder_.isEmpty()) {
              uninterpretedOptionBuilder_.dispose();
              uninterpretedOptionBuilder_ = null;
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000001);
              uninterpretedOptionBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getUninterpretedOptionFieldBuilder() : null;
            } else {
              uninterpretedOptionBuilder_.addAllMessages(other.uninterpretedOption_);
            }
          }
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getUninterpretedOptionCount(); i++) {
          if (!getUninterpretedOption(i).isInitialized()) {

            return false;
          }
        }
        if (!extensionsAreInitialized()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.ServiceOptions parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.ServiceOptions) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_ =
        java.util.Collections.emptyList();
      private void ensureUninterpretedOptionIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>(uninterpretedOption_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
        if (uninterpretedOptionBuilder_ == null) {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        } else {
          return uninterpretedOptionBuilder_.getMessageList();
        }
      }

      public int getUninterpretedOptionCount() {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.size();
        } else {
          return uninterpretedOptionBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);
        } else {
          return uninterpretedOptionBuilder_.getMessage(index);
        }
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllUninterpretedOption(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption> values) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          super.addAll(values, uninterpretedOption_);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearUninterpretedOption() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder removeUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.remove(index);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
          int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);  } else {
          return uninterpretedOptionBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
           getUninterpretedOptionOrBuilderList() {
        if (uninterpretedOptionBuilder_ != null) {
          return uninterpretedOptionBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder> 
           getUninterpretedOptionBuilderList() {
        return getUninterpretedOptionFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
          getUninterpretedOptionFieldBuilder() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOptionBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder>(
                  uninterpretedOption_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          uninterpretedOption_ = null;
        }
        return uninterpretedOptionBuilder_;
      }

    }

    static {
      defaultInstance = new ServiceOptions(true);
      defaultInstance.initFields();
    }

  }

  public interface MethodOptionsOrBuilder extends
      com.google.protobuf.GeneratedMessage.
          ExtendableMessageOrBuilder<MethodOptions> {

    java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> 
        getUninterpretedOptionList();

    com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index);

    int getUninterpretedOptionCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList();

    com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index);
  }

  public static final class MethodOptions extends
      com.google.protobuf.GeneratedMessage.ExtendableMessage<
        MethodOptions> implements MethodOptionsOrBuilder {

    private MethodOptions(com.google.protobuf.GeneratedMessage.ExtendableBuilder<com.google.protobuf.DescriptorProtos.MethodOptions, ?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private MethodOptions(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final MethodOptions defaultInstance;
    public static MethodOptions getDefaultInstance() {
      return defaultInstance;
    }

    public MethodOptions getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private MethodOptions(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 7994: {
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>();
                mutable_bitField0_ |= 0x00000001;
              }
              uninterpretedOption_.add(input.readMessage(com.google.protobuf.DescriptorProtos.UninterpretedOption.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodOptions_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.MethodOptions.class, com.google.protobuf.DescriptorProtos.MethodOptions.Builder.class);
    }

    public static com.google.protobuf.Parser<MethodOptions> PARSER =
        new com.google.protobuf.AbstractParser<MethodOptions>() {
      public MethodOptions parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new MethodOptions(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<MethodOptions> getParserForType() {
      return PARSER;
    }

    public static final int UNINTERPRETED_OPTION_FIELD_NUMBER = 999;
    private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_;

    public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
      return uninterpretedOption_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
        getUninterpretedOptionOrBuilderList() {
      return uninterpretedOption_;
    }

    public int getUninterpretedOptionCount() {
      return uninterpretedOption_.size();
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
      return uninterpretedOption_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
        int index) {
      return uninterpretedOption_.get(index);
    }

    private void initFields() {
      uninterpretedOption_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getUninterpretedOptionCount(); i++) {
        if (!getUninterpretedOption(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      if (!extensionsAreInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      com.google.protobuf.GeneratedMessage
        .ExtendableMessage<com.google.protobuf.DescriptorProtos.MethodOptions>.ExtensionWriter extensionWriter =
          newExtensionWriter();
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        output.writeMessage(999, uninterpretedOption_.get(i));
      }
      extensionWriter.writeUntil(536870912, output);
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < uninterpretedOption_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(999, uninterpretedOption_.get(i));
      }
      size += extensionsSerializedSize();
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.MethodOptions parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.MethodOptions parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MethodOptions parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.MethodOptions parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MethodOptions parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.MethodOptions parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MethodOptions parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.MethodOptions parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.MethodOptions parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.MethodOptions parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.MethodOptions prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.ExtendableBuilder<
          com.google.protobuf.DescriptorProtos.MethodOptions, Builder> implements com.google.protobuf.DescriptorProtos.MethodOptionsOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodOptions_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.MethodOptions.class, com.google.protobuf.DescriptorProtos.MethodOptions.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getUninterpretedOptionFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_MethodOptions_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.MethodOptions getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.MethodOptions.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.MethodOptions build() {
        com.google.protobuf.DescriptorProtos.MethodOptions result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.MethodOptions buildPartial() {
        com.google.protobuf.DescriptorProtos.MethodOptions result = new com.google.protobuf.DescriptorProtos.MethodOptions(this);
        int from_bitField0_ = bitField0_;
        if (uninterpretedOptionBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            uninterpretedOption_ = java.util.Collections.unmodifiableList(uninterpretedOption_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.uninterpretedOption_ = uninterpretedOption_;
        } else {
          result.uninterpretedOption_ = uninterpretedOptionBuilder_.build();
        }
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.MethodOptions) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.MethodOptions)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.MethodOptions other) {
        if (other == com.google.protobuf.DescriptorProtos.MethodOptions.getDefaultInstance()) return this;
        if (uninterpretedOptionBuilder_ == null) {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOption_.isEmpty()) {
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureUninterpretedOptionIsMutable();
              uninterpretedOption_.addAll(other.uninterpretedOption_);
            }
            onChanged();
          }
        } else {
          if (!other.uninterpretedOption_.isEmpty()) {
            if (uninterpretedOptionBuilder_.isEmpty()) {
              uninterpretedOptionBuilder_.dispose();
              uninterpretedOptionBuilder_ = null;
              uninterpretedOption_ = other.uninterpretedOption_;
              bitField0_ = (bitField0_ & ~0x00000001);
              uninterpretedOptionBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getUninterpretedOptionFieldBuilder() : null;
            } else {
              uninterpretedOptionBuilder_.addAllMessages(other.uninterpretedOption_);
            }
          }
        }
        this.mergeExtensionFields(other);
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getUninterpretedOptionCount(); i++) {
          if (!getUninterpretedOption(i).isInitialized()) {

            return false;
          }
        }
        if (!extensionsAreInitialized()) {

          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.MethodOptions parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.MethodOptions) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> uninterpretedOption_ =
        java.util.Collections.emptyList();
      private void ensureUninterpretedOptionIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          uninterpretedOption_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption>(uninterpretedOption_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> uninterpretedOptionBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption> getUninterpretedOptionList() {
        if (uninterpretedOptionBuilder_ == null) {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        } else {
          return uninterpretedOptionBuilder_.getMessageList();
        }
      }

      public int getUninterpretedOptionCount() {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.size();
        } else {
          return uninterpretedOptionBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption getUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);
        } else {
          return uninterpretedOptionBuilder_.getMessage(index);
        }
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.set(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption value) {
        if (uninterpretedOptionBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, value);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addUninterpretedOption(
          com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addUninterpretedOption(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder builderForValue) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.add(index, builderForValue.build());
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllUninterpretedOption(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption> values) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          super.addAll(values, uninterpretedOption_);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearUninterpretedOption() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOption_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.clear();
        }
        return this;
      }

      public Builder removeUninterpretedOption(int index) {
        if (uninterpretedOptionBuilder_ == null) {
          ensureUninterpretedOptionIsMutable();
          uninterpretedOption_.remove(index);
          onChanged();
        } else {
          uninterpretedOptionBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder getUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder getUninterpretedOptionOrBuilder(
          int index) {
        if (uninterpretedOptionBuilder_ == null) {
          return uninterpretedOption_.get(index);  } else {
          return uninterpretedOptionBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
           getUninterpretedOptionOrBuilderList() {
        if (uninterpretedOptionBuilder_ != null) {
          return uninterpretedOptionBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(uninterpretedOption_);
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder() {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder addUninterpretedOptionBuilder(
          int index) {
        return getUninterpretedOptionFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder> 
           getUninterpretedOptionBuilderList() {
        return getUninterpretedOptionFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder> 
          getUninterpretedOptionFieldBuilder() {
        if (uninterpretedOptionBuilder_ == null) {
          uninterpretedOptionBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.UninterpretedOption, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder>(
                  uninterpretedOption_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          uninterpretedOption_ = null;
        }
        return uninterpretedOptionBuilder_;
      }

    }

    static {
      defaultInstance = new MethodOptions(true);
      defaultInstance.initFields();
    }

  }

  public interface UninterpretedOptionOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart> 
        getNameList();

    com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart getName(int index);

    int getNameCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder> 
        getNameOrBuilderList();

    com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder getNameOrBuilder(
        int index);

    boolean hasIdentifierValue();

    java.lang.String getIdentifierValue();

    com.google.protobuf.ByteString
        getIdentifierValueBytes();

    boolean hasPositiveIntValue();

    long getPositiveIntValue();

    boolean hasNegativeIntValue();

    long getNegativeIntValue();

    boolean hasDoubleValue();

    double getDoubleValue();

    boolean hasStringValue();

    com.google.protobuf.ByteString getStringValue();

    boolean hasAggregateValue();

    java.lang.String getAggregateValue();

    com.google.protobuf.ByteString
        getAggregateValueBytes();
  }

  public static final class UninterpretedOption extends
      com.google.protobuf.GeneratedMessage
      implements UninterpretedOptionOrBuilder {

    private UninterpretedOption(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private UninterpretedOption(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final UninterpretedOption defaultInstance;
    public static UninterpretedOption getDefaultInstance() {
      return defaultInstance;
    }

    public UninterpretedOption getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private UninterpretedOption(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 18: {
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                name_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart>();
                mutable_bitField0_ |= 0x00000001;
              }
              name_.add(input.readMessage(com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.PARSER, extensionRegistry));
              break;
            }
            case 26: {
              bitField0_ |= 0x00000001;
              identifierValue_ = input.readBytes();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000002;
              positiveIntValue_ = input.readUInt64();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000004;
              negativeIntValue_ = input.readInt64();
              break;
            }
            case 49: {
              bitField0_ |= 0x00000008;
              doubleValue_ = input.readDouble();
              break;
            }
            case 58: {
              bitField0_ |= 0x00000010;
              stringValue_ = input.readBytes();
              break;
            }
            case 66: {
              bitField0_ |= 0x00000020;
              aggregateValue_ = input.readBytes();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          name_ = java.util.Collections.unmodifiableList(name_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.UninterpretedOption.class, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder.class);
    }

    public static com.google.protobuf.Parser<UninterpretedOption> PARSER =
        new com.google.protobuf.AbstractParser<UninterpretedOption>() {
      public UninterpretedOption parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new UninterpretedOption(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<UninterpretedOption> getParserForType() {
      return PARSER;
    }

    public interface NamePartOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      boolean hasNamePart();

      java.lang.String getNamePart();

      com.google.protobuf.ByteString
          getNamePartBytes();

      boolean hasIsExtension();

      boolean getIsExtension();
    }

    public static final class NamePart extends
        com.google.protobuf.GeneratedMessage
        implements NamePartOrBuilder {

      private NamePart(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private NamePart(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final NamePart defaultInstance;
      public static NamePart getDefaultInstance() {
        return defaultInstance;
      }

      public NamePart getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private NamePart(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        initFields();
        int mutable_bitField0_ = 0;
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
            com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              default: {
                if (!parseUnknownField(input, unknownFields,
                                       extensionRegistry, tag)) {
                  done = true;
                }
                break;
              }
              case 10: {
                bitField0_ |= 0x00000001;
                namePart_ = input.readBytes();
                break;
              }
              case 16: {
                bitField0_ |= 0x00000002;
                isExtension_ = input.readBool();
                break;
              }
            }
          }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this);
        } finally {
          this.unknownFields = unknownFields.build();
          makeExtensionsImmutable();
        }
      }
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.class, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder.class);
      }

      public static com.google.protobuf.Parser<NamePart> PARSER =
          new com.google.protobuf.AbstractParser<NamePart>() {
        public NamePart parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new NamePart(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<NamePart> getParserForType() {
        return PARSER;
      }

      private int bitField0_;

      public static final int NAME_PART_FIELD_NUMBER = 1;
      private java.lang.Object namePart_;

      public boolean hasNamePart() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getNamePart() {
        java.lang.Object ref = namePart_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            namePart_ = s;
          }
          return s;
        }
      }

      public com.google.protobuf.ByteString
          getNamePartBytes() {
        java.lang.Object ref = namePart_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          namePart_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public static final int IS_EXTENSION_FIELD_NUMBER = 2;
      private boolean isExtension_;

      public boolean hasIsExtension() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public boolean getIsExtension() {
        return isExtension_;
      }

      private void initFields() {
        namePart_ = "";
        isExtension_ = false;
      }
      private byte memoizedIsInitialized = -1;
      public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized != -1) return isInitialized == 1;

        if (!hasNamePart()) {
          memoizedIsInitialized = 0;
          return false;
        }
        if (!hasIsExtension()) {
          memoizedIsInitialized = 0;
          return false;
        }
        memoizedIsInitialized = 1;
        return true;
      }

      public void writeTo(com.google.protobuf.CodedOutputStream output)
                          throws java.io.IOException {
        getSerializedSize();
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          output.writeBytes(1, getNamePartBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeBool(2, isExtension_);
        }
        getUnknownFields().writeTo(output);
      }

      private int memoizedSerializedSize = -1;
      public int getSerializedSize() {
        int size = memoizedSerializedSize;
        if (size != -1) return size;

        size = 0;
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(1, getNamePartBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBoolSize(2, isExtension_);
        }
        size += getUnknownFields().getSerializedSize();
        memoizedSerializedSize = size;
        return size;
      }

      private static final long serialVersionUID = 0L;
      @java.lang.Override
      protected java.lang.Object writeReplace()
          throws java.io.ObjectStreamException {
        return super.writeReplace();
      }

      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart prototype) {
        return newBuilder().mergeFrom(prototype);
      }
      public Builder toBuilder() { return newBuilder(this); }

      @java.lang.Override
      protected Builder newBuilderForType(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
      }

      public static final class Builder extends
          com.google.protobuf.GeneratedMessage.Builder<Builder>
         implements com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.class, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder.class);
        }

        private Builder() {
          maybeForceBuilderInitialization();
        }

        private Builder(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
          super(parent);
          maybeForceBuilderInitialization();
        }
        private void maybeForceBuilderInitialization() {
          if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          }
        }
        private static Builder create() {
          return new Builder();
        }

        public Builder clear() {
          super.clear();
          namePart_ = "";
          bitField0_ = (bitField0_ & ~0x00000001);
          isExtension_ = false;
          bitField0_ = (bitField0_ & ~0x00000002);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
        }

        public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart getDefaultInstanceForType() {
          return com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance();
        }

        public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart build() {
          com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart buildPartial() {
          com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart result = new com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
            to_bitField0_ |= 0x00000001;
          }
          result.namePart_ = namePart_;
          if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
            to_bitField0_ |= 0x00000002;
          }
          result.isExtension_ = isExtension_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart) {
            return mergeFrom((com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart other) {
          if (other == com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance()) return this;
          if (other.hasNamePart()) {
            bitField0_ |= 0x00000001;
            namePart_ = other.namePart_;
            onChanged();
          }
          if (other.hasIsExtension()) {
            setIsExtension(other.getIsExtension());
          }
          this.mergeUnknownFields(other.getUnknownFields());
          return this;
        }

        public final boolean isInitialized() {
          if (!hasNamePart()) {

            return false;
          }
          if (!hasIsExtension()) {

            return false;
          }
          return true;
        }

        public Builder mergeFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
          com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private java.lang.Object namePart_ = "";

        public boolean hasNamePart() {
          return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        public java.lang.String getNamePart() {
          java.lang.Object ref = namePart_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            namePart_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }

        public com.google.protobuf.ByteString
            getNamePartBytes() {
          java.lang.Object ref = namePart_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            namePart_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }

        public Builder setNamePart(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          namePart_ = value;
          onChanged();
          return this;
        }

        public Builder clearNamePart() {
          bitField0_ = (bitField0_ & ~0x00000001);
          namePart_ = getDefaultInstance().getNamePart();
          onChanged();
          return this;
        }

        public Builder setNamePartBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
          namePart_ = value;
          onChanged();
          return this;
        }

        private boolean isExtension_ ;

        public boolean hasIsExtension() {
          return ((bitField0_ & 0x00000002) == 0x00000002);
        }

        public boolean getIsExtension() {
          return isExtension_;
        }

        public Builder setIsExtension(boolean value) {
          bitField0_ |= 0x00000002;
          isExtension_ = value;
          onChanged();
          return this;
        }

        public Builder clearIsExtension() {
          bitField0_ = (bitField0_ & ~0x00000002);
          isExtension_ = false;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new NamePart(true);
        defaultInstance.initFields();
      }

    }

    private int bitField0_;

    public static final int NAME_FIELD_NUMBER = 2;
    private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart> name_;

    public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart> getNameList() {
      return name_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder> 
        getNameOrBuilderList() {
      return name_;
    }

    public int getNameCount() {
      return name_.size();
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart getName(int index) {
      return name_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder getNameOrBuilder(
        int index) {
      return name_.get(index);
    }

    public static final int IDENTIFIER_VALUE_FIELD_NUMBER = 3;
    private java.lang.Object identifierValue_;

    public boolean hasIdentifierValue() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }

    public java.lang.String getIdentifierValue() {
      java.lang.Object ref = identifierValue_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          identifierValue_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getIdentifierValueBytes() {
      java.lang.Object ref = identifierValue_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        identifierValue_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    public static final int POSITIVE_INT_VALUE_FIELD_NUMBER = 4;
    private long positiveIntValue_;

    public boolean hasPositiveIntValue() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }

    public long getPositiveIntValue() {
      return positiveIntValue_;
    }

    public static final int NEGATIVE_INT_VALUE_FIELD_NUMBER = 5;
    private long negativeIntValue_;

    public boolean hasNegativeIntValue() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }

    public long getNegativeIntValue() {
      return negativeIntValue_;
    }

    public static final int DOUBLE_VALUE_FIELD_NUMBER = 6;
    private double doubleValue_;

    public boolean hasDoubleValue() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }

    public double getDoubleValue() {
      return doubleValue_;
    }

    public static final int STRING_VALUE_FIELD_NUMBER = 7;
    private com.google.protobuf.ByteString stringValue_;

    public boolean hasStringValue() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }

    public com.google.protobuf.ByteString getStringValue() {
      return stringValue_;
    }

    public static final int AGGREGATE_VALUE_FIELD_NUMBER = 8;
    private java.lang.Object aggregateValue_;

    public boolean hasAggregateValue() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }

    public java.lang.String getAggregateValue() {
      java.lang.Object ref = aggregateValue_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        if (bs.isValidUtf8()) {
          aggregateValue_ = s;
        }
        return s;
      }
    }

    public com.google.protobuf.ByteString
        getAggregateValueBytes() {
      java.lang.Object ref = aggregateValue_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        aggregateValue_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private void initFields() {
      name_ = java.util.Collections.emptyList();
      identifierValue_ = "";
      positiveIntValue_ = 0L;
      negativeIntValue_ = 0L;
      doubleValue_ = 0D;
      stringValue_ = com.google.protobuf.ByteString.EMPTY;
      aggregateValue_ = "";
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      for (int i = 0; i < getNameCount(); i++) {
        if (!getName(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (int i = 0; i < name_.size(); i++) {
        output.writeMessage(2, name_.get(i));
      }
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(3, getIdentifierValueBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeUInt64(4, positiveIntValue_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt64(5, negativeIntValue_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeDouble(6, doubleValue_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(7, stringValue_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(8, getAggregateValueBytes());
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < name_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, name_.get(i));
      }
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getIdentifierValueBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(4, positiveIntValue_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(5, negativeIntValue_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeDoubleSize(6, doubleValue_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, stringValue_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(8, getAggregateValueBytes());
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.UninterpretedOption parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.UninterpretedOption prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.UninterpretedOptionOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.UninterpretedOption.class, com.google.protobuf.DescriptorProtos.UninterpretedOption.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getNameFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (nameBuilder_ == null) {
          name_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          nameBuilder_.clear();
        }
        identifierValue_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        positiveIntValue_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000004);
        negativeIntValue_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000008);
        doubleValue_ = 0D;
        bitField0_ = (bitField0_ & ~0x00000010);
        stringValue_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000020);
        aggregateValue_ = "";
        bitField0_ = (bitField0_ & ~0x00000040);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_UninterpretedOption_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption build() {
        com.google.protobuf.DescriptorProtos.UninterpretedOption result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption buildPartial() {
        com.google.protobuf.DescriptorProtos.UninterpretedOption result = new com.google.protobuf.DescriptorProtos.UninterpretedOption(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (nameBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            name_ = java.util.Collections.unmodifiableList(name_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.name_ = name_;
        } else {
          result.name_ = nameBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000001;
        }
        result.identifierValue_ = identifierValue_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000002;
        }
        result.positiveIntValue_ = positiveIntValue_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000004;
        }
        result.negativeIntValue_ = negativeIntValue_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000008;
        }
        result.doubleValue_ = doubleValue_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000010;
        }
        result.stringValue_ = stringValue_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000020;
        }
        result.aggregateValue_ = aggregateValue_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.UninterpretedOption) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.UninterpretedOption)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.UninterpretedOption other) {
        if (other == com.google.protobuf.DescriptorProtos.UninterpretedOption.getDefaultInstance()) return this;
        if (nameBuilder_ == null) {
          if (!other.name_.isEmpty()) {
            if (name_.isEmpty()) {
              name_ = other.name_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureNameIsMutable();
              name_.addAll(other.name_);
            }
            onChanged();
          }
        } else {
          if (!other.name_.isEmpty()) {
            if (nameBuilder_.isEmpty()) {
              nameBuilder_.dispose();
              nameBuilder_ = null;
              name_ = other.name_;
              bitField0_ = (bitField0_ & ~0x00000001);
              nameBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getNameFieldBuilder() : null;
            } else {
              nameBuilder_.addAllMessages(other.name_);
            }
          }
        }
        if (other.hasIdentifierValue()) {
          bitField0_ |= 0x00000002;
          identifierValue_ = other.identifierValue_;
          onChanged();
        }
        if (other.hasPositiveIntValue()) {
          setPositiveIntValue(other.getPositiveIntValue());
        }
        if (other.hasNegativeIntValue()) {
          setNegativeIntValue(other.getNegativeIntValue());
        }
        if (other.hasDoubleValue()) {
          setDoubleValue(other.getDoubleValue());
        }
        if (other.hasStringValue()) {
          setStringValue(other.getStringValue());
        }
        if (other.hasAggregateValue()) {
          bitField0_ |= 0x00000040;
          aggregateValue_ = other.aggregateValue_;
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        for (int i = 0; i < getNameCount(); i++) {
          if (!getName(i).isInitialized()) {

            return false;
          }
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.UninterpretedOption parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.UninterpretedOption) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart> name_ =
        java.util.Collections.emptyList();
      private void ensureNameIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          name_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart>(name_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder> nameBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart> getNameList() {
        if (nameBuilder_ == null) {
          return java.util.Collections.unmodifiableList(name_);
        } else {
          return nameBuilder_.getMessageList();
        }
      }

      public int getNameCount() {
        if (nameBuilder_ == null) {
          return name_.size();
        } else {
          return nameBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart getName(int index) {
        if (nameBuilder_ == null) {
          return name_.get(index);
        } else {
          return nameBuilder_.getMessage(index);
        }
      }

      public Builder setName(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart value) {
        if (nameBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureNameIsMutable();
          name_.set(index, value);
          onChanged();
        } else {
          nameBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setName(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder builderForValue) {
        if (nameBuilder_ == null) {
          ensureNameIsMutable();
          name_.set(index, builderForValue.build());
          onChanged();
        } else {
          nameBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addName(com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart value) {
        if (nameBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureNameIsMutable();
          name_.add(value);
          onChanged();
        } else {
          nameBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addName(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart value) {
        if (nameBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureNameIsMutable();
          name_.add(index, value);
          onChanged();
        } else {
          nameBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addName(
          com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder builderForValue) {
        if (nameBuilder_ == null) {
          ensureNameIsMutable();
          name_.add(builderForValue.build());
          onChanged();
        } else {
          nameBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addName(
          int index, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder builderForValue) {
        if (nameBuilder_ == null) {
          ensureNameIsMutable();
          name_.add(index, builderForValue.build());
          onChanged();
        } else {
          nameBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllName(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart> values) {
        if (nameBuilder_ == null) {
          ensureNameIsMutable();
          super.addAll(values, name_);
          onChanged();
        } else {
          nameBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearName() {
        if (nameBuilder_ == null) {
          name_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          nameBuilder_.clear();
        }
        return this;
      }

      public Builder removeName(int index) {
        if (nameBuilder_ == null) {
          ensureNameIsMutable();
          name_.remove(index);
          onChanged();
        } else {
          nameBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder getNameBuilder(
          int index) {
        return getNameFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder getNameOrBuilder(
          int index) {
        if (nameBuilder_ == null) {
          return name_.get(index);  } else {
          return nameBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder> 
           getNameOrBuilderList() {
        if (nameBuilder_ != null) {
          return nameBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(name_);
        }
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder addNameBuilder() {
        return getNameFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder addNameBuilder(
          int index) {
        return getNameFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder> 
           getNameBuilderList() {
        return getNameFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder> 
          getNameFieldBuilder() {
        if (nameBuilder_ == null) {
          nameBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePart.Builder, com.google.protobuf.DescriptorProtos.UninterpretedOption.NamePartOrBuilder>(
                  name_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          name_ = null;
        }
        return nameBuilder_;
      }

      private java.lang.Object identifierValue_ = "";

      public boolean hasIdentifierValue() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public java.lang.String getIdentifierValue() {
        java.lang.Object ref = identifierValue_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          identifierValue_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getIdentifierValueBytes() {
        java.lang.Object ref = identifierValue_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          identifierValue_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setIdentifierValue(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        identifierValue_ = value;
        onChanged();
        return this;
      }

      public Builder clearIdentifierValue() {
        bitField0_ = (bitField0_ & ~0x00000002);
        identifierValue_ = getDefaultInstance().getIdentifierValue();
        onChanged();
        return this;
      }

      public Builder setIdentifierValueBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        identifierValue_ = value;
        onChanged();
        return this;
      }

      private long positiveIntValue_ ;

      public boolean hasPositiveIntValue() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }

      public long getPositiveIntValue() {
        return positiveIntValue_;
      }

      public Builder setPositiveIntValue(long value) {
        bitField0_ |= 0x00000004;
        positiveIntValue_ = value;
        onChanged();
        return this;
      }

      public Builder clearPositiveIntValue() {
        bitField0_ = (bitField0_ & ~0x00000004);
        positiveIntValue_ = 0L;
        onChanged();
        return this;
      }

      private long negativeIntValue_ ;

      public boolean hasNegativeIntValue() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }

      public long getNegativeIntValue() {
        return negativeIntValue_;
      }

      public Builder setNegativeIntValue(long value) {
        bitField0_ |= 0x00000008;
        negativeIntValue_ = value;
        onChanged();
        return this;
      }

      public Builder clearNegativeIntValue() {
        bitField0_ = (bitField0_ & ~0x00000008);
        negativeIntValue_ = 0L;
        onChanged();
        return this;
      }

      private double doubleValue_ ;

      public boolean hasDoubleValue() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }

      public double getDoubleValue() {
        return doubleValue_;
      }

      public Builder setDoubleValue(double value) {
        bitField0_ |= 0x00000010;
        doubleValue_ = value;
        onChanged();
        return this;
      }

      public Builder clearDoubleValue() {
        bitField0_ = (bitField0_ & ~0x00000010);
        doubleValue_ = 0D;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString stringValue_ = com.google.protobuf.ByteString.EMPTY;

      public boolean hasStringValue() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }

      public com.google.protobuf.ByteString getStringValue() {
        return stringValue_;
      }

      public Builder setStringValue(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        stringValue_ = value;
        onChanged();
        return this;
      }

      public Builder clearStringValue() {
        bitField0_ = (bitField0_ & ~0x00000020);
        stringValue_ = getDefaultInstance().getStringValue();
        onChanged();
        return this;
      }

      private java.lang.Object aggregateValue_ = "";

      public boolean hasAggregateValue() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }

      public java.lang.String getAggregateValue() {
        java.lang.Object ref = aggregateValue_;
        if (!(ref instanceof java.lang.String)) {
          java.lang.String s = ((com.google.protobuf.ByteString) ref)
              .toStringUtf8();
          aggregateValue_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }

      public com.google.protobuf.ByteString
          getAggregateValueBytes() {
        java.lang.Object ref = aggregateValue_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          aggregateValue_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public Builder setAggregateValue(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        aggregateValue_ = value;
        onChanged();
        return this;
      }

      public Builder clearAggregateValue() {
        bitField0_ = (bitField0_ & ~0x00000040);
        aggregateValue_ = getDefaultInstance().getAggregateValue();
        onChanged();
        return this;
      }

      public Builder setAggregateValueBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        aggregateValue_ = value;
        onChanged();
        return this;
      }

    }

    static {
      defaultInstance = new UninterpretedOption(true);
      defaultInstance.initFields();
    }

  }

  public interface SourceCodeInfoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    java.util.List<com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location> 
        getLocationList();

    com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location getLocation(int index);

    int getLocationCount();

    java.util.List<? extends com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder> 
        getLocationOrBuilderList();

    com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder getLocationOrBuilder(
        int index);
  }

  public static final class SourceCodeInfo extends
      com.google.protobuf.GeneratedMessage
      implements SourceCodeInfoOrBuilder {

    private SourceCodeInfo(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private SourceCodeInfo(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final SourceCodeInfo defaultInstance;
    public static SourceCodeInfo getDefaultInstance() {
      return defaultInstance;
    }

    public SourceCodeInfo getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private SourceCodeInfo(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                location_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location>();
                mutable_bitField0_ |= 0x00000001;
              }
              location_.add(input.readMessage(com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.PARSER, extensionRegistry));
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
          location_ = java.util.Collections.unmodifiableList(location_);
        }
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.google.protobuf.DescriptorProtos.SourceCodeInfo.class, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Builder.class);
    }

    public static com.google.protobuf.Parser<SourceCodeInfo> PARSER =
        new com.google.protobuf.AbstractParser<SourceCodeInfo>() {
      public SourceCodeInfo parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new SourceCodeInfo(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<SourceCodeInfo> getParserForType() {
      return PARSER;
    }

    public interface LocationOrBuilder
        extends com.google.protobuf.MessageOrBuilder {

      java.util.List<java.lang.Integer> getPathList();

      int getPathCount();

      int getPath(int index);

      java.util.List<java.lang.Integer> getSpanList();

      int getSpanCount();

      int getSpan(int index);

      boolean hasLeadingComments();

      java.lang.String getLeadingComments();

      com.google.protobuf.ByteString
          getLeadingCommentsBytes();

      boolean hasTrailingComments();

      java.lang.String getTrailingComments();

      com.google.protobuf.ByteString
          getTrailingCommentsBytes();
    }

    public static final class Location extends
        com.google.protobuf.GeneratedMessage
        implements LocationOrBuilder {

      private Location(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
        super(builder);
        this.unknownFields = builder.getUnknownFields();
      }
      private Location(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

      private static final Location defaultInstance;
      public static Location getDefaultInstance() {
        return defaultInstance;
      }

      public Location getDefaultInstanceForType() {
        return defaultInstance;
      }

      private final com.google.protobuf.UnknownFieldSet unknownFields;
      @java.lang.Override
      public final com.google.protobuf.UnknownFieldSet
          getUnknownFields() {
        return this.unknownFields;
      }
      private Location(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        initFields();
        int mutable_bitField0_ = 0;
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
            com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
          boolean done = false;
          while (!done) {
            int tag = input.readTag();
            switch (tag) {
              case 0:
                done = true;
                break;
              default: {
                if (!parseUnknownField(input, unknownFields,
                                       extensionRegistry, tag)) {
                  done = true;
                }
                break;
              }
              case 8: {
                if (!((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
                  path_ = new java.util.ArrayList<java.lang.Integer>();
                  mutable_bitField0_ |= 0x00000001;
                }
                path_.add(input.readInt32());
                break;
              }
              case 10: {
                int length = input.readRawVarint32();
                int limit = input.pushLimit(length);
                if (!((mutable_bitField0_ & 0x00000001) == 0x00000001) && input.getBytesUntilLimit() > 0) {
                  path_ = new java.util.ArrayList<java.lang.Integer>();
                  mutable_bitField0_ |= 0x00000001;
                }
                while (input.getBytesUntilLimit() > 0) {
                  path_.add(input.readInt32());
                }
                input.popLimit(limit);
                break;
              }
              case 16: {
                if (!((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
                  span_ = new java.util.ArrayList<java.lang.Integer>();
                  mutable_bitField0_ |= 0x00000002;
                }
                span_.add(input.readInt32());
                break;
              }
              case 18: {
                int length = input.readRawVarint32();
                int limit = input.pushLimit(length);
                if (!((mutable_bitField0_ & 0x00000002) == 0x00000002) && input.getBytesUntilLimit() > 0) {
                  span_ = new java.util.ArrayList<java.lang.Integer>();
                  mutable_bitField0_ |= 0x00000002;
                }
                while (input.getBytesUntilLimit() > 0) {
                  span_.add(input.readInt32());
                }
                input.popLimit(limit);
                break;
              }
              case 26: {
                bitField0_ |= 0x00000001;
                leadingComments_ = input.readBytes();
                break;
              }
              case 34: {
                bitField0_ |= 0x00000002;
                trailingComments_ = input.readBytes();
                break;
              }
            }
          }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
          throw new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this);
        } finally {
          if (((mutable_bitField0_ & 0x00000001) == 0x00000001)) {
            path_ = java.util.Collections.unmodifiableList(path_);
          }
          if (((mutable_bitField0_ & 0x00000002) == 0x00000002)) {
            span_ = java.util.Collections.unmodifiableList(span_);
          }
          this.unknownFields = unknownFields.build();
          makeExtensionsImmutable();
        }
      }
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.class, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder.class);
      }

      public static com.google.protobuf.Parser<Location> PARSER =
          new com.google.protobuf.AbstractParser<Location>() {
        public Location parsePartialFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws com.google.protobuf.InvalidProtocolBufferException {
          return new Location(input, extensionRegistry);
        }
      };

      @java.lang.Override
      public com.google.protobuf.Parser<Location> getParserForType() {
        return PARSER;
      }

      private int bitField0_;

      public static final int PATH_FIELD_NUMBER = 1;
      private java.util.List<java.lang.Integer> path_;

      public java.util.List<java.lang.Integer>
          getPathList() {
        return path_;
      }

      public int getPathCount() {
        return path_.size();
      }

      public int getPath(int index) {
        return path_.get(index);
      }
      private int pathMemoizedSerializedSize = -1;

      public static final int SPAN_FIELD_NUMBER = 2;
      private java.util.List<java.lang.Integer> span_;

      public java.util.List<java.lang.Integer>
          getSpanList() {
        return span_;
      }

      public int getSpanCount() {
        return span_.size();
      }

      public int getSpan(int index) {
        return span_.get(index);
      }
      private int spanMemoizedSerializedSize = -1;

      public static final int LEADING_COMMENTS_FIELD_NUMBER = 3;
      private java.lang.Object leadingComments_;

      public boolean hasLeadingComments() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }

      public java.lang.String getLeadingComments() {
        java.lang.Object ref = leadingComments_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            leadingComments_ = s;
          }
          return s;
        }
      }

      public com.google.protobuf.ByteString
          getLeadingCommentsBytes() {
        java.lang.Object ref = leadingComments_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          leadingComments_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      public static final int TRAILING_COMMENTS_FIELD_NUMBER = 4;
      private java.lang.Object trailingComments_;

      public boolean hasTrailingComments() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }

      public java.lang.String getTrailingComments() {
        java.lang.Object ref = trailingComments_;
        if (ref instanceof java.lang.String) {
          return (java.lang.String) ref;
        } else {
          com.google.protobuf.ByteString bs = 
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          if (bs.isValidUtf8()) {
            trailingComments_ = s;
          }
          return s;
        }
      }

      public com.google.protobuf.ByteString
          getTrailingCommentsBytes() {
        java.lang.Object ref = trailingComments_;
        if (ref instanceof java.lang.String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          trailingComments_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }

      private void initFields() {
        path_ = java.util.Collections.emptyList();
        span_ = java.util.Collections.emptyList();
        leadingComments_ = "";
        trailingComments_ = "";
      }
      private byte memoizedIsInitialized = -1;
      public final boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized != -1) return isInitialized == 1;

        memoizedIsInitialized = 1;
        return true;
      }

      public void writeTo(com.google.protobuf.CodedOutputStream output)
                          throws java.io.IOException {
        getSerializedSize();
        if (getPathList().size() > 0) {
          output.writeRawVarint32(10);
          output.writeRawVarint32(pathMemoizedSerializedSize);
        }
        for (int i = 0; i < path_.size(); i++) {
          output.writeInt32NoTag(path_.get(i));
        }
        if (getSpanList().size() > 0) {
          output.writeRawVarint32(18);
          output.writeRawVarint32(spanMemoizedSerializedSize);
        }
        for (int i = 0; i < span_.size(); i++) {
          output.writeInt32NoTag(span_.get(i));
        }
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          output.writeBytes(3, getLeadingCommentsBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          output.writeBytes(4, getTrailingCommentsBytes());
        }
        getUnknownFields().writeTo(output);
      }

      private int memoizedSerializedSize = -1;
      public int getSerializedSize() {
        int size = memoizedSerializedSize;
        if (size != -1) return size;

        size = 0;
        {
          int dataSize = 0;
          for (int i = 0; i < path_.size(); i++) {
            dataSize += com.google.protobuf.CodedOutputStream
              .computeInt32SizeNoTag(path_.get(i));
          }
          size += dataSize;
          if (!getPathList().isEmpty()) {
            size += 1;
            size += com.google.protobuf.CodedOutputStream
                .computeInt32SizeNoTag(dataSize);
          }
          pathMemoizedSerializedSize = dataSize;
        }
        {
          int dataSize = 0;
          for (int i = 0; i < span_.size(); i++) {
            dataSize += com.google.protobuf.CodedOutputStream
              .computeInt32SizeNoTag(span_.get(i));
          }
          size += dataSize;
          if (!getSpanList().isEmpty()) {
            size += 1;
            size += com.google.protobuf.CodedOutputStream
                .computeInt32SizeNoTag(dataSize);
          }
          spanMemoizedSerializedSize = dataSize;
        }
        if (((bitField0_ & 0x00000001) == 0x00000001)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(3, getLeadingCommentsBytes());
        }
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          size += com.google.protobuf.CodedOutputStream
            .computeBytesSize(4, getTrailingCommentsBytes());
        }
        size += getUnknownFields().getSerializedSize();
        memoizedSerializedSize = size;
        return size;
      }

      private static final long serialVersionUID = 0L;
      @java.lang.Override
      protected java.lang.Object writeReplace()
          throws java.io.ObjectStreamException {
        return super.writeReplace();
      }

      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseFrom(
          com.google.protobuf.ByteString data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseFrom(
          com.google.protobuf.ByteString data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseFrom(byte[] data)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
      }
      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseFrom(
          byte[] data,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseDelimitedFrom(java.io.InputStream input)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input);
      }
      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseDelimitedFrom(
          java.io.InputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseDelimitedFrom(input, extensionRegistry);
      }
      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseFrom(
          com.google.protobuf.CodedInputStream input)
          throws java.io.IOException {
        return PARSER.parseFrom(input);
      }
      public static com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parseFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        return PARSER.parseFrom(input, extensionRegistry);
      }

      public static Builder newBuilder() { return Builder.create(); }
      public Builder newBuilderForType() { return newBuilder(); }
      public static Builder newBuilder(com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location prototype) {
        return newBuilder().mergeFrom(prototype);
      }
      public Builder toBuilder() { return newBuilder(this); }

      @java.lang.Override
      protected Builder newBuilderForType(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
      }

      public static final class Builder extends
          com.google.protobuf.GeneratedMessage.Builder<Builder>
         implements com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder {
        public static final com.google.protobuf.Descriptors.Descriptor
            getDescriptor() {
          return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
          return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable
              .ensureFieldAccessorsInitialized(
                  com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.class, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder.class);
        }

        private Builder() {
          maybeForceBuilderInitialization();
        }

        private Builder(
            com.google.protobuf.GeneratedMessage.BuilderParent parent) {
          super(parent);
          maybeForceBuilderInitialization();
        }
        private void maybeForceBuilderInitialization() {
          if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          }
        }
        private static Builder create() {
          return new Builder();
        }

        public Builder clear() {
          super.clear();
          path_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          span_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          leadingComments_ = "";
          bitField0_ = (bitField0_ & ~0x00000004);
          trailingComments_ = "";
          bitField0_ = (bitField0_ & ~0x00000008);
          return this;
        }

        public Builder clone() {
          return create().mergeFrom(buildPartial());
        }

        public com.google.protobuf.Descriptors.Descriptor
            getDescriptorForType() {
          return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
        }

        public com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location getDefaultInstanceForType() {
          return com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance();
        }

        public com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location build() {
          com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location result = buildPartial();
          if (!result.isInitialized()) {
            throw newUninitializedMessageException(result);
          }
          return result;
        }

        public com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location buildPartial() {
          com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location result = new com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location(this);
          int from_bitField0_ = bitField0_;
          int to_bitField0_ = 0;
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            path_ = java.util.Collections.unmodifiableList(path_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.path_ = path_;
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            span_ = java.util.Collections.unmodifiableList(span_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.span_ = span_;
          if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
            to_bitField0_ |= 0x00000001;
          }
          result.leadingComments_ = leadingComments_;
          if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
            to_bitField0_ |= 0x00000002;
          }
          result.trailingComments_ = trailingComments_;
          result.bitField0_ = to_bitField0_;
          onBuilt();
          return result;
        }

        public Builder mergeFrom(com.google.protobuf.Message other) {
          if (other instanceof com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location) {
            return mergeFrom((com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location)other);
          } else {
            super.mergeFrom(other);
            return this;
          }
        }

        public Builder mergeFrom(com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location other) {
          if (other == com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance()) return this;
          if (!other.path_.isEmpty()) {
            if (path_.isEmpty()) {
              path_ = other.path_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensurePathIsMutable();
              path_.addAll(other.path_);
            }
            onChanged();
          }
          if (!other.span_.isEmpty()) {
            if (span_.isEmpty()) {
              span_ = other.span_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureSpanIsMutable();
              span_.addAll(other.span_);
            }
            onChanged();
          }
          if (other.hasLeadingComments()) {
            bitField0_ |= 0x00000004;
            leadingComments_ = other.leadingComments_;
            onChanged();
          }
          if (other.hasTrailingComments()) {
            bitField0_ |= 0x00000008;
            trailingComments_ = other.trailingComments_;
            onChanged();
          }
          this.mergeUnknownFields(other.getUnknownFields());
          return this;
        }

        public final boolean isInitialized() {
          return true;
        }

        public Builder mergeFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
          com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location parsedMessage = null;
          try {
            parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            parsedMessage = (com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location) e.getUnfinishedMessage();
            throw e;
          } finally {
            if (parsedMessage != null) {
              mergeFrom(parsedMessage);
            }
          }
          return this;
        }
        private int bitField0_;

        private java.util.List<java.lang.Integer> path_ = java.util.Collections.emptyList();
        private void ensurePathIsMutable() {
          if (!((bitField0_ & 0x00000001) == 0x00000001)) {
            path_ = new java.util.ArrayList<java.lang.Integer>(path_);
            bitField0_ |= 0x00000001;
           }
        }

        public java.util.List<java.lang.Integer>
            getPathList() {
          return java.util.Collections.unmodifiableList(path_);
        }

        public int getPathCount() {
          return path_.size();
        }

        public int getPath(int index) {
          return path_.get(index);
        }

        public Builder setPath(
            int index, int value) {
          ensurePathIsMutable();
          path_.set(index, value);
          onChanged();
          return this;
        }

        public Builder addPath(int value) {
          ensurePathIsMutable();
          path_.add(value);
          onChanged();
          return this;
        }

        public Builder addAllPath(
            java.lang.Iterable<? extends java.lang.Integer> values) {
          ensurePathIsMutable();
          super.addAll(values, path_);
          onChanged();
          return this;
        }

        public Builder clearPath() {
          path_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
          return this;
        }

        private java.util.List<java.lang.Integer> span_ = java.util.Collections.emptyList();
        private void ensureSpanIsMutable() {
          if (!((bitField0_ & 0x00000002) == 0x00000002)) {
            span_ = new java.util.ArrayList<java.lang.Integer>(span_);
            bitField0_ |= 0x00000002;
           }
        }

        public java.util.List<java.lang.Integer>
            getSpanList() {
          return java.util.Collections.unmodifiableList(span_);
        }

        public int getSpanCount() {
          return span_.size();
        }

        public int getSpan(int index) {
          return span_.get(index);
        }

        public Builder setSpan(
            int index, int value) {
          ensureSpanIsMutable();
          span_.set(index, value);
          onChanged();
          return this;
        }

        public Builder addSpan(int value) {
          ensureSpanIsMutable();
          span_.add(value);
          onChanged();
          return this;
        }

        public Builder addAllSpan(
            java.lang.Iterable<? extends java.lang.Integer> values) {
          ensureSpanIsMutable();
          super.addAll(values, span_);
          onChanged();
          return this;
        }

        public Builder clearSpan() {
          span_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
          return this;
        }

        private java.lang.Object leadingComments_ = "";

        public boolean hasLeadingComments() {
          return ((bitField0_ & 0x00000004) == 0x00000004);
        }

        public java.lang.String getLeadingComments() {
          java.lang.Object ref = leadingComments_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            leadingComments_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }

        public com.google.protobuf.ByteString
            getLeadingCommentsBytes() {
          java.lang.Object ref = leadingComments_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            leadingComments_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }

        public Builder setLeadingComments(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
          leadingComments_ = value;
          onChanged();
          return this;
        }

        public Builder clearLeadingComments() {
          bitField0_ = (bitField0_ & ~0x00000004);
          leadingComments_ = getDefaultInstance().getLeadingComments();
          onChanged();
          return this;
        }

        public Builder setLeadingCommentsBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
          leadingComments_ = value;
          onChanged();
          return this;
        }

        private java.lang.Object trailingComments_ = "";

        public boolean hasTrailingComments() {
          return ((bitField0_ & 0x00000008) == 0x00000008);
        }

        public java.lang.String getTrailingComments() {
          java.lang.Object ref = trailingComments_;
          if (!(ref instanceof java.lang.String)) {
            java.lang.String s = ((com.google.protobuf.ByteString) ref)
                .toStringUtf8();
            trailingComments_ = s;
            return s;
          } else {
            return (java.lang.String) ref;
          }
        }

        public com.google.protobuf.ByteString
            getTrailingCommentsBytes() {
          java.lang.Object ref = trailingComments_;
          if (ref instanceof String) {
            com.google.protobuf.ByteString b = 
                com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref);
            trailingComments_ = b;
            return b;
          } else {
            return (com.google.protobuf.ByteString) ref;
          }
        }

        public Builder setTrailingComments(
            java.lang.String value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
          trailingComments_ = value;
          onChanged();
          return this;
        }

        public Builder clearTrailingComments() {
          bitField0_ = (bitField0_ & ~0x00000008);
          trailingComments_ = getDefaultInstance().getTrailingComments();
          onChanged();
          return this;
        }

        public Builder setTrailingCommentsBytes(
            com.google.protobuf.ByteString value) {
          if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
          trailingComments_ = value;
          onChanged();
          return this;
        }

      }

      static {
        defaultInstance = new Location(true);
        defaultInstance.initFields();
      }

    }

    public static final int LOCATION_FIELD_NUMBER = 1;
    private java.util.List<com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location> location_;

    public java.util.List<com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location> getLocationList() {
      return location_;
    }

    public java.util.List<? extends com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder> 
        getLocationOrBuilderList() {
      return location_;
    }

    public int getLocationCount() {
      return location_.size();
    }

    public com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location getLocation(int index) {
      return location_.get(index);
    }

    public com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder getLocationOrBuilder(
        int index) {
      return location_.get(index);
    }

    private void initFields() {
      location_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      for (int i = 0; i < location_.size(); i++) {
        output.writeMessage(1, location_.get(i));
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      for (int i = 0; i < location_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, location_.get(i));
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static com.google.protobuf.DescriptorProtos.SourceCodeInfo parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.google.protobuf.DescriptorProtos.SourceCodeInfo prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }

    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.google.protobuf.DescriptorProtos.SourceCodeInfoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.google.protobuf.DescriptorProtos.SourceCodeInfo.class, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Builder.class);
      }

      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getLocationFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (locationBuilder_ == null) {
          location_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
        } else {
          locationBuilder_.clear();
        }
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.google.protobuf.DescriptorProtos.internal_static_google_protobuf_SourceCodeInfo_descriptor;
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo getDefaultInstanceForType() {
        return com.google.protobuf.DescriptorProtos.SourceCodeInfo.getDefaultInstance();
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo build() {
        com.google.protobuf.DescriptorProtos.SourceCodeInfo result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo buildPartial() {
        com.google.protobuf.DescriptorProtos.SourceCodeInfo result = new com.google.protobuf.DescriptorProtos.SourceCodeInfo(this);
        int from_bitField0_ = bitField0_;
        if (locationBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001)) {
            location_ = java.util.Collections.unmodifiableList(location_);
            bitField0_ = (bitField0_ & ~0x00000001);
          }
          result.location_ = location_;
        } else {
          result.location_ = locationBuilder_.build();
        }
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.google.protobuf.DescriptorProtos.SourceCodeInfo) {
          return mergeFrom((com.google.protobuf.DescriptorProtos.SourceCodeInfo)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.google.protobuf.DescriptorProtos.SourceCodeInfo other) {
        if (other == com.google.protobuf.DescriptorProtos.SourceCodeInfo.getDefaultInstance()) return this;
        if (locationBuilder_ == null) {
          if (!other.location_.isEmpty()) {
            if (location_.isEmpty()) {
              location_ = other.location_;
              bitField0_ = (bitField0_ & ~0x00000001);
            } else {
              ensureLocationIsMutable();
              location_.addAll(other.location_);
            }
            onChanged();
          }
        } else {
          if (!other.location_.isEmpty()) {
            if (locationBuilder_.isEmpty()) {
              locationBuilder_.dispose();
              locationBuilder_ = null;
              location_ = other.location_;
              bitField0_ = (bitField0_ & ~0x00000001);
              locationBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getLocationFieldBuilder() : null;
            } else {
              locationBuilder_.addAllMessages(other.location_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.DescriptorProtos.SourceCodeInfo parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.google.protobuf.DescriptorProtos.SourceCodeInfo) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private java.util.List<com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location> location_ =
        java.util.Collections.emptyList();
      private void ensureLocationIsMutable() {
        if (!((bitField0_ & 0x00000001) == 0x00000001)) {
          location_ = new java.util.ArrayList<com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location>(location_);
          bitField0_ |= 0x00000001;
         }
      }

      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder, com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder> locationBuilder_;

      public java.util.List<com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location> getLocationList() {
        if (locationBuilder_ == null) {
          return java.util.Collections.unmodifiableList(location_);
        } else {
          return locationBuilder_.getMessageList();
        }
      }

      public int getLocationCount() {
        if (locationBuilder_ == null) {
          return location_.size();
        } else {
          return locationBuilder_.getCount();
        }
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location getLocation(int index) {
        if (locationBuilder_ == null) {
          return location_.get(index);
        } else {
          return locationBuilder_.getMessage(index);
        }
      }

      public Builder setLocation(
          int index, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location value) {
        if (locationBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureLocationIsMutable();
          location_.set(index, value);
          onChanged();
        } else {
          locationBuilder_.setMessage(index, value);
        }
        return this;
      }

      public Builder setLocation(
          int index, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder builderForValue) {
        if (locationBuilder_ == null) {
          ensureLocationIsMutable();
          location_.set(index, builderForValue.build());
          onChanged();
        } else {
          locationBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addLocation(com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location value) {
        if (locationBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureLocationIsMutable();
          location_.add(value);
          onChanged();
        } else {
          locationBuilder_.addMessage(value);
        }
        return this;
      }

      public Builder addLocation(
          int index, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location value) {
        if (locationBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureLocationIsMutable();
          location_.add(index, value);
          onChanged();
        } else {
          locationBuilder_.addMessage(index, value);
        }
        return this;
      }

      public Builder addLocation(
          com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder builderForValue) {
        if (locationBuilder_ == null) {
          ensureLocationIsMutable();
          location_.add(builderForValue.build());
          onChanged();
        } else {
          locationBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }

      public Builder addLocation(
          int index, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder builderForValue) {
        if (locationBuilder_ == null) {
          ensureLocationIsMutable();
          location_.add(index, builderForValue.build());
          onChanged();
        } else {
          locationBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }

      public Builder addAllLocation(
          java.lang.Iterable<? extends com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location> values) {
        if (locationBuilder_ == null) {
          ensureLocationIsMutable();
          super.addAll(values, location_);
          onChanged();
        } else {
          locationBuilder_.addAllMessages(values);
        }
        return this;
      }

      public Builder clearLocation() {
        if (locationBuilder_ == null) {
          location_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000001);
          onChanged();
        } else {
          locationBuilder_.clear();
        }
        return this;
      }

      public Builder removeLocation(int index) {
        if (locationBuilder_ == null) {
          ensureLocationIsMutable();
          location_.remove(index);
          onChanged();
        } else {
          locationBuilder_.remove(index);
        }
        return this;
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder getLocationBuilder(
          int index) {
        return getLocationFieldBuilder().getBuilder(index);
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder getLocationOrBuilder(
          int index) {
        if (locationBuilder_ == null) {
          return location_.get(index);  } else {
          return locationBuilder_.getMessageOrBuilder(index);
        }
      }

      public java.util.List<? extends com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder> 
           getLocationOrBuilderList() {
        if (locationBuilder_ != null) {
          return locationBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(location_);
        }
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder addLocationBuilder() {
        return getLocationFieldBuilder().addBuilder(
            com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance());
      }

      public com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder addLocationBuilder(
          int index) {
        return getLocationFieldBuilder().addBuilder(
            index, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.getDefaultInstance());
      }

      public java.util.List<com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder> 
           getLocationBuilderList() {
        return getLocationFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder, com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder> 
          getLocationFieldBuilder() {
        if (locationBuilder_ == null) {
          locationBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location, com.google.protobuf.DescriptorProtos.SourceCodeInfo.Location.Builder, com.google.protobuf.DescriptorProtos.SourceCodeInfo.LocationOrBuilder>(
                  location_,
                  ((bitField0_ & 0x00000001) == 0x00000001),
                  getParentForChildren(),
                  isClean());
          location_ = null;
        }
        return locationBuilder_;
      }

    }

    static {
      defaultInstance = new SourceCodeInfo(true);
      defaultInstance.initFields();
    }

  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_FileDescriptorSet_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_FileDescriptorProto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_DescriptorProto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_DescriptorProto_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_FieldDescriptorProto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_EnumDescriptorProto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_EnumValueDescriptorProto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_ServiceDescriptorProto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_MethodDescriptorProto_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_FileOptions_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_FileOptions_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_MessageOptions_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_MessageOptions_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_FieldOptions_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_FieldOptions_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_EnumOptions_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_EnumOptions_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_EnumValueOptions_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_ServiceOptions_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_ServiceOptions_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_MethodOptions_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_MethodOptions_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_UninterpretedOption_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_SourceCodeInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable;
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_google_protobuf_SourceCodeInfo_Location_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n google/protobuf/descriptor.proto\022\017goog" +
      "le.protobuf\"G\n\021FileDescriptorSet\0222\n\004file" +
      "\030\001 \003(\0132$.google.protobuf.FileDescriptorP" +
      "roto\"\313\003\n\023FileDescriptorProto\022\014\n\004name\030\001 \001" +
      "(\t\022\017\n\007package\030\002 \001(\t\022\022\n\ndependency\030\003 \003(\t\022" +
      "\031\n\021public_dependency\030\n \003(\005\022\027\n\017weak_depen" +
      "dency\030\013 \003(\005\0226\n\014message_type\030\004 \003(\0132 .goog" +
      "le.protobuf.DescriptorProto\0227\n\tenum_type" +
      "\030\005 \003(\0132$.google.protobuf.EnumDescriptorP" +
      "roto\0228\n\007service\030\006 \003(\0132\'.google.protobuf.",
      "ServiceDescriptorProto\0228\n\textension\030\007 \003(" +
      "\0132%.google.protobuf.FieldDescriptorProto" +
      "\022-\n\007options\030\010 \001(\0132\034.google.protobuf.File" +
      "Options\0229\n\020source_code_info\030\t \001(\0132\037.goog" +
      "le.protobuf.SourceCodeInfo\"\251\003\n\017Descripto" +
      "rProto\022\014\n\004name\030\001 \001(\t\0224\n\005field\030\002 \003(\0132%.go" +
      "ogle.protobuf.FieldDescriptorProto\0228\n\tex" +
      "tension\030\006 \003(\0132%.google.protobuf.FieldDes" +
      "criptorProto\0225\n\013nested_type\030\003 \003(\0132 .goog" +
      "le.protobuf.DescriptorProto\0227\n\tenum_type",
      "\030\004 \003(\0132$.google.protobuf.EnumDescriptorP" +
      "roto\022H\n\017extension_range\030\005 \003(\0132/.google.p" +
      "rotobuf.DescriptorProto.ExtensionRange\0220" +
      "\n\007options\030\007 \001(\0132\037.google.protobuf.Messag" +
      "eOptions\032,\n\016ExtensionRange\022\r\n\005start\030\001 \001(" +
      "\005\022\013\n\003end\030\002 \001(\005\"\224\005\n\024FieldDescriptorProto\022" +
      "\014\n\004name\030\001 \001(\t\022\016\n\006number\030\003 \001(\005\022:\n\005label\030\004" +
      " \001(\0162+.google.protobuf.FieldDescriptorPr" +
      "oto.Label\0228\n\004type\030\005 \001(\0162*.google.protobu" +
      "f.FieldDescriptorProto.Type\022\021\n\ttype_name",
      "\030\006 \001(\t\022\020\n\010extendee\030\002 \001(\t\022\025\n\rdefault_valu" +
      "e\030\007 \001(\t\022.\n\007options\030\010 \001(\0132\035.google.protob" +
      "uf.FieldOptions\"\266\002\n\004Type\022\017\n\013TYPE_DOUBLE\020" +
      "\001\022\016\n\nTYPE_FLOAT\020\002\022\016\n\nTYPE_INT64\020\003\022\017\n\013TYP" +
      "E_UINT64\020\004\022\016\n\nTYPE_INT32\020\005\022\020\n\014TYPE_FIXED" +
      "64\020\006\022\020\n\014TYPE_FIXED32\020\007\022\r\n\tTYPE_BOOL\020\010\022\017\n" +
      "\013TYPE_STRING\020\t\022\016\n\nTYPE_GROUP\020\n\022\020\n\014TYPE_M" +
      "ESSAGE\020\013\022\016\n\nTYPE_BYTES\020\014\022\017\n\013TYPE_UINT32\020" +
      "\r\022\r\n\tTYPE_ENUM\020\016\022\021\n\rTYPE_SFIXED32\020\017\022\021\n\rT" +
      "YPE_SFIXED64\020\020\022\017\n\013TYPE_SINT32\020\021\022\017\n\013TYPE_",
      "SINT64\020\022\"C\n\005Label\022\022\n\016LABEL_OPTIONAL\020\001\022\022\n" +
      "\016LABEL_REQUIRED\020\002\022\022\n\016LABEL_REPEATED\020\003\"\214\001" +
      "\n\023EnumDescriptorProto\022\014\n\004name\030\001 \001(\t\0228\n\005v" +
      "alue\030\002 \003(\0132).google.protobuf.EnumValueDe" +
      "scriptorProto\022-\n\007options\030\003 \001(\0132\034.google." +
      "protobuf.EnumOptions\"l\n\030EnumValueDescrip" +
      "torProto\022\014\n\004name\030\001 \001(\t\022\016\n\006number\030\002 \001(\005\0222" +
      "\n\007options\030\003 \001(\0132!.google.protobuf.EnumVa" +
      "lueOptions\"\220\001\n\026ServiceDescriptorProto\022\014\n" +
      "\004name\030\001 \001(\t\0226\n\006method\030\002 \003(\0132&.google.pro",
      "tobuf.MethodDescriptorProto\0220\n\007options\030\003" +
      " \001(\0132\037.google.protobuf.ServiceOptions\"\177\n" +
      "\025MethodDescriptorProto\022\014\n\004name\030\001 \001(\t\022\022\n\n" +
      "input_type\030\002 \001(\t\022\023\n\013output_type\030\003 \001(\t\022/\n" +
      "\007options\030\004 \001(\0132\036.google.protobuf.MethodO" +
      "ptions\"\351\003\n\013FileOptions\022\024\n\014java_package\030\001" +
      " \001(\t\022\034\n\024java_outer_classname\030\010 \001(\t\022\"\n\023ja" +
      "va_multiple_files\030\n \001(\010:\005false\022,\n\035java_g" +
      "enerate_equals_and_hash\030\024 \001(\010:\005false\022F\n\014" +
      "optimize_for\030\t \001(\0162).google.protobuf.Fil",
      "eOptions.OptimizeMode:\005SPEED\022\022\n\ngo_packa" +
      "ge\030\013 \001(\t\022\"\n\023cc_generic_services\030\020 \001(\010:\005f" +
      "alse\022$\n\025java_generic_services\030\021 \001(\010:\005fal" +
      "se\022\"\n\023py_generic_services\030\022 \001(\010:\005false\022C" +
      "\n\024uninterpreted_option\030\347\007 \003(\0132$.google.p" +
      "rotobuf.UninterpretedOption\":\n\014OptimizeM" +
      "ode\022\t\n\005SPEED\020\001\022\r\n\tCODE_SIZE\020\002\022\020\n\014LITE_RU" +
      "NTIME\020\003*\t\010\350\007\020\200\200\200\200\002\"\270\001\n\016MessageOptions\022&\n" +
      "\027message_set_wire_format\030\001 \001(\010:\005false\022.\n" +
      "\037no_standard_descriptor_accessor\030\002 \001(\010:\005",
      "false\022C\n\024uninterpreted_option\030\347\007 \003(\0132$.g" +
      "oogle.protobuf.UninterpretedOption*\t\010\350\007\020" +
      "\200\200\200\200\002\"\276\002\n\014FieldOptions\022:\n\005ctype\030\001 \001(\0162#." +
      "google.protobuf.FieldOptions.CType:\006STRI" +
      "NG\022\016\n\006packed\030\002 \001(\010\022\023\n\004lazy\030\005 \001(\010:\005false\022" +
      "\031\n\ndeprecated\030\003 \001(\010:\005false\022\034\n\024experiment" +
      "al_map_key\030\t \001(\t\022\023\n\004weak\030\n \001(\010:\005false\022C\n" +
      "\024uninterpreted_option\030\347\007 \003(\0132$.google.pr" +
      "otobuf.UninterpretedOption\"/\n\005CType\022\n\n\006S" +
      "TRING\020\000\022\010\n\004CORD\020\001\022\020\n\014STRING_PIECE\020\002*\t\010\350\007",
      "\020\200\200\200\200\002\"x\n\013EnumOptions\022\031\n\013allow_alias\030\002 \001" +
      "(\010:\004true\022C\n\024uninterpreted_option\030\347\007 \003(\0132" +
      "$.google.protobuf.UninterpretedOption*\t\010" +
      "\350\007\020\200\200\200\200\002\"b\n\020EnumValueOptions\022C\n\024uninterp" +
      "reted_option\030\347\007 \003(\0132$.google.protobuf.Un" +
      "interpretedOption*\t\010\350\007\020\200\200\200\200\002\"`\n\016ServiceO" +
      "ptions\022C\n\024uninterpreted_option\030\347\007 \003(\0132$." +
      "google.protobuf.UninterpretedOption*\t\010\350\007" +
      "\020\200\200\200\200\002\"_\n\rMethodOptions\022C\n\024uninterpreted" +
      "_option\030\347\007 \003(\0132$.google.protobuf.Uninter",
      "pretedOption*\t\010\350\007\020\200\200\200\200\002\"\236\002\n\023Uninterprete" +
      "dOption\022;\n\004name\030\002 \003(\0132-.google.protobuf." +
      "UninterpretedOption.NamePart\022\030\n\020identifi" +
      "er_value\030\003 \001(\t\022\032\n\022positive_int_value\030\004 \001" +
      "(\004\022\032\n\022negative_int_value\030\005 \001(\003\022\024\n\014double" +
      "_value\030\006 \001(\001\022\024\n\014string_value\030\007 \001(\014\022\027\n\017ag" +
      "gregate_value\030\010 \001(\t\0323\n\010NamePart\022\021\n\tname_" +
      "part\030\001 \002(\t\022\024\n\014is_extension\030\002 \002(\010\"\261\001\n\016Sou" +
      "rceCodeInfo\022:\n\010location\030\001 \003(\0132(.google.p" +
      "rotobuf.SourceCodeInfo.Location\032c\n\010Locat",
      "ion\022\020\n\004path\030\001 \003(\005B\002\020\001\022\020\n\004span\030\002 \003(\005B\002\020\001\022" +
      "\030\n\020leading_comments\030\003 \001(\t\022\031\n\021trailing_co" +
      "mments\030\004 \001(\tB)\n\023com.google.protobufB\020Des" +
      "criptorProtosH\001"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_google_protobuf_FileDescriptorSet_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_google_protobuf_FileDescriptorSet_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_FileDescriptorSet_descriptor,
              new java.lang.String[] { "File", });
          internal_static_google_protobuf_FileDescriptorProto_descriptor =
            getDescriptor().getMessageTypes().get(1);
          internal_static_google_protobuf_FileDescriptorProto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_FileDescriptorProto_descriptor,
              new java.lang.String[] { "Name", "Package", "Dependency", "PublicDependency", "WeakDependency", "MessageType", "EnumType", "Service", "Extension", "Options", "SourceCodeInfo", });
          internal_static_google_protobuf_DescriptorProto_descriptor =
            getDescriptor().getMessageTypes().get(2);
          internal_static_google_protobuf_DescriptorProto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_DescriptorProto_descriptor,
              new java.lang.String[] { "Name", "Field", "Extension", "NestedType", "EnumType", "ExtensionRange", "Options", });
          internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor =
            internal_static_google_protobuf_DescriptorProto_descriptor.getNestedTypes().get(0);
          internal_static_google_protobuf_DescriptorProto_ExtensionRange_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_DescriptorProto_ExtensionRange_descriptor,
              new java.lang.String[] { "Start", "End", });
          internal_static_google_protobuf_FieldDescriptorProto_descriptor =
            getDescriptor().getMessageTypes().get(3);
          internal_static_google_protobuf_FieldDescriptorProto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_FieldDescriptorProto_descriptor,
              new java.lang.String[] { "Name", "Number", "Label", "Type", "TypeName", "Extendee", "DefaultValue", "Options", });
          internal_static_google_protobuf_EnumDescriptorProto_descriptor =
            getDescriptor().getMessageTypes().get(4);
          internal_static_google_protobuf_EnumDescriptorProto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_EnumDescriptorProto_descriptor,
              new java.lang.String[] { "Name", "Value", "Options", });
          internal_static_google_protobuf_EnumValueDescriptorProto_descriptor =
            getDescriptor().getMessageTypes().get(5);
          internal_static_google_protobuf_EnumValueDescriptorProto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_EnumValueDescriptorProto_descriptor,
              new java.lang.String[] { "Name", "Number", "Options", });
          internal_static_google_protobuf_ServiceDescriptorProto_descriptor =
            getDescriptor().getMessageTypes().get(6);
          internal_static_google_protobuf_ServiceDescriptorProto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_ServiceDescriptorProto_descriptor,
              new java.lang.String[] { "Name", "Method", "Options", });
          internal_static_google_protobuf_MethodDescriptorProto_descriptor =
            getDescriptor().getMessageTypes().get(7);
          internal_static_google_protobuf_MethodDescriptorProto_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_MethodDescriptorProto_descriptor,
              new java.lang.String[] { "Name", "InputType", "OutputType", "Options", });
          internal_static_google_protobuf_FileOptions_descriptor =
            getDescriptor().getMessageTypes().get(8);
          internal_static_google_protobuf_FileOptions_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_FileOptions_descriptor,
              new java.lang.String[] { "JavaPackage", "JavaOuterClassname", "JavaMultipleFiles", "JavaGenerateEqualsAndHash", "OptimizeFor", "GoPackage", "CcGenericServices", "JavaGenericServices", "PyGenericServices", "UninterpretedOption", });
          internal_static_google_protobuf_MessageOptions_descriptor =
            getDescriptor().getMessageTypes().get(9);
          internal_static_google_protobuf_MessageOptions_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_MessageOptions_descriptor,
              new java.lang.String[] { "MessageSetWireFormat", "NoStandardDescriptorAccessor", "UninterpretedOption", });
          internal_static_google_protobuf_FieldOptions_descriptor =
            getDescriptor().getMessageTypes().get(10);
          internal_static_google_protobuf_FieldOptions_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_FieldOptions_descriptor,
              new java.lang.String[] { "Ctype", "Packed", "Lazy", "Deprecated", "ExperimentalMapKey", "Weak", "UninterpretedOption", });
          internal_static_google_protobuf_EnumOptions_descriptor =
            getDescriptor().getMessageTypes().get(11);
          internal_static_google_protobuf_EnumOptions_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_EnumOptions_descriptor,
              new java.lang.String[] { "AllowAlias", "UninterpretedOption", });
          internal_static_google_protobuf_EnumValueOptions_descriptor =
            getDescriptor().getMessageTypes().get(12);
          internal_static_google_protobuf_EnumValueOptions_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_EnumValueOptions_descriptor,
              new java.lang.String[] { "UninterpretedOption", });
          internal_static_google_protobuf_ServiceOptions_descriptor =
            getDescriptor().getMessageTypes().get(13);
          internal_static_google_protobuf_ServiceOptions_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_ServiceOptions_descriptor,
              new java.lang.String[] { "UninterpretedOption", });
          internal_static_google_protobuf_MethodOptions_descriptor =
            getDescriptor().getMessageTypes().get(14);
          internal_static_google_protobuf_MethodOptions_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_MethodOptions_descriptor,
              new java.lang.String[] { "UninterpretedOption", });
          internal_static_google_protobuf_UninterpretedOption_descriptor =
            getDescriptor().getMessageTypes().get(15);
          internal_static_google_protobuf_UninterpretedOption_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_UninterpretedOption_descriptor,
              new java.lang.String[] { "Name", "IdentifierValue", "PositiveIntValue", "NegativeIntValue", "DoubleValue", "StringValue", "AggregateValue", });
          internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor =
            internal_static_google_protobuf_UninterpretedOption_descriptor.getNestedTypes().get(0);
          internal_static_google_protobuf_UninterpretedOption_NamePart_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_UninterpretedOption_NamePart_descriptor,
              new java.lang.String[] { "NamePart", "IsExtension", });
          internal_static_google_protobuf_SourceCodeInfo_descriptor =
            getDescriptor().getMessageTypes().get(16);
          internal_static_google_protobuf_SourceCodeInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_SourceCodeInfo_descriptor,
              new java.lang.String[] { "Location", });
          internal_static_google_protobuf_SourceCodeInfo_Location_descriptor =
            internal_static_google_protobuf_SourceCodeInfo_descriptor.getNestedTypes().get(0);
          internal_static_google_protobuf_SourceCodeInfo_Location_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_google_protobuf_SourceCodeInfo_Location_descriptor,
              new java.lang.String[] { "Path", "Span", "LeadingComments", "TrailingComments", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

}
