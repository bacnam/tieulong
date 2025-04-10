

package com.google.protobuf;

import com.google.protobuf.LazyField.LazyIterator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class FieldSet<FieldDescriptorType extends
      FieldSet.FieldDescriptorLite<FieldDescriptorType>> {

  public interface FieldDescriptorLite<T extends FieldDescriptorLite<T>>
      extends Comparable<T> {
    int getNumber();
    WireFormat.FieldType getLiteType();
    WireFormat.JavaType getLiteJavaType();
    boolean isRepeated();
    boolean isPacked();
    Internal.EnumLiteMap<?> getEnumType();

    MessageLite.Builder internalMergeFrom(
        MessageLite.Builder to, MessageLite from);
  }

  private final SmallSortedMap<FieldDescriptorType, Object> fields;
  private boolean isImmutable;
  private boolean hasLazyField = false;

  private FieldSet() {
    this.fields = SmallSortedMap.newFieldMap(16);
  }

  private FieldSet(final boolean dummy) {
    this.fields = SmallSortedMap.newFieldMap(0);
    makeImmutable();
  }

  public static <T extends FieldSet.FieldDescriptorLite<T>>
      FieldSet<T> newFieldSet() {
    return new FieldSet<T>();
  }

  @SuppressWarnings("unchecked")
  public static <T extends FieldSet.FieldDescriptorLite<T>>
      FieldSet<T> emptySet() {
    return DEFAULT_INSTANCE;
  }
  @SuppressWarnings("rawtypes")
  private static final FieldSet DEFAULT_INSTANCE = new FieldSet(true);

  @SuppressWarnings("unchecked")
  public void makeImmutable() {
    if (isImmutable) {
      return;
    }
    fields.makeImmutable();
    isImmutable = true;
  }

  public boolean isImmutable() {
    return isImmutable;
  }

  @Override
  public FieldSet<FieldDescriptorType> clone() {

    FieldSet<FieldDescriptorType> clone = FieldSet.newFieldSet();
    for (int i = 0; i < fields.getNumArrayEntries(); i++) {
      Map.Entry<FieldDescriptorType, Object> entry = fields.getArrayEntryAt(i);
      FieldDescriptorType descriptor = entry.getKey();
      clone.setField(descriptor, entry.getValue());
    }
    for (Map.Entry<FieldDescriptorType, Object> entry :
             fields.getOverflowEntries()) {
      FieldDescriptorType descriptor = entry.getKey();
      clone.setField(descriptor, entry.getValue());
    }
    clone.hasLazyField = hasLazyField;
    return clone;
  }

  public void clear() {
    fields.clear();
    hasLazyField = false;
  }

  public Map<FieldDescriptorType, Object> getAllFields() {
    if (hasLazyField) {
      SmallSortedMap<FieldDescriptorType, Object> result =
          SmallSortedMap.newFieldMap(16);
      for (int i = 0; i < fields.getNumArrayEntries(); i++) {
        cloneFieldEntry(result, fields.getArrayEntryAt(i));
      }
      for (Map.Entry<FieldDescriptorType, Object> entry :
          fields.getOverflowEntries()) {
        cloneFieldEntry(result, entry);
      }
      if (fields.isImmutable()) {
        result.makeImmutable();
      }
      return result;
    }
    return fields.isImmutable() ? fields : Collections.unmodifiableMap(fields);
  }

  private void cloneFieldEntry(Map<FieldDescriptorType, Object> map,
      Map.Entry<FieldDescriptorType, Object> entry) {
    FieldDescriptorType key = entry.getKey();
    Object value = entry.getValue();
    if (value instanceof LazyField) {
      map.put(key, ((LazyField) value).getValue());
    } else {
      map.put(key, value);
    }
  }

  public Iterator<Map.Entry<FieldDescriptorType, Object>> iterator() {
    if (hasLazyField) {
      return new LazyIterator<FieldDescriptorType>(
          fields.entrySet().iterator());
    }
    return fields.entrySet().iterator();
  }

  public boolean hasField(final FieldDescriptorType descriptor) {
    if (descriptor.isRepeated()) {
      throw new IllegalArgumentException(
        "hasField() can only be called on non-repeated fields.");
    }

    return fields.get(descriptor) != null;
  }

  public Object getField(final FieldDescriptorType descriptor) {
    Object o = fields.get(descriptor);
    if (o instanceof LazyField) {
      return ((LazyField) o).getValue();
    }
    return o;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void setField(final FieldDescriptorType descriptor,
                       Object value) {
    if (descriptor.isRepeated()) {
      if (!(value instanceof List)) {
        throw new IllegalArgumentException(
          "Wrong object type used with protocol message reflection.");
      }

      final List newList = new ArrayList();
      newList.addAll((List) value);
      for (final Object element : newList) {
        verifyType(descriptor.getLiteType(), element);
      }
      value = newList;
    } else {
      verifyType(descriptor.getLiteType(), value);
    }

    if (value instanceof LazyField) {
      hasLazyField = true;
    }
    fields.put(descriptor, value);
  }

  public void clearField(final FieldDescriptorType descriptor) {
    fields.remove(descriptor);
    if (fields.isEmpty()) {
      hasLazyField = false;
    }
  }

  public int getRepeatedFieldCount(final FieldDescriptorType descriptor) {
    if (!descriptor.isRepeated()) {
      throw new IllegalArgumentException(
        "getRepeatedField() can only be called on repeated fields.");
    }

    final Object value = getField(descriptor);
    if (value == null) {
      return 0;
    } else {
      return ((List<?>) value).size();
    }
  }

  public Object getRepeatedField(final FieldDescriptorType descriptor,
                                 final int index) {
    if (!descriptor.isRepeated()) {
      throw new IllegalArgumentException(
        "getRepeatedField() can only be called on repeated fields.");
    }

    final Object value = getField(descriptor);

    if (value == null) {
      throw new IndexOutOfBoundsException();
    } else {
      return ((List<?>) value).get(index);
    }
  }

  @SuppressWarnings("unchecked")
  public void setRepeatedField(final FieldDescriptorType descriptor,
                               final int index,
                               final Object value) {
    if (!descriptor.isRepeated()) {
      throw new IllegalArgumentException(
        "getRepeatedField() can only be called on repeated fields.");
    }

    final Object list = getField(descriptor);
    if (list == null) {
      throw new IndexOutOfBoundsException();
    }

    verifyType(descriptor.getLiteType(), value);
    ((List<Object>) list).set(index, value);
  }

  @SuppressWarnings("unchecked")
  public void addRepeatedField(final FieldDescriptorType descriptor,
                               final Object value) {
    if (!descriptor.isRepeated()) {
      throw new IllegalArgumentException(
        "addRepeatedField() can only be called on repeated fields.");
    }

    verifyType(descriptor.getLiteType(), value);

    final Object existingValue = getField(descriptor);
    List<Object> list;
    if (existingValue == null) {
      list = new ArrayList<Object>();
      fields.put(descriptor, list);
    } else {
      list = (List<Object>) existingValue;
    }

    list.add(value);
  }

  private static void verifyType(final WireFormat.FieldType type,
                                 final Object value) {
    if (value == null) {
      throw new NullPointerException();
    }

    boolean isValid = false;
    switch (type.getJavaType()) {
      case INT:          isValid = value instanceof Integer   ; break;
      case LONG:         isValid = value instanceof Long      ; break;
      case FLOAT:        isValid = value instanceof Float     ; break;
      case DOUBLE:       isValid = value instanceof Double    ; break;
      case BOOLEAN:      isValid = value instanceof Boolean   ; break;
      case STRING:       isValid = value instanceof String    ; break;
      case BYTE_STRING:  isValid = value instanceof ByteString; break;
      case ENUM:

        isValid = value instanceof Internal.EnumLite;
        break;
      case MESSAGE:

        isValid =
            (value instanceof MessageLite) || (value instanceof LazyField);
        break;
    }

    if (!isValid) {

      throw new IllegalArgumentException(
        "Wrong object type used with protocol message reflection.");
    }
  }

  public boolean isInitialized() {
    for (int i = 0; i < fields.getNumArrayEntries(); i++) {
      if (!isInitialized(fields.getArrayEntryAt(i))) {
        return false;
      }
    }
    for (final Map.Entry<FieldDescriptorType, Object> entry :
             fields.getOverflowEntries()) {
      if (!isInitialized(entry)) {
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  private boolean isInitialized(
      final Map.Entry<FieldDescriptorType, Object> entry) {
    final FieldDescriptorType descriptor = entry.getKey();
    if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
      if (descriptor.isRepeated()) {
        for (final MessageLite element:
                 (List<MessageLite>) entry.getValue()) {
          if (!element.isInitialized()) {
            return false;
          }
        }
      } else {
        Object value = entry.getValue();
        if (value instanceof MessageLite) {
          if (!((MessageLite) value).isInitialized()) {
            return false;
          }
        } else if (value instanceof LazyField) {
          return true;
        } else {
          throw new IllegalArgumentException(
              "Wrong object type used with protocol message reflection.");
        }
      }
    }
    return true;
  }

  static int getWireFormatForFieldType(final WireFormat.FieldType type,
                                       boolean isPacked) {
    if (isPacked) {
      return WireFormat.WIRETYPE_LENGTH_DELIMITED;
    } else {
      return type.getWireType();
    }
  }

  public void mergeFrom(final FieldSet<FieldDescriptorType> other) {
    for (int i = 0; i < other.fields.getNumArrayEntries(); i++) {
      mergeFromField(other.fields.getArrayEntryAt(i));
    }
    for (final Map.Entry<FieldDescriptorType, Object> entry :
             other.fields.getOverflowEntries()) {
      mergeFromField(entry);
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void mergeFromField(
      final Map.Entry<FieldDescriptorType, Object> entry) {
    final FieldDescriptorType descriptor = entry.getKey();
    Object otherValue = entry.getValue();
    if (otherValue instanceof LazyField) {
      otherValue = ((LazyField) otherValue).getValue();
    }

    if (descriptor.isRepeated()) {
      Object value = getField(descriptor);
      if (value == null) {

        fields.put(descriptor, new ArrayList((List) otherValue));
      } else {

        ((List) value).addAll((List) otherValue);
      }
    } else if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE) {
      Object value = getField(descriptor);
      if (value == null) {
        fields.put(descriptor, otherValue);
      } else {

        fields.put(
            descriptor,
            descriptor.internalMergeFrom(
                ((MessageLite) value).toBuilder(), (MessageLite) otherValue)
            .build());
      }
    } else {
      fields.put(descriptor, otherValue);
    }
  }

  public static Object readPrimitiveField(
      CodedInputStream input,
      final WireFormat.FieldType type) throws IOException {
    switch (type) {
      case DOUBLE  : return input.readDouble  ();
      case FLOAT   : return input.readFloat   ();
      case INT64   : return input.readInt64   ();
      case UINT64  : return input.readUInt64  ();
      case INT32   : return input.readInt32   ();
      case FIXED64 : return input.readFixed64 ();
      case FIXED32 : return input.readFixed32 ();
      case BOOL    : return input.readBool    ();
      case STRING  : return input.readString  ();
      case BYTES   : return input.readBytes   ();
      case UINT32  : return input.readUInt32  ();
      case SFIXED32: return input.readSFixed32();
      case SFIXED64: return input.readSFixed64();
      case SINT32  : return input.readSInt32  ();
      case SINT64  : return input.readSInt64  ();

      case GROUP:
        throw new IllegalArgumentException(
          "readPrimitiveField() cannot handle nested groups.");
      case MESSAGE:
        throw new IllegalArgumentException(
          "readPrimitiveField() cannot handle embedded messages.");
      case ENUM:

        throw new IllegalArgumentException(
          "readPrimitiveField() cannot handle enums.");
    }

    throw new RuntimeException(
      "There is no way to get here, but the compiler thinks otherwise.");
  }

  public void writeTo(final CodedOutputStream output)
                      throws IOException {
    for (int i = 0; i < fields.getNumArrayEntries(); i++) {
      final Map.Entry<FieldDescriptorType, Object> entry =
          fields.getArrayEntryAt(i);
      writeField(entry.getKey(), entry.getValue(), output);
    }
    for (final Map.Entry<FieldDescriptorType, Object> entry :
         fields.getOverflowEntries()) {
      writeField(entry.getKey(), entry.getValue(), output);
    }
  }

  public void writeMessageSetTo(final CodedOutputStream output)
                                throws IOException {
    for (int i = 0; i < fields.getNumArrayEntries(); i++) {
      writeMessageSetTo(fields.getArrayEntryAt(i), output);
    }
    for (final Map.Entry<FieldDescriptorType, Object> entry :
             fields.getOverflowEntries()) {
      writeMessageSetTo(entry, output);
    }
  }

  private void writeMessageSetTo(
      final Map.Entry<FieldDescriptorType, Object> entry,
      final CodedOutputStream output) throws IOException {
    final FieldDescriptorType descriptor = entry.getKey();
    if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE &&
        !descriptor.isRepeated() && !descriptor.isPacked()) {
      output.writeMessageSetExtension(entry.getKey().getNumber(),
                                      (MessageLite) entry.getValue());
    } else {
      writeField(descriptor, entry.getValue(), output);
    }
  }

  private static void writeElement(final CodedOutputStream output,
                                   final WireFormat.FieldType type,
                                   final int number,
                                   final Object value) throws IOException {

    if (type == WireFormat.FieldType.GROUP) {
      output.writeGroup(number, (MessageLite) value);
    } else {
      output.writeTag(number, getWireFormatForFieldType(type, false));
      writeElementNoTag(output, type, value);
    }
  }

  private static void writeElementNoTag(
      final CodedOutputStream output,
      final WireFormat.FieldType type,
      final Object value) throws IOException {
    switch (type) {
      case DOUBLE  : output.writeDoubleNoTag  ((Double     ) value); break;
      case FLOAT   : output.writeFloatNoTag   ((Float      ) value); break;
      case INT64   : output.writeInt64NoTag   ((Long       ) value); break;
      case UINT64  : output.writeUInt64NoTag  ((Long       ) value); break;
      case INT32   : output.writeInt32NoTag   ((Integer    ) value); break;
      case FIXED64 : output.writeFixed64NoTag ((Long       ) value); break;
      case FIXED32 : output.writeFixed32NoTag ((Integer    ) value); break;
      case BOOL    : output.writeBoolNoTag    ((Boolean    ) value); break;
      case STRING  : output.writeStringNoTag  ((String     ) value); break;
      case GROUP   : output.writeGroupNoTag   ((MessageLite) value); break;
      case MESSAGE : output.writeMessageNoTag ((MessageLite) value); break;
      case BYTES   : output.writeBytesNoTag   ((ByteString ) value); break;
      case UINT32  : output.writeUInt32NoTag  ((Integer    ) value); break;
      case SFIXED32: output.writeSFixed32NoTag((Integer    ) value); break;
      case SFIXED64: output.writeSFixed64NoTag((Long       ) value); break;
      case SINT32  : output.writeSInt32NoTag  ((Integer    ) value); break;
      case SINT64  : output.writeSInt64NoTag  ((Long       ) value); break;

      case ENUM:
        output.writeEnumNoTag(((Internal.EnumLite) value).getNumber());
        break;
    }
  }

  public static void writeField(final FieldDescriptorLite<?> descriptor,
                                final Object value,
                                final CodedOutputStream output)
                                throws IOException {
    WireFormat.FieldType type = descriptor.getLiteType();
    int number = descriptor.getNumber();
    if (descriptor.isRepeated()) {
      final List<?> valueList = (List<?>)value;
      if (descriptor.isPacked()) {
        output.writeTag(number, WireFormat.WIRETYPE_LENGTH_DELIMITED);

        int dataSize = 0;
        for (final Object element : valueList) {
          dataSize += computeElementSizeNoTag(type, element);
        }
        output.writeRawVarint32(dataSize);

        for (final Object element : valueList) {
          writeElementNoTag(output, type, element);
        }
      } else {
        for (final Object element : valueList) {
          writeElement(output, type, number, element);
        }
      }
    } else {
      if (value instanceof LazyField) {
        writeElement(output, type, number, ((LazyField) value).getValue());
      } else {
        writeElement(output, type, number, value);
      }
    }
  }

  public int getSerializedSize() {
    int size = 0;
    for (int i = 0; i < fields.getNumArrayEntries(); i++) {
      final Map.Entry<FieldDescriptorType, Object> entry =
          fields.getArrayEntryAt(i);
      size += computeFieldSize(entry.getKey(), entry.getValue());
    }
    for (final Map.Entry<FieldDescriptorType, Object> entry :
         fields.getOverflowEntries()) {
      size += computeFieldSize(entry.getKey(), entry.getValue());
    }
    return size;
  }

  public int getMessageSetSerializedSize() {
    int size = 0;
    for (int i = 0; i < fields.getNumArrayEntries(); i++) {
      size += getMessageSetSerializedSize(fields.getArrayEntryAt(i));
    }
    for (final Map.Entry<FieldDescriptorType, Object> entry :
             fields.getOverflowEntries()) {
      size += getMessageSetSerializedSize(entry);
    }
    return size;
  }

  private int getMessageSetSerializedSize(
      final Map.Entry<FieldDescriptorType, Object> entry) {
    final FieldDescriptorType descriptor = entry.getKey();
    Object value = entry.getValue();
    if (descriptor.getLiteJavaType() == WireFormat.JavaType.MESSAGE
        && !descriptor.isRepeated() && !descriptor.isPacked()) {
      if (value instanceof LazyField) {
        return CodedOutputStream.computeLazyFieldMessageSetExtensionSize(
            entry.getKey().getNumber(), (LazyField) value);
      } else {
        return CodedOutputStream.computeMessageSetExtensionSize(
            entry.getKey().getNumber(), (MessageLite) value);
      }
    } else {
      return computeFieldSize(descriptor, value);
    }
  }

  private static int computeElementSize(
      final WireFormat.FieldType type,
      final int number, final Object value) {
    int tagSize = CodedOutputStream.computeTagSize(number);
    if (type == WireFormat.FieldType.GROUP) {
      tagSize *= 2;
    }
    return tagSize + computeElementSizeNoTag(type, value);
  }

  private static int computeElementSizeNoTag(
      final WireFormat.FieldType type, final Object value) {
    switch (type) {

      case DOUBLE  : return CodedOutputStream.computeDoubleSizeNoTag  ((Double     )value);
      case FLOAT   : return CodedOutputStream.computeFloatSizeNoTag   ((Float      )value);
      case INT64   : return CodedOutputStream.computeInt64SizeNoTag   ((Long       )value);
      case UINT64  : return CodedOutputStream.computeUInt64SizeNoTag  ((Long       )value);
      case INT32   : return CodedOutputStream.computeInt32SizeNoTag   ((Integer    )value);
      case FIXED64 : return CodedOutputStream.computeFixed64SizeNoTag ((Long       )value);
      case FIXED32 : return CodedOutputStream.computeFixed32SizeNoTag ((Integer    )value);
      case BOOL    : return CodedOutputStream.computeBoolSizeNoTag    ((Boolean    )value);
      case STRING  : return CodedOutputStream.computeStringSizeNoTag  ((String     )value);
      case GROUP   : return CodedOutputStream.computeGroupSizeNoTag   ((MessageLite)value);
      case BYTES   : return CodedOutputStream.computeBytesSizeNoTag   ((ByteString )value);
      case UINT32  : return CodedOutputStream.computeUInt32SizeNoTag  ((Integer    )value);
      case SFIXED32: return CodedOutputStream.computeSFixed32SizeNoTag((Integer    )value);
      case SFIXED64: return CodedOutputStream.computeSFixed64SizeNoTag((Long       )value);
      case SINT32  : return CodedOutputStream.computeSInt32SizeNoTag  ((Integer    )value);
      case SINT64  : return CodedOutputStream.computeSInt64SizeNoTag  ((Long       )value);

      case MESSAGE:
        if (value instanceof LazyField) {
          return CodedOutputStream.computeLazyFieldSizeNoTag((LazyField) value);
        } else {
          return CodedOutputStream.computeMessageSizeNoTag((MessageLite) value);
        }

      case ENUM:
        return CodedOutputStream.computeEnumSizeNoTag(
            ((Internal.EnumLite) value).getNumber());
    }

    throw new RuntimeException(
      "There is no way to get here, but the compiler thinks otherwise.");
  }

  public static int computeFieldSize(final FieldDescriptorLite<?> descriptor,
                                     final Object value) {
    WireFormat.FieldType type = descriptor.getLiteType();
    int number = descriptor.getNumber();
    if (descriptor.isRepeated()) {
      if (descriptor.isPacked()) {
        int dataSize = 0;
        for (final Object element : (List<?>)value) {
          dataSize += computeElementSizeNoTag(type, element);
        }
        return dataSize +
            CodedOutputStream.computeTagSize(number) +
            CodedOutputStream.computeRawVarint32Size(dataSize);
      } else {
        int size = 0;
        for (final Object element : (List<?>)value) {
          size += computeElementSize(type, number, element);
        }
        return size;
      }
    } else {
      return computeElementSize(type, number, value);
    }
  }
}
