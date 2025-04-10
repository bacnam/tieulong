

package com.google.protobuf;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

class LazyField {

  final private MessageLite defaultInstance;
  final private ExtensionRegistryLite extensionRegistry;

  private ByteString bytes;
  private volatile MessageLite value;
  private volatile boolean isDirty = false;

  public LazyField(MessageLite defaultInstance,
      ExtensionRegistryLite extensionRegistry, ByteString bytes) {
    this.defaultInstance = defaultInstance;
    this.extensionRegistry = extensionRegistry;
    this.bytes = bytes;
  }

  public MessageLite getValue() {
    ensureInitialized();
    return value;
  }

  public MessageLite setValue(MessageLite value) {
    MessageLite originalValue = this.value;
    this.value = value;
    bytes = null;
    isDirty = true;
    return originalValue;
  }

  public int getSerializedSize() {
    if (isDirty) {
      return value.getSerializedSize();
    }
    return bytes.size();
  }

  public ByteString toByteString() {
    if (!isDirty) {
      return bytes;
    }
    synchronized (this) {
      if (!isDirty) {
        return bytes;
      }
      bytes = value.toByteString();
      isDirty = false;
      return bytes;
    }
  }

  @Override
  public int hashCode() {
    ensureInitialized();
    return value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    ensureInitialized();
    return value.equals(obj);
  }

  @Override
  public String toString() {
    ensureInitialized();
    return value.toString();
  }

  private void ensureInitialized() {
    if (value != null) {
      return;
    }
    synchronized (this) {
      if (value != null) {
        return;
      }
      try {
        if (bytes != null) {
          value = defaultInstance.getParserForType()
              .parseFrom(bytes, extensionRegistry);
        }
      } catch (IOException e) {

      }
    }
  }

  static class LazyEntry<K> implements Entry<K, Object> {
    private Entry<K, LazyField> entry;

    private LazyEntry(Entry<K, LazyField> entry) {
      this.entry = entry;
    }

    public K getKey() {
      return entry.getKey();
    }

    public Object getValue() {
      LazyField field = entry.getValue();
      if (field == null) {
        return null;
      }
      return field.getValue();
    }

    public LazyField getField() {
      return entry.getValue();
    }

    public Object setValue(Object value) {
      if (!(value instanceof MessageLite)) {
        throw new IllegalArgumentException(
            "LazyField now only used for MessageSet, "
            + "and the value of MessageSet must be an instance of MessageLite");
      }
      return entry.getValue().setValue((MessageLite) value);
    }
  }

  static class LazyIterator<K> implements Iterator<Entry<K, Object>> {
    private Iterator<Entry<K, Object>> iterator;

    public LazyIterator(Iterator<Entry<K, Object>> iterator) {
      this.iterator = iterator;
    }

    public boolean hasNext() {
      return iterator.hasNext();
    }

    @SuppressWarnings("unchecked")
    public Entry<K, Object> next() {
      Entry<K, ?> entry = iterator.next();
      if (entry.getValue() instanceof LazyField) {
        return new LazyEntry<K>((Entry<K, LazyField>) entry);
      }
      return (Entry<K, Object>) entry;
    }

    public void remove() {
      iterator.remove();
    }
  }
}
