

package com.google.protobuf;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExtensionRegistryLite {

  private static volatile boolean eagerlyParseMessageSets = false;

  public static boolean isEagerlyParseMessageSets() {
    return eagerlyParseMessageSets;
  }

  public static void setEagerlyParseMessageSets(boolean isEagerlyParse) {
    eagerlyParseMessageSets = isEagerlyParse;
  }

  public static ExtensionRegistryLite newInstance() {
    return new ExtensionRegistryLite();
  }

  public static ExtensionRegistryLite getEmptyRegistry() {
    return EMPTY;
  }

  public ExtensionRegistryLite getUnmodifiable() {
    return new ExtensionRegistryLite(this);
  }

  @SuppressWarnings("unchecked")
  public <ContainingType extends MessageLite>
      GeneratedMessageLite.GeneratedExtension<ContainingType, ?>
        findLiteExtensionByNumber(
          final ContainingType containingTypeDefaultInstance,
          final int fieldNumber) {
    return (GeneratedMessageLite.GeneratedExtension<ContainingType, ?>)
      extensionsByNumber.get(
        new ObjectIntPair(containingTypeDefaultInstance, fieldNumber));
  }

  public final void add(
      final GeneratedMessageLite.GeneratedExtension<?, ?> extension) {
    extensionsByNumber.put(
      new ObjectIntPair(extension.getContainingTypeDefaultInstance(),
                        extension.getNumber()),
      extension);
  }

  ExtensionRegistryLite() {
    this.extensionsByNumber =
        new HashMap<ObjectIntPair,
                    GeneratedMessageLite.GeneratedExtension<?, ?>>();
  }

  ExtensionRegistryLite(ExtensionRegistryLite other) {
    if (other == EMPTY) {
      this.extensionsByNumber = Collections.emptyMap();
    } else {
      this.extensionsByNumber =
        Collections.unmodifiableMap(other.extensionsByNumber);
    }
  }

  private final Map<ObjectIntPair,
                    GeneratedMessageLite.GeneratedExtension<?, ?>>
      extensionsByNumber;

  private ExtensionRegistryLite(boolean empty) {
    this.extensionsByNumber = Collections.emptyMap();
  }
  private static final ExtensionRegistryLite EMPTY =
    new ExtensionRegistryLite(true);

  private static final class ObjectIntPair {
    private final Object object;
    private final int number;

    ObjectIntPair(final Object object, final int number) {
      this.object = object;
      this.number = number;
    }

    @Override
    public int hashCode() {
      return System.identityHashCode(object) * ((1 << 16) - 1) + number;
    }
    @Override
    public boolean equals(final Object obj) {
      if (!(obj instanceof ObjectIntPair)) {
        return false;
      }
      final ObjectIntPair other = (ObjectIntPair)obj;
      return object == other.object && number == other.number;
    }
  }
}
