package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.ConverterUtil;
import ch.qos.logback.core.pattern.parser.Node;
import ch.qos.logback.core.pattern.parser.Parser;
import ch.qos.logback.core.pattern.util.AlmostAsIsEscapeUtil;
import ch.qos.logback.core.pattern.util.IEscapeUtil;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.ScanException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FileNamePattern
        extends ContextAwareBase {
    static final Map<String, String> CONVERTER_MAP = new HashMap<String, String>();

    static {
        CONVERTER_MAP.put("i", IntegerTokenConverter.class.getName());

        CONVERTER_MAP.put("d", DateTokenConverter.class.getName());
    }

    String pattern;

    Converter<Object> headTokenConverter;

    public FileNamePattern(String patternArg, Context contextArg) {
        setPattern(FileFilterUtil.slashify(patternArg));
        setContext(contextArg);
        parse();
        ConverterUtil.startConverters(this.headTokenConverter);
    }

    void parse() {
        try {
            String patternForParsing = escapeRightParantesis(this.pattern);
            Parser<Object> p = new Parser(patternForParsing, (IEscapeUtil) new AlmostAsIsEscapeUtil());
            p.setContext(this.context);
            Node t = p.parse();
            this.headTokenConverter = p.compile(t, CONVERTER_MAP);
        } catch (ScanException sce) {
            addError("Failed to parse pattern \"" + this.pattern + "\".", (Throwable) sce);
        }
    }

    String escapeRightParantesis(String in) {
        return this.pattern.replace(")", "\\)");
    }

    public String toString() {
        return this.pattern;
    }

    public DateTokenConverter getPrimaryDateTokenConverter() {
        Converter<Object> p = this.headTokenConverter;

        while (p != null) {
            if (p instanceof DateTokenConverter) {
                DateTokenConverter dtc = (DateTokenConverter) p;

                if (dtc.isPrimary()) {
                    return dtc;
                }
            }
            p = p.getNext();
        }

        return null;
    }

    public IntegerTokenConverter getIntegerTokenConverter() {
        Converter<Object> p = this.headTokenConverter;

        while (p != null) {
            if (p instanceof IntegerTokenConverter) {
                return (IntegerTokenConverter) p;
            }

            p = p.getNext();
        }
        return null;
    }

    public String convertMultipleArguments(Object... objectList) {
        StringBuilder buf = new StringBuilder();
        Converter<Object> c = this.headTokenConverter;
        while (c != null) {
            if (c instanceof MonoTypedConverter) {
                MonoTypedConverter monoTyped = (MonoTypedConverter) c;
                for (Object o : objectList) {
                    if (monoTyped.isApplicable(o)) {
                        buf.append(c.convert(o));
                    }
                }
            } else {
                buf.append(c.convert(objectList));
            }
            c = c.getNext();
        }
        return buf.toString();
    }

    public String convert(Object o) {
        StringBuilder buf = new StringBuilder();
        Converter<Object> p = this.headTokenConverter;
        while (p != null) {
            buf.append(p.convert(o));
            p = p.getNext();
        }
        return buf.toString();
    }

    public String convertInt(int i) {
        return convert(Integer.valueOf(i));
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String pattern) {
        if (pattern != null) {
            this.pattern = pattern.trim();
        }
    }

    public String toRegexForFixedDate(Date date) {
        StringBuilder buf = new StringBuilder();
        Converter<Object> p = this.headTokenConverter;
        while (p != null) {
            if (p instanceof ch.qos.logback.core.pattern.LiteralConverter) {
                buf.append(p.convert(null));
            } else if (p instanceof IntegerTokenConverter) {
                buf.append("(\\d{1,3})");
            } else if (p instanceof DateTokenConverter) {
                buf.append(p.convert(date));
            }
            p = p.getNext();
        }
        return buf.toString();
    }

    public String toRegex() {
        StringBuilder buf = new StringBuilder();
        Converter<Object> p = this.headTokenConverter;
        while (p != null) {
            if (p instanceof ch.qos.logback.core.pattern.LiteralConverter) {
                buf.append(p.convert(null));
            } else if (p instanceof IntegerTokenConverter) {
                buf.append("\\d{1,2}");
            } else if (p instanceof DateTokenConverter) {
                DateTokenConverter<Object> dtc = (DateTokenConverter<Object>) p;
                buf.append(dtc.toRegex());
            }
            p = p.getNext();
        }
        return buf.toString();
    }
}

