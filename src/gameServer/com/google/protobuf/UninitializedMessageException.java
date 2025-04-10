

package com.google.protobuf;

import java.util.Collections;
import java.util.List;

public class UninitializedMessageException extends RuntimeException {
  private static final long serialVersionUID = -7466929953374883507L;

  public UninitializedMessageException(final MessageLite message) {
    super("Message was missing required fields.  (Lite runtime could not " +
          "determine which fields were missing).");
    missingFields = null;
  }

  public UninitializedMessageException(final List<String> missingFields) {
    super(buildDescription(missingFields));
    this.missingFields = missingFields;
  }

  private final List<String> missingFields;

  public List<String> getMissingFields() {
    return Collections.unmodifiableList(missingFields);
  }

  public InvalidProtocolBufferException asInvalidProtocolBufferException() {
    return new InvalidProtocolBufferException(getMessage());
  }

  private static String buildDescription(final List<String> missingFields) {
    final StringBuilder description =
      new StringBuilder("Message missing required fields: ");
    boolean first = true;
    for (final String field : missingFields) {
      if (first) {
        first = false;
      } else {
        description.append(", ");
      }
      description.append(field);
    }
    return description.toString();
  }
}
