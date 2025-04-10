

package com.google.protobuf;

import java.util.List;
import java.util.Map;

public interface MessageOrBuilder extends MessageLiteOrBuilder {

  Message getDefaultInstanceForType();

  List<String> findInitializationErrors();

  String getInitializationErrorString();

  Descriptors.Descriptor getDescriptorForType();

  Map<Descriptors.FieldDescriptor, Object> getAllFields();

  boolean hasField(Descriptors.FieldDescriptor field);

  Object getField(Descriptors.FieldDescriptor field);

  int getRepeatedFieldCount(Descriptors.FieldDescriptor field);

  Object getRepeatedField(Descriptors.FieldDescriptor field, int index);

  UnknownFieldSet getUnknownFields();
}
