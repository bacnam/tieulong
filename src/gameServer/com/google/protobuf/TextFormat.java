

package com.google.protobuf;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;

import java.io.IOException;
import java.nio.CharBuffer;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextFormat {
  private TextFormat() {}

  private static final Printer DEFAULT_PRINTER = new Printer();
  private static final Printer SINGLE_LINE_PRINTER =
      (new Printer()).setSingleLineMode(true);
  private static final Printer UNICODE_PRINTER =
      (new Printer()).setEscapeNonAscii(false);

  public static void print(final MessageOrBuilder message, final Appendable output)
                           throws IOException {
    DEFAULT_PRINTER.print(message, new TextGenerator(output));
  }

  public static void print(final UnknownFieldSet fields,
                           final Appendable output)
                           throws IOException {
    DEFAULT_PRINTER.printUnknownFields(fields, new TextGenerator(output));
  }

  public static String shortDebugString(final MessageOrBuilder message) {
    try {
      final StringBuilder sb = new StringBuilder();
      SINGLE_LINE_PRINTER.print(message, new TextGenerator(sb));

      return sb.toString().trim();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static String shortDebugString(final UnknownFieldSet fields) {
    try {
      final StringBuilder sb = new StringBuilder();
      SINGLE_LINE_PRINTER.printUnknownFields(fields, new TextGenerator(sb));

      return sb.toString().trim();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static String printToString(final MessageOrBuilder message) {
    try {
      final StringBuilder text = new StringBuilder();
      print(message, text);
      return text.toString();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static String printToString(final UnknownFieldSet fields) {
    try {
      final StringBuilder text = new StringBuilder();
      print(fields, text);
      return text.toString();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static String printToUnicodeString(final MessageOrBuilder message) {
    try {
      final StringBuilder text = new StringBuilder();
      UNICODE_PRINTER.print(message, new TextGenerator(text));
      return text.toString();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static String printToUnicodeString(final UnknownFieldSet fields) {
    try {
      final StringBuilder text = new StringBuilder();
      UNICODE_PRINTER.printUnknownFields(fields, new TextGenerator(text));
      return text.toString();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void printField(final FieldDescriptor field,
                                final Object value,
                                final Appendable output)
                                throws IOException {
    DEFAULT_PRINTER.printField(field, value, new TextGenerator(output));
  }

  public static String printFieldToString(final FieldDescriptor field,
                                          final Object value) {
    try {
      final StringBuilder text = new StringBuilder();
      printField(field, value, text);
      return text.toString();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static void printFieldValue(final FieldDescriptor field,
                                     final Object value,
                                     final Appendable output)
                                     throws IOException {
    DEFAULT_PRINTER.printFieldValue(field, value, new TextGenerator(output));
  }

  public static void printUnknownFieldValue(final int tag,
                                            final Object value,
                                            final Appendable output)
                                            throws IOException {
    printUnknownFieldValue(tag, value, new TextGenerator(output));
  }

  private static void printUnknownFieldValue(final int tag,
                                             final Object value,
                                             final TextGenerator generator)
                                             throws IOException {
    switch (WireFormat.getTagWireType(tag)) {
      case WireFormat.WIRETYPE_VARINT:
        generator.print(unsignedToString((Long) value));
        break;
      case WireFormat.WIRETYPE_FIXED32:
        generator.print(
            String.format((Locale) null, "0x%08x", (Integer) value));
        break;
      case WireFormat.WIRETYPE_FIXED64:
        generator.print(String.format((Locale) null, "0x%016x", (Long) value));
        break;
      case WireFormat.WIRETYPE_LENGTH_DELIMITED:
        generator.print("\"");
        generator.print(escapeBytes((ByteString) value));
        generator.print("\"");
        break;
      case WireFormat.WIRETYPE_START_GROUP:
        DEFAULT_PRINTER.printUnknownFields((UnknownFieldSet) value, generator);
        break;
      default:
        throw new IllegalArgumentException("Bad tag: " + tag);
    }
  }

  private static final class Printer {

    boolean singleLineMode = false;

    boolean escapeNonAscii = true;

    private Printer() {}

    private Printer setSingleLineMode(boolean singleLineMode) {
      this.singleLineMode = singleLineMode;
      return this;
    }

    private Printer setEscapeNonAscii(boolean escapeNonAscii) {
      this.escapeNonAscii = escapeNonAscii;
      return this;
    }

    private void print(final MessageOrBuilder message, final TextGenerator generator)
        throws IOException {
      for (Map.Entry<FieldDescriptor, Object> field
          : message.getAllFields().entrySet()) {
        printField(field.getKey(), field.getValue(), generator);
      }
      printUnknownFields(message.getUnknownFields(), generator);
    }

    private void printField(final FieldDescriptor field, final Object value,
        final TextGenerator generator) throws IOException {
      if (field.isRepeated()) {

        for (Object element : (List<?>) value) {
          printSingleField(field, element, generator);
        }
      } else {
        printSingleField(field, value, generator);
      }
    }

    private void printSingleField(final FieldDescriptor field,
                                  final Object value,
                                  final TextGenerator generator)
                                  throws IOException {
      if (field.isExtension()) {
        generator.print("[");

        if (field.getContainingType().getOptions().getMessageSetWireFormat()
            && (field.getType() == FieldDescriptor.Type.MESSAGE)
            && (field.isOptional())

            && (field.getExtensionScope() == field.getMessageType())) {
          generator.print(field.getMessageType().getFullName());
        } else {
          generator.print(field.getFullName());
        }
        generator.print("]");
      } else {
        if (field.getType() == FieldDescriptor.Type.GROUP) {

          generator.print(field.getMessageType().getName());
        } else {
          generator.print(field.getName());
        }
      }

      if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
        if (singleLineMode) {
          generator.print(" { ");
        } else {
          generator.print(" {\n");
          generator.indent();
        }
      } else {
        generator.print(": ");
      }

      printFieldValue(field, value, generator);

      if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
        if (singleLineMode) {
          generator.print("} ");
        } else {
          generator.outdent();
          generator.print("}\n");
        }
      } else {
        if (singleLineMode) {
          generator.print(" ");
        } else {
          generator.print("\n");
        }
      }
    }

    private void printFieldValue(final FieldDescriptor field,
                                 final Object value,
                                 final TextGenerator generator)
                                 throws IOException {
      switch (field.getType()) {
        case INT32:
        case SINT32:
        case SFIXED32:
          generator.print(((Integer) value).toString());
          break;

        case INT64:
        case SINT64:
        case SFIXED64:
          generator.print(((Long) value).toString());
          break;

        case BOOL:
          generator.print(((Boolean) value).toString());
          break;

        case FLOAT:
          generator.print(((Float) value).toString());
          break;

        case DOUBLE:
          generator.print(((Double) value).toString());
          break;

        case UINT32:
        case FIXED32:
          generator.print(unsignedToString((Integer) value));
          break;

        case UINT64:
        case FIXED64:
          generator.print(unsignedToString((Long) value));
          break;

        case STRING:
          generator.print("\"");
          generator.print(escapeNonAscii ?
              escapeText((String) value) :
              (String) value);
          generator.print("\"");
          break;

        case BYTES:
          generator.print("\"");
          generator.print(escapeBytes((ByteString) value));
          generator.print("\"");
          break;

        case ENUM:
          generator.print(((EnumValueDescriptor) value).getName());
          break;

        case MESSAGE:
        case GROUP:
          print((Message) value, generator);
          break;
      }
    }

    private void printUnknownFields(final UnknownFieldSet unknownFields,
                                    final TextGenerator generator)
                                    throws IOException {
      for (Map.Entry<Integer, UnknownFieldSet.Field> entry :
               unknownFields.asMap().entrySet()) {
        final int number = entry.getKey();
        final UnknownFieldSet.Field field = entry.getValue();
        printUnknownField(number, WireFormat.WIRETYPE_VARINT,
            field.getVarintList(), generator);
        printUnknownField(number, WireFormat.WIRETYPE_FIXED32,
            field.getFixed32List(), generator);
        printUnknownField(number, WireFormat.WIRETYPE_FIXED64,
            field.getFixed64List(), generator);
        printUnknownField(number, WireFormat.WIRETYPE_LENGTH_DELIMITED,
            field.getLengthDelimitedList(), generator);
        for (final UnknownFieldSet value : field.getGroupList()) {
          generator.print(entry.getKey().toString());
          if (singleLineMode) {
            generator.print(" { ");
          } else {
            generator.print(" {\n");
            generator.indent();
          }
          printUnknownFields(value, generator);
          if (singleLineMode) {
            generator.print("} ");
          } else {
            generator.outdent();
            generator.print("}\n");
          }
        }
      }
    }

    private void printUnknownField(final int number,
                                   final int wireType,
                                   final List<?> values,
                                   final TextGenerator generator)
                                   throws IOException {
      for (final Object value : values) {
        generator.print(String.valueOf(number));
        generator.print(": ");
        printUnknownFieldValue(wireType, value, generator);
        generator.print(singleLineMode ? " " : "\n");
      }
    }
  }

  private static String unsignedToString(final int value) {
    if (value >= 0) {
      return Integer.toString(value);
    } else {
      return Long.toString(((long) value) & 0x00000000FFFFFFFFL);
    }
  }

  private static String unsignedToString(final long value) {
    if (value >= 0) {
      return Long.toString(value);
    } else {

      return BigInteger.valueOf(value & 0x7FFFFFFFFFFFFFFFL)
                       .setBit(63).toString();
    }
  }

  private static final class TextGenerator {
    private final Appendable output;
    private final StringBuilder indent = new StringBuilder();
    private boolean atStartOfLine = true;

    private TextGenerator(final Appendable output) {
      this.output = output;
    }

    public void indent() {
      indent.append("  ");
    }

    public void outdent() {
      final int length = indent.length();
      if (length == 0) {
        throw new IllegalArgumentException(
            " Outdent() without matching Indent().");
      }
      indent.delete(length - 2, length);
    }

    public void print(final CharSequence text) throws IOException {
      final int size = text.length();
      int pos = 0;

      for (int i = 0; i < size; i++) {
        if (text.charAt(i) == '\n') {
          write(text.subSequence(pos, size), i - pos + 1);
          pos = i + 1;
          atStartOfLine = true;
        }
      }
      write(text.subSequence(pos, size), size - pos);
    }

    private void write(final CharSequence data, final int size)
                       throws IOException {
      if (size == 0) {
        return;
      }
      if (atStartOfLine) {
        atStartOfLine = false;
        output.append(indent);
      }
      output.append(data);
    }
  }

  private static final class Tokenizer {
    private final CharSequence text;
    private final Matcher matcher;
    private String currentToken;

    private int pos = 0;

    private int line = 0;
    private int column = 0;

    private int previousLine = 0;
    private int previousColumn = 0;

    private static final Pattern WHITESPACE =
      Pattern.compile("(\\s|(#.*$))++", Pattern.MULTILINE);
    private static final Pattern TOKEN = Pattern.compile(
      "[a-zA-Z_][0-9a-zA-Z_+-]*+|" +                
      "[.]?[0-9+-][0-9a-zA-Z_.+-]*+|" +             
      "\"([^\"\n\\\\]|\\\\.)*+(\"|\\\\?$)|" +       
      "\'([^\'\n\\\\]|\\\\.)*+(\'|\\\\?$)",         
      Pattern.MULTILINE);

    private static final Pattern DOUBLE_INFINITY = Pattern.compile(
      "-?inf(inity)?",
      Pattern.CASE_INSENSITIVE);
    private static final Pattern FLOAT_INFINITY = Pattern.compile(
      "-?inf(inity)?f?",
      Pattern.CASE_INSENSITIVE);
    private static final Pattern FLOAT_NAN = Pattern.compile(
      "nanf?",
      Pattern.CASE_INSENSITIVE);

    private Tokenizer(final CharSequence text) {
      this.text = text;
      this.matcher = WHITESPACE.matcher(text);
      skipWhitespace();
      nextToken();
    }

    public boolean atEnd() {
      return currentToken.length() == 0;
    }

    public void nextToken() {
      previousLine = line;
      previousColumn = column;

      while (pos < matcher.regionStart()) {
        if (text.charAt(pos) == '\n') {
          ++line;
          column = 0;
        } else {
          ++column;
        }
        ++pos;
      }

      if (matcher.regionStart() == matcher.regionEnd()) {

        currentToken = "";
      } else {
        matcher.usePattern(TOKEN);
        if (matcher.lookingAt()) {
          currentToken = matcher.group();
          matcher.region(matcher.end(), matcher.regionEnd());
        } else {

          currentToken = String.valueOf(text.charAt(pos));
          matcher.region(pos + 1, matcher.regionEnd());
        }

        skipWhitespace();
      }
    }

    private void skipWhitespace() {
      matcher.usePattern(WHITESPACE);
      if (matcher.lookingAt()) {
        matcher.region(matcher.end(), matcher.regionEnd());
      }
    }

    public boolean tryConsume(final String token) {
      if (currentToken.equals(token)) {
        nextToken();
        return true;
      } else {
        return false;
      }
    }

    public void consume(final String token) throws ParseException {
      if (!tryConsume(token)) {
        throw parseException("Expected \"" + token + "\".");
      }
    }

    public boolean lookingAtInteger() {
      if (currentToken.length() == 0) {
        return false;
      }

      final char c = currentToken.charAt(0);
      return ('0' <= c && c <= '9') ||
             c == '-' || c == '+';
    }

    public String consumeIdentifier() throws ParseException {
      for (int i = 0; i < currentToken.length(); i++) {
        final char c = currentToken.charAt(i);
        if (('a' <= c && c <= 'z') ||
            ('A' <= c && c <= 'Z') ||
            ('0' <= c && c <= '9') ||
            (c == '_') || (c == '.')) {

        } else {
          throw parseException("Expected identifier.");
        }
      }

      final String result = currentToken;
      nextToken();
      return result;
    }

    public int consumeInt32() throws ParseException {
      try {
        final int result = parseInt32(currentToken);
        nextToken();
        return result;
      } catch (NumberFormatException e) {
        throw integerParseException(e);
      }
    }

    public int consumeUInt32() throws ParseException {
      try {
        final int result = parseUInt32(currentToken);
        nextToken();
        return result;
      } catch (NumberFormatException e) {
        throw integerParseException(e);
      }
    }

    public long consumeInt64() throws ParseException {
      try {
        final long result = parseInt64(currentToken);
        nextToken();
        return result;
      } catch (NumberFormatException e) {
        throw integerParseException(e);
      }
    }

    public long consumeUInt64() throws ParseException {
      try {
        final long result = parseUInt64(currentToken);
        nextToken();
        return result;
      } catch (NumberFormatException e) {
        throw integerParseException(e);
      }
    }

    public double consumeDouble() throws ParseException {

      if (DOUBLE_INFINITY.matcher(currentToken).matches()) {
        final boolean negative = currentToken.startsWith("-");
        nextToken();
        return negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
      }
      if (currentToken.equalsIgnoreCase("nan")) {
        nextToken();
        return Double.NaN;
      }
      try {
        final double result = Double.parseDouble(currentToken);
        nextToken();
        return result;
      } catch (NumberFormatException e) {
        throw floatParseException(e);
      }
    }

    public float consumeFloat() throws ParseException {

      if (FLOAT_INFINITY.matcher(currentToken).matches()) {
        final boolean negative = currentToken.startsWith("-");
        nextToken();
        return negative ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
      }
      if (FLOAT_NAN.matcher(currentToken).matches()) {
        nextToken();
        return Float.NaN;
      }
      try {
        final float result = Float.parseFloat(currentToken);
        nextToken();
        return result;
      } catch (NumberFormatException e) {
        throw floatParseException(e);
      }
    }

    public boolean consumeBoolean() throws ParseException {
      if (currentToken.equals("true") ||
          currentToken.equals("t") ||
          currentToken.equals("1")) {
        nextToken();
        return true;
      } else if (currentToken.equals("false") ||
                 currentToken.equals("f") ||
                 currentToken.equals("0")) {
        nextToken();
        return false;
      } else {
        throw parseException("Expected \"true\" or \"false\".");
      }
    }

    public String consumeString() throws ParseException {
      return consumeByteString().toStringUtf8();
    }

    public ByteString consumeByteString() throws ParseException {
      List<ByteString> list = new ArrayList<ByteString>();
      consumeByteString(list);
      while (currentToken.startsWith("'") || currentToken.startsWith("\"")) {
        consumeByteString(list);
      }
      return ByteString.copyFrom(list);
    }

    private void consumeByteString(List<ByteString> list) throws ParseException {
      final char quote = currentToken.length() > 0 ? currentToken.charAt(0)
                                                   : '\0';
      if (quote != '\"' && quote != '\'') {
        throw parseException("Expected string.");
      }

      if (currentToken.length() < 2 ||
          currentToken.charAt(currentToken.length() - 1) != quote) {
        throw parseException("String missing ending quote.");
      }

      try {
        final String escaped =
            currentToken.substring(1, currentToken.length() - 1);
        final ByteString result = unescapeBytes(escaped);
        nextToken();
        list.add(result);
      } catch (InvalidEscapeSequenceException e) {
        throw parseException(e.getMessage());
      }
    }

    public ParseException parseException(final String description) {

      return new ParseException(
        line + 1, column + 1, description);
    }

    public ParseException parseExceptionPreviousToken(
        final String description) {

      return new ParseException(
        previousLine + 1, previousColumn + 1, description);
    }

    private ParseException integerParseException(
        final NumberFormatException e) {
      return parseException("Couldn't parse integer: " + e.getMessage());
    }

    private ParseException floatParseException(final NumberFormatException e) {
      return parseException("Couldn't parse number: " + e.getMessage());
    }
  }

  public static class ParseException extends IOException {
    private static final long serialVersionUID = 3196188060225107702L;

    private final int line;
    private final int column;

    public ParseException(final String message) {
      this(-1, -1, message);
    }

    public ParseException(final int line, final int column,
        final String message) {
      super(Integer.toString(line) + ":" + column + ": " + message);
      this.line = line;
      this.column = column;
    }

    public int getLine() {
      return line;
    }

    public int getColumn() {
      return column;
    }
  }

  public static void merge(final Readable input,
                           final Message.Builder builder)
                           throws IOException {
    merge(input, ExtensionRegistry.getEmptyRegistry(), builder);
  }

  public static void merge(final CharSequence input,
                           final Message.Builder builder)
                           throws ParseException {
    merge(input, ExtensionRegistry.getEmptyRegistry(), builder);
  }

  public static void merge(final Readable input,
                           final ExtensionRegistry extensionRegistry,
                           final Message.Builder builder)
                           throws IOException {

    merge(toStringBuilder(input), extensionRegistry, builder);
  }

  private static final int BUFFER_SIZE = 4096;

  private static StringBuilder toStringBuilder(final Readable input)
      throws IOException {
    final StringBuilder text = new StringBuilder();
    final CharBuffer buffer = CharBuffer.allocate(BUFFER_SIZE);
    while (true) {
      final int n = input.read(buffer);
      if (n == -1) {
        break;
      }
      buffer.flip();
      text.append(buffer, 0, n);
    }
    return text;
  }

  public static void merge(final CharSequence input,
                           final ExtensionRegistry extensionRegistry,
                           final Message.Builder builder)
                           throws ParseException {
    final Tokenizer tokenizer = new Tokenizer(input);

    while (!tokenizer.atEnd()) {
      mergeField(tokenizer, extensionRegistry, builder);
    }
  }

  private static void mergeField(final Tokenizer tokenizer,
                                 final ExtensionRegistry extensionRegistry,
                                 final Message.Builder builder)
                                 throws ParseException {
    FieldDescriptor field;
    final Descriptor type = builder.getDescriptorForType();
    ExtensionRegistry.ExtensionInfo extension = null;

    if (tokenizer.tryConsume("[")) {

      final StringBuilder name =
          new StringBuilder(tokenizer.consumeIdentifier());
      while (tokenizer.tryConsume(".")) {
        name.append('.');
        name.append(tokenizer.consumeIdentifier());
      }

      extension = extensionRegistry.findExtensionByName(name.toString());

      if (extension == null) {
        throw tokenizer.parseExceptionPreviousToken(
          "Extension \"" + name + "\" not found in the ExtensionRegistry.");
      } else if (extension.descriptor.getContainingType() != type) {
        throw tokenizer.parseExceptionPreviousToken(
          "Extension \"" + name + "\" does not extend message type \"" +
          type.getFullName() + "\".");
      }

      tokenizer.consume("]");

      field = extension.descriptor;
    } else {
      final String name = tokenizer.consumeIdentifier();
      field = type.findFieldByName(name);

      if (field == null) {

        final String lowerName = name.toLowerCase(Locale.US);
        field = type.findFieldByName(lowerName);

        if (field != null && field.getType() != FieldDescriptor.Type.GROUP) {
          field = null;
        }
      }

      if (field != null && field.getType() == FieldDescriptor.Type.GROUP &&
          !field.getMessageType().getName().equals(name)) {
        field = null;
      }

      if (field == null) {
        throw tokenizer.parseExceptionPreviousToken(
          "Message type \"" + type.getFullName() +
          "\" has no field named \"" + name + "\".");
      }
    }

    Object value = null;

    if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
      tokenizer.tryConsume(":");  

      final String endToken;
      if (tokenizer.tryConsume("<")) {
        endToken = ">";
      } else {
        tokenizer.consume("{");
        endToken = "}";
      }

      final Message.Builder subBuilder;
      if (extension == null) {
        subBuilder = builder.newBuilderForField(field);
      } else {
        subBuilder = extension.defaultInstance.newBuilderForType();
      }

      while (!tokenizer.tryConsume(endToken)) {
        if (tokenizer.atEnd()) {
          throw tokenizer.parseException(
            "Expected \"" + endToken + "\".");
        }
        mergeField(tokenizer, extensionRegistry, subBuilder);
      }

      value = subBuilder.buildPartial();

    } else {
      tokenizer.consume(":");

      switch (field.getType()) {
        case INT32:
        case SINT32:
        case SFIXED32:
          value = tokenizer.consumeInt32();
          break;

        case INT64:
        case SINT64:
        case SFIXED64:
          value = tokenizer.consumeInt64();
          break;

        case UINT32:
        case FIXED32:
          value = tokenizer.consumeUInt32();
          break;

        case UINT64:
        case FIXED64:
          value = tokenizer.consumeUInt64();
          break;

        case FLOAT:
          value = tokenizer.consumeFloat();
          break;

        case DOUBLE:
          value = tokenizer.consumeDouble();
          break;

        case BOOL:
          value = tokenizer.consumeBoolean();
          break;

        case STRING:
          value = tokenizer.consumeString();
          break;

        case BYTES:
          value = tokenizer.consumeByteString();
          break;

        case ENUM:
          final EnumDescriptor enumType = field.getEnumType();

          if (tokenizer.lookingAtInteger()) {
            final int number = tokenizer.consumeInt32();
            value = enumType.findValueByNumber(number);
            if (value == null) {
              throw tokenizer.parseExceptionPreviousToken(
                "Enum type \"" + enumType.getFullName() +
                "\" has no value with number " + number + '.');
            }
          } else {
            final String id = tokenizer.consumeIdentifier();
            value = enumType.findValueByName(id);
            if (value == null) {
              throw tokenizer.parseExceptionPreviousToken(
                "Enum type \"" + enumType.getFullName() +
                "\" has no value named \"" + id + "\".");
            }
          }

          break;

        case MESSAGE:
        case GROUP:
          throw new RuntimeException("Can't get here.");
      }
    }

    if (field.isRepeated()) {
      builder.addRepeatedField(field, value);
    } else {
      builder.setField(field, value);
    }
  }

  static String escapeBytes(final ByteString input) {
    final StringBuilder builder = new StringBuilder(input.size());
    for (int i = 0; i < input.size(); i++) {
      final byte b = input.byteAt(i);
      switch (b) {

        case 0x07: builder.append("\\a" ); break;
        case '\b': builder.append("\\b" ); break;
        case '\f': builder.append("\\f" ); break;
        case '\n': builder.append("\\n" ); break;
        case '\r': builder.append("\\r" ); break;
        case '\t': builder.append("\\t" ); break;
        case 0x0b: builder.append("\\v" ); break;
        case '\\': builder.append("\\\\"); break;
        case '\'': builder.append("\\\'"); break;
        case '"' : builder.append("\\\""); break;
        default:

          if (b >= 0x20) {
            builder.append((char) b);
          } else {
            builder.append('\\');
            builder.append((char) ('0' + ((b >>> 6) & 3)));
            builder.append((char) ('0' + ((b >>> 3) & 7)));
            builder.append((char) ('0' + (b & 7)));
          }
          break;
      }
    }
    return builder.toString();
  }

  static ByteString unescapeBytes(final CharSequence charString)
      throws InvalidEscapeSequenceException {

    ByteString input = ByteString.copyFromUtf8(charString.toString());

    final byte[] result = new byte[input.size()];
    int pos = 0;
    for (int i = 0; i < input.size(); i++) {
      byte c = input.byteAt(i);
      if (c == '\\') {
        if (i + 1 < input.size()) {
          ++i;
          c = input.byteAt(i);
          if (isOctal(c)) {

            int code = digitValue(c);
            if (i + 1 < input.size() && isOctal(input.byteAt(i + 1))) {
              ++i;
              code = code * 8 + digitValue(input.byteAt(i));
            }
            if (i + 1 < input.size() && isOctal(input.byteAt(i + 1))) {
              ++i;
              code = code * 8 + digitValue(input.byteAt(i));
            }

            result[pos++] = (byte)code;
          } else {
            switch (c) {
              case 'a' : result[pos++] = 0x07; break;
              case 'b' : result[pos++] = '\b'; break;
              case 'f' : result[pos++] = '\f'; break;
              case 'n' : result[pos++] = '\n'; break;
              case 'r' : result[pos++] = '\r'; break;
              case 't' : result[pos++] = '\t'; break;
              case 'v' : result[pos++] = 0x0b; break;
              case '\\': result[pos++] = '\\'; break;
              case '\'': result[pos++] = '\''; break;
              case '"' : result[pos++] = '\"'; break;

              case 'x':

                int code = 0;
                if (i + 1 < input.size() && isHex(input.byteAt(i + 1))) {
                  ++i;
                  code = digitValue(input.byteAt(i));
                } else {
                  throw new InvalidEscapeSequenceException(
                      "Invalid escape sequence: '\\x' with no digits");
                }
                if (i + 1 < input.size() && isHex(input.byteAt(i + 1))) {
                  ++i;
                  code = code * 16 + digitValue(input.byteAt(i));
                }
                result[pos++] = (byte)code;
                break;

              default:
                throw new InvalidEscapeSequenceException(
                    "Invalid escape sequence: '\\" + (char)c + '\'');
            }
          }
        } else {
          throw new InvalidEscapeSequenceException(
              "Invalid escape sequence: '\\' at end of string.");
        }
      } else {
        result[pos++] = c;
      }
    }

    return ByteString.copyFrom(result, 0, pos);
  }

  static class InvalidEscapeSequenceException extends IOException {
    private static final long serialVersionUID = -8164033650142593304L;

    InvalidEscapeSequenceException(final String description) {
      super(description);
    }
  }

  static String escapeText(final String input) {
    return escapeBytes(ByteString.copyFromUtf8(input));
  }

  static String unescapeText(final String input)
                             throws InvalidEscapeSequenceException {
    return unescapeBytes(input).toStringUtf8();
  }

  private static boolean isOctal(final byte c) {
    return '0' <= c && c <= '7';
  }

  private static boolean isHex(final byte c) {
    return ('0' <= c && c <= '9') ||
           ('a' <= c && c <= 'f') ||
           ('A' <= c && c <= 'F');
  }

  private static int digitValue(final byte c) {
    if ('0' <= c && c <= '9') {
      return c - '0';
    } else if ('a' <= c && c <= 'z') {
      return c - 'a' + 10;
    } else {
      return c - 'A' + 10;
    }
  }

  static int parseInt32(final String text) throws NumberFormatException {
    return (int) parseInteger(text, true, false);
  }

  static int parseUInt32(final String text) throws NumberFormatException {
    return (int) parseInteger(text, false, false);
  }

  static long parseInt64(final String text) throws NumberFormatException {
    return parseInteger(text, true, true);
  }

  static long parseUInt64(final String text) throws NumberFormatException {
    return parseInteger(text, false, true);
  }

  private static long parseInteger(final String text,
                                   final boolean isSigned,
                                   final boolean isLong)
                                   throws NumberFormatException {
    int pos = 0;

    boolean negative = false;
    if (text.startsWith("-", pos)) {
      if (!isSigned) {
        throw new NumberFormatException("Number must be positive: " + text);
      }
      ++pos;
      negative = true;
    }

    int radix = 10;
    if (text.startsWith("0x", pos)) {
      pos += 2;
      radix = 16;
    } else if (text.startsWith("0", pos)) {
      radix = 8;
    }

    final String numberText = text.substring(pos);

    long result = 0;
    if (numberText.length() < 16) {

      result = Long.parseLong(numberText, radix);
      if (negative) {
        result = -result;
      }

      if (!isLong) {
        if (isSigned) {
          if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
            throw new NumberFormatException(
              "Number out of range for 32-bit signed integer: " + text);
          }
        } else {
          if (result >= (1L << 32) || result < 0) {
            throw new NumberFormatException(
              "Number out of range for 32-bit unsigned integer: " + text);
          }
        }
      }
    } else {
      BigInteger bigValue = new BigInteger(numberText, radix);
      if (negative) {
        bigValue = bigValue.negate();
      }

      if (!isLong) {
        if (isSigned) {
          if (bigValue.bitLength() > 31) {
            throw new NumberFormatException(
              "Number out of range for 32-bit signed integer: " + text);
          }
        } else {
          if (bigValue.bitLength() > 32) {
            throw new NumberFormatException(
              "Number out of range for 32-bit unsigned integer: " + text);
          }
        }
      } else {
        if (isSigned) {
          if (bigValue.bitLength() > 63) {
            throw new NumberFormatException(
              "Number out of range for 64-bit signed integer: " + text);
          }
        } else {
          if (bigValue.bitLength() > 64) {
            throw new NumberFormatException(
              "Number out of range for 64-bit unsigned integer: " + text);
          }
        }
      }

      result = bigValue.longValue();
    }

    return result;
  }
}
